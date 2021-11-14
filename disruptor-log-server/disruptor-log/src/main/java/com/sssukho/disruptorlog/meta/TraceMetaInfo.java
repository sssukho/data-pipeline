package com.sssukho.disruptorlog.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.TreeMap;

@Getter
@Setter
@ToString
public class TraceMetaInfo {
    String name;
    String tableName;
    String insertSQL;
    String partition;
    TreeMap<String, String> columnTypeMap;
    TreeMap<String, String> chainMap;
    TreeMap<String, TreeMap<String, String>> chainColumnTypeMap;



}
