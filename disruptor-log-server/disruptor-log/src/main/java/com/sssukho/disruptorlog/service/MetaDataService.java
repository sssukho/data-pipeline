package com.sssukho.disruptorlog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sssukho.disruptorlog.constant.ConstValues;
import com.sssukho.disruptorlog.constant.ParsingKeys;
import com.sssukho.disruptorlog.meta.MetaInfo;
import com.sssukho.disruptorlog.meta.TraceMetaInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MetaDataService {

    public ResultSet resultSet = null;
    public ArrayList<String> tables = null;

    public Map<String, DisruptorService> dsMap;
    public Map<String, TraceMetaInfo> queryMap;

    private final ArrayProcessingService ais;
    private final JdbcTemplate jdbcTemplate;

    public DatabaseMetaData databaseMetaData;

    @PostConstruct
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        // classpath 기준으로 xxx.yml configuration 파일 read
        ClassPathResource classPathResource = new ClassPathResource(ConstValues.CONFIG_FILE_PATH);
        File configFileDirectory = classPathResource.getFile();
        File[] metaConfigFiles = configFileDirectory.listFiles();

        ArrayList<MetaInfo> metaInfoList = new ArrayList<>(metaConfigFiles.length);

        for(File configFile : metaConfigFiles) {
            metaInfoList.add(mapper.readValue(configFile, MetaInfo.class));
        }

        dsMap = new HashMap<String, DisruptorService>();
        queryMap = new HashMap<>();

        for(MetaInfo metaInfo : metaInfoList) {
            for(MetaInfo.Trace trace : metaInfo.getTrace()) {
                DisruptorService ds = new DisruptorService();
                TraceMetaInfo info = generateTableMetaInfo(trace.getTable());
                info.setInsertSQL(createInsertSQL(info.getTableName(), info.getColumnTypeMap()));

                TreeMap<String, MetaInfo.Trace.Chain> chainMap = new TreeMap<>();
                TreeMap<String, TreeMap<String, String>> chainColumnTypeMap = new TreeMap<>();
//                info.setChainMap(chainMap);
                info.setChainColumnTypeMap(chainColumnTypeMap);
                info.setName(trace.getName());
                ds.init(trace.getName(), trace.getThread(), info, ais);
                dsMap.put(trace.getName(), ds);
            }

            for(MetaInfo.Query query : metaInfo.getQuery()) {
                TraceMetaInfo info = generateTableMetaInfo(query.getTable());
                queryMap.put(query.getName(), info);
            }
        }
    }

    /***
     * JdbcTemplate 을 통해 해당 db의 메타 데이터
     * @param tableName resources/config/xxx.yml 파일 내에 있는 테이블명
     * @return
     * @throws Exception
     */
    public TraceMetaInfo generateTableMetaInfo(String tableName) throws Exception {
        String metaTableName = tableName;

        TraceMetaInfo info = new TraceMetaInfo();
        info.setTableName(tableName);
        TreeMap<String, String> colMap = new TreeMap<>();
        databaseMetaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        resultSet = databaseMetaData.getColumns(null, "public", (String) metaTableName, null);

        while(resultSet.next()) {
            String col = resultSet.getString("COLUMN_NAME");
            colMap.put(col, resultSet.getString("TYPE_NAME"));
        }

        info.setColumnTypeMap(colMap);

        return info;
    }

    /***
     * 테이블별 DB INSERT SQL 문 생성
     *
     * @param tableName : 부팅시 메모리에 들고 있는 DB 메타 정보의 테이블명
     * @param colMap : 부팅시 메모리에 들고 있는 DB 메타 정보의 컬럼 정보
     * @return Insert Query
     *
     * @author sssukho
     */
    private String createInsertSQL(String tableName, TreeMap<String, String> colMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + tableName + " (");
        boolean first = true;

        int count = 0;
        for(Map.Entry<String, String> entry: colMap.entrySet()) {
            if(first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(entry.getKey());
            count++;
        }

        sb.append(") VALUES (");
        for(int i = 0; i < count; i++) {
            if (i == 0) {

            } else {
                sb.append(",");
            }
            sb.append("?");
        }

        sb.append(")");

        return sb.toString();
    }

    /***
     *
     * @param info
     * @param queryMap
     * @return
     */
    public HashMap getSelectQueryInfo(TraceMetaInfo info, HashMap queryMap) {
        HashMap body = (HashMap) queryMap.get(ParsingKeys.REQUEST_MESSAGE_BODY);
        List<String> column = (List) body.get(ParsingKeys.REQUEST_MESSAGE_BODY_COLUMN);
        TreeMap<String, Object> condition = new TreeMap<>();
        condition.putAll((Map) body.get(ParsingKeys.REQUEST_MESSAGE_BODY_CONDITION));
        TreeMap<String, String> conditionColumn = new TreeMap<>();

        Integer offset = (Integer) body.get(ParsingKeys.REQUEST_MESSAGE_BODY_OFFSET);
        Integer limit = (Integer) body.get(ParsingKeys.REQUEST_MESSAGE_BODY_LIMIT);
        List<String> group = (List) body.get(ParsingKeys.REQUEST_MESSAGE_BODY_GROUP);
        List<String> order = (List) body.get(ParsingKeys.REQUEST_MESSAGE_BODY_ORDER);

        Integer isExport = (Integer) body.get(ParsingKeys.REQUEST_MESSAGE_BODY_EXPORT);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        boolean first = true;
        boolean isGroup = false;
        for (String col : column) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(col);
        }

        sb.append(" FROM ");
        sb.append(info.getTableName());

        if (!condition.isEmpty()) {

            sb.append(" WHERE ");

            first = true;
            for (Map.Entry<String, Object> entry : condition.entrySet()) {
                if (first) {
                    first = false;
                } else if(!first && !isGroup){
                    sb.append(" AND ");
                }

                if(entry.getKey().startsWith("{")) {
                    isGroup = true;
                    sb.append("(");

                    if(entry.getKey().contains("$")) {
                        String[] key = entry.getKey().split("[$]");
                        conditionColumn.put(entry.getKey(), key[0].substring(1));
                        sb.append(key[0].substring(1));
                        sb.append(" ");
                        sb.append(key[1]);
                        if(entry.getValue() instanceof List) {
                            sb.append(" (");
                            for(int i = 0; i < ((List) entry.getValue()).size(); i++) {
                                if(i == 0) {
                                    sb.append(" ?");
                                } else {
                                    sb.append(" , ?");
                                }
                            }
                            sb.append(" )");
                        } else {
                            sb.append(" ?");
                        }
                    } else {
                        conditionColumn.put(entry.getKey(), entry.getKey().substring(1));
                    }
                    continue;
                }

                if(entry.getKey().startsWith("|")) {
                    sb.append(" OR ");

                    if(entry.getKey().contains("$")) {
                        String[] key = entry.getKey().split("[$]");
                        conditionColumn.put(entry.getKey(), key[0].substring(1));
                        sb.append(key[0].substring(1));
                        sb.append(" ");
                        if(key[1].endsWith("}")) {
                            sb.append(key[1].substring(0, key[1].length()-1));
                        } else {
                            sb.append(key[1]);
                        }

                        if(entry.getValue() instanceof List) {
                            sb.append(" (");
                            for(int i = 0; i < ((List) entry.getValue()).size(); i++) {
                                if(i == 0) {
                                    sb.append(" ?");
                                } else {
                                    sb.append(" , ?");
                                }
                            }
                            sb.append(" )");
                        } else {
                            sb.append(" ?");
                        }
                    } else {
                        conditionColumn.put(entry.getKey(), entry.getKey().substring(1));
                    }

                    if(entry.getKey().endsWith("}")) {
                        sb.append(")");
                        isGroup = false;
                    }
                    continue;
                }

                if (entry.getKey().contains("$")) {
                    String[] key = entry.getKey().split("[$]");
                    conditionColumn.put(entry.getKey(), key[0]);
                    sb.append(key[0]);
                    sb.append(" ");
                    sb.append(key[1]);
                    if (entry.getValue() instanceof List) {
                        sb.append(" (");
                        for (int i = 0; i < ((List) entry.getValue()).size(); i++) {
                            if (i == 0) {
                                sb.append(" ?");
                            } else {
                                sb.append(" , ?");
                            }
                        }
                        sb.append(" )");
                    } else {
                        sb.append(" ?");
                    }
                } else {
                    conditionColumn.put(entry.getKey(), entry.getKey());
                    sb.append(entry.getKey());
                    sb.append(" = ?");
                }
            }
        }

        if (group != null) {
            first = true;
            sb.append(" GROUP BY");
            for (String s : group) {
                String[] ss = s.split(":");
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                for (String s1 : ss) {
                    sb.append(" " + s1);
                }
            }
            sb.append(" ");
            first = false;
        }

        if (order != null) {
            first = true;
            sb.append(" ORDER BY ");
            for (String s : order) {
                String[] ss = s.split(":");
                if (first) {
                    first = false;
                } else {
                    sb.append(" , ");
                }
                for (String s1 : ss) {
                    sb.append(" " + s1);
                }
            }
            sb.append(" ");
        }

        if (offset != null) {
            sb.append(" OFFSET " + offset);
        }
        if (limit != null) {
            sb.append(" LIMIT " + limit);
        }

        HashMap resultMap = new HashMap();
        resultMap.put(ParsingKeys.SELECT_QUERY, sb.toString());
        resultMap.put(ParsingKeys.CONDITION_COLUMN_TREE_MAP, conditionColumn);
        resultMap.put(ParsingKeys.CONDITION_TREE_MAP, condition);

        return resultMap;
    }
}
