package com.sssukho.disruptorlog.service;

import com.sssukho.disruptorlog.meta.TraceMetaInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArrayProcessingService {

    static final SimpleDateFormat format = new SimpleDateFormat("yyyymmddHHmmss");
    static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public int[][] arrayInsert(TraceMetaInfo info, List<HashMap> logMap) {
        Long processTime = System.currentTimeMillis();

        int[][] updateCounts =
                jdbcTemplate.batchUpdate(
                        info.getInsertSQL(),
                        logMap,
                        logMap.size(),
                        new ParameterizedPreparedStatementSetter<HashMap>() {
                            @Override
                            public void setValues(PreparedStatement ps, HashMap argument)
                                    throws SQLException {
                                int i = 1;
                                for(Map.Entry<String, String> entry: info.getColumnTypeMap().entrySet()) {
                                    Object obj;
                                    switch(entry.getKey()) {
                                        case "process_time":
                                            obj = processTime;
                                            break;
                                        case "stat_time":
                                            obj =
                                    }
                                }
                            }
                        });
    }
}
