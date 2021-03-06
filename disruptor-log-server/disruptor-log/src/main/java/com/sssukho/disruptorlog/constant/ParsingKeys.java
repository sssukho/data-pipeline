package com.sssukho.disruptorlog.constant;

public class ParsingKeys {
    /* Request Message Fields */
    public static final String REQUEST_MESSAGE_HEADER = "header";
    public static final String REQUEST_MESSAGE_HEADER_TYPE = "type";
    public static final String REQUEST_MESSAGE_HEADER_VERSION = "version";
    public static final String REQUEST_MESSAGE_BODY = "body";
    public static final String REQUEST_MESSAGE_BODY_COLUMN = "column";
    public static final String REQUEST_MESSAGE_BODY_CONDITION = "condition";
    public static final String REQUEST_MESSAGE_BODY_OFFSET  = "offset";
    public static final String REQUEST_MESSAGE_BODY_GROUP = "group";
    public static final String REQUEST_MESSAGE_BODY_ORDER = "order";
    public static final String REQUEST_MESSAGE_BODY_LIMIT = "limit";
    public static final String REQUEST_MESSAGE_BODY_EXPORT = "export";
    /* Meta info keys */
    public static final String SELECT_QUERY = "selectQuery";
    public static final String CONDITION_TREE_MAP = "conditionTreeMap";
    public static final String CONDITION_COLUMN_TREE_MAP = "conditionColumnTreeMap";
    /* Fields in log map */
    public static final String LOG_ID = "log_id";

    /* Keys in response message */
    public static final String RESPONSE_MESSAGE_ERROR_MESSAGE = "errorMessage";
    public static final String RESPONSE_MESSAGE_ERROR_CODE = "errorCode";
}
