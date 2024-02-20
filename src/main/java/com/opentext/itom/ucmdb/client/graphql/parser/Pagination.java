package com.opentext.itom.ucmdb.client.graphql.parser;

import com.opentext.itom.ucmdb.client.graphql.SingleTopoQuery;

public class Pagination {
    private int limit;

    private int skip;

    public Pagination(int limit, int skip) {
        this.limit = limit;
        this.skip = skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String generatePayload() {
        return SingleTopoQuery.PAYLOAD_KEYWORD_LIMIT + SingleTopoQuery.PAYLOAD_COLON + limit
                + SingleTopoQuery.PAYLOAD_COMMA + SingleTopoQuery.PAYLOAD_KEYWORD_SKIP + SingleTopoQuery.PAYLOAD_COLON
                + skip;
    }
}
