package com.sssukho.disruptorlog.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.asm.Type;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

@Slf4j
public class SQLUtils {

    public static void prepareSet(PreparedStatement ps, int i, String key, String colType, Object obj)
            throws SQLException {
        try {
            switch (colType) {
                case "citext":
                case "varchar":
                case "text":
                case "bpchar":
                    if (obj == null) {
                        ps.setString(i, "");
                    } else {
                        ps.setString(i, (String) obj);
                    }
                    break;
                case "numeric":
                case "int4":
                    if (obj == null) {
                        ps.setInt(i, 0);
                    } else if(obj instanceof String) {
                        ps.setLong(i, Integer.parseInt((String) obj));
                    } else {
                        ps.setInt(i, (Integer) obj);
                    }
                    break;
                case "int8":
                    if (obj == null) {
                        ps.setLong(i, 0);
                    } else if (obj instanceof Integer) {
                        ps.setLong(i, (Integer) obj);
                    } else if (obj instanceof Long) {
                        ps.setLong(i, (Long) obj);
                    } else if (obj instanceof String) {
                        ps.setLong(i, Long.parseLong((String)obj));
                    }
                    break;
                case "float4":
                    if (obj == null) {
                        ps.setDouble(i, 0.0);
                    } else {
                        ps.setDouble(i, (Double) obj);
                    }
                    break;
                case "bool":
                    if (obj == null) {
                        ps.setNull(i, Type.BOOLEAN);
                    } else {
                        ps.setBoolean(i, (Boolean) obj);
                    }
                    break;
                case "timestamp":
                    try {
                        if (obj instanceof Long) {
                            Timestamp ts = new Timestamp((Long) obj);
                            ps.setTimestamp(i, ts);
                        } else if (obj instanceof String) {
                            String v = (String) obj;
                            if (obj == null ||
                                    v.length() == 0) {
                                ps.setNull(i, Types.TIMESTAMP_WITH_TIMEZONE);
                            } else {
                                ps.setTimestamp(i, Timestamp.valueOf(v));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ps.setNull(i, Types.TIMESTAMP_WITH_TIMEZONE);
                    }
                    break;
                default:
                    throw new SQLException(
                            "unsupported db column type["
                                    + colType
                                    + "] key["
                                    + key
                                    + "] value["
                                    + obj
                                    + "]");
            }
        } catch (SQLException e) {
            log.error(
                    "SQL Parsing error key[" + key + "] col type[" + colType + " obj[" + obj + "]");
            throw e;
        } catch (Exception e) {
            log.error(
                    "SQL Parsing error key[" + key + "] col type[" + colType + " obj[" + obj + "]");
            throw e;
        }
    }

    public static void prepareSet(PreparedStatement ps, int i, String colType, Object obj)
            throws SQLException {
        try {
            switch (colType) {
                case "varchar":
                case "text":
                case "citext":
                case "bpchar":
                    ps.setString(i, (String) obj);
                    break;
                case "numeric":
                case "int4":
                    ps.setInt(i, (Integer) obj);
                    break;
                case "int8":
                    if (obj == null) {
                        ps.setLong(i, 0);
                    } else if (obj instanceof Integer) {
                        ps.setLong(i, (Integer) obj);
                    } else if (obj instanceof Long) {
                        ps.setLong(i, (Long) obj);
                    }
                    break;
                case "float4":
                    ps.setDouble(i, (Double) obj);
                    break;
                case "bool":
                    ps.setBoolean(i, (Boolean) obj);
                    break;
                case "timestamp":
                    String v = (String) obj;
                    ps.setTimestamp(i, Timestamp.valueOf(v));
                    break;
                default:
                    throw new SQLException("unsupported db column type: " + colType + " col:" + obj);
            }
        } catch (Exception e) {
            log.error("SQL Parsing error col type[" + colType + " obj[" + obj + "]");
            throw e;
        }

    }
}
