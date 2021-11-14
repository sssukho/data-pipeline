package com.sssukho.disruptorlog.meta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MetaInfo {
    private List<Trace> trace;
    private List<Query> query;

    @Getter
    @Setter
    public static class Trace {
        private String name;
        private String version;
        private String table;
        private int thread;
        private String statPeriod;
        private String stattime;
        private String partition;
        private List<Chain> chain;

        @Getter
        @Setter
        public static class Chain {
            private String name;
            private Condition condition;
            private String type;
            private String table;
            private List<String> key;
            private List<String> column;
        }

        @Getter
        @Setter
        public static class Condition {
            private String key;
            private String value;
        }
    }

    @Getter
    @Setter
    public static class Query {
        private String name;
        private String version;
        private String table;
    }
}
