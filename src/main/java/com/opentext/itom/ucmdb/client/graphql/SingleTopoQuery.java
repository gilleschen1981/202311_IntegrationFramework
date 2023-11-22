package com.opentext.itom.ucmdb.client.graphql;

import com.opentext.itom.ucmdb.client.graphql.parser.Element;

public class SingleTopoQuery {
    public static final String PAYLOAD_OPENING_BRACE = "{";
    public static final String PAYLOAD_CLOSING_BRACE = "}";
    public static final String PAYLOAD_OPENING_BRACKET = "[";
    public static final String PAYLOAD_CLOSING_BRACKET = "]";
    public static final String PAYLOAD_OPENING_PARENTHESE = "(";
    public static final String PAYLOAD_CLOSING_PARENTHESE = ")";
    public static final String PAYLOAD_COLON = ":";
    public static final String PAYLOAD_COMMA = ",";
    public static final String PAYLOAD_KEYWORD_COUNT = "COUNT";
    public static final String PAYLOAD_KEYWORD_AND = "AND";
    public static final String PAYLOAD_KEYWORD_FILTER = "filter";
    public static final String PAYLOAD_KEYWORD_LIMIT = "limit";
    public static final String PAYLOAD_KEYWORD_SKIP = "skip";
    public static final String PAYLOAD_KEYWORD_FILED = "field";
    public static final String PAYLOAD_ATTRIBUTE_GLOBALID = "global_id";

    private Element element;

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String generateCountPayload() {
        return PAYLOAD_OPENING_BRACE + "\n" + element.generateCountPayload() + "\n" + PAYLOAD_CLOSING_BRACE;
    }

    public String generateBatchQueryPayload() {
        return PAYLOAD_OPENING_BRACE + "\n" + element.generateBatchQueryPayload() + "\n" + PAYLOAD_CLOSING_BRACE;
    }
}
