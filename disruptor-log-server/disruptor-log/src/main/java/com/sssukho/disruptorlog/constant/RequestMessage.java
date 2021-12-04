package com.sssukho.disruptorlog.constant;

public enum RequestMessage {

    HEADER("header");

    private String key;

    RequestMessage(String key) {
        this.key = key;
    }


//    public static final String REQUEST_MESSAGE_HEADER = "header";
//    public static final String REQUEST_MESSAGE_HEADER_TYPE = "type";
//    public static final String REQUEST_MESSAGE_HEADER_VERSION = "version";
//    public static final String REQUEST_MESSAGE_BODY = "body";
//    public static final String REQUEST_MESSAGE_BODY_COLUMN = "column";
//    public static final String REQUEST_MESSAGE_BODY_CONDITION = "condition";
//    public static final String REQUEST_MESSAGE_BODY_OFFSET  = "offset";
//    public static final String REQUEST_MESSAGE_BODY_GROUP = "group";
//    public static final String REQUEST_MESSAGE_BODY_ORDER = "order";
//    public static final String REQUEST_MESSAGE_BODY_LIMIT = "limit";
//    public static final String REQUEST_MESSAGE_BODY_EXPORT = "export";
}
