package com.sssukho.disruptorlog.service;

import com.sssukho.disruptorlog.constant.ParsingKeys;
import com.sssukho.disruptorlog.meta.TraceMetaInfo;
import com.sssukho.disruptorlog.util.SQLUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryService {

    private final JdbcTemplate jdbcTemplate;
    private final MetaDataService metaDataService;

    public Object query(TraceMetaInfo info, HashMap queryMap) {

        HashMap selectQueryInfo = metaDataService.getSelectQueryInfo(info, queryMap);

        String sql = (String) selectQueryInfo.get(ParsingKeys.SELECT_QUERY);
        ArrayList<HashMap<String, Object>> result;

        TreeMap<String, String> conditionColumn =
                (TreeMap<String, String>) selectQueryInfo.get(ParsingKeys.CONDITION_COLUMN_TREE_MAP);
        TreeMap<String, Object> condition =
                (TreeMap<String, Object>) selectQueryInfo.get(ParsingKeys.CONDITION_TREE_MAP);

        try {
            result = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareCall(sql);
                    int i = 1;

                    for (Map.Entry<String, String> entry : conditionColumn.entrySet()) {
                        String key = entry.getKey();
                        String colType = info.getColumnTypeMap().get(entry.getValue());
                        Object obj = condition.get(key);
                        if (obj instanceof List) {
                            for (int j = 0; j < ((List) obj).size(); j++) {
                                SQLUtils.prepareSet(ps, i, colType, ((List) obj).get(j));
                                i++;
                            }
                        } else {
                            SQLUtils.prepareSet(ps, i, colType, obj);
                            i++;
                        }

                    }
                    log.info(ps.toString());
                    return ps;
                }
            }, new ResultSetExtractor<ArrayList<HashMap<String, Object>>>() {

                @Override
                public ArrayList<HashMap<String, Object>> extractData(ResultSet rs)
                        throws SQLException, DataAccessException {
                    ResultSetMetaData md = rs.getMetaData();
                    int columns = md.getColumnCount();
                    ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

                    while (rs.next()) {
                        HashMap<String, Object> row = new HashMap<String, Object>(columns);
                        for (int i = 1; i <= columns; ++i) {
                            if(md.getColumnTypeName(i).equals("citext")) {
                                row.put(md.getColumnName(i), rs.getObject(i).toString());
                            } else {
                                row.put(md.getColumnName(i), rs.getObject(i));
                            }
                        }
                        list.add(row);
                    }

                    return list;
                }
            });
        } catch (Exception e) {
            log.error("Query error occured : {}", e);

            return ResponseEntity.badRequest();
        }

        return ResponseEntity.ok();
    }
}
