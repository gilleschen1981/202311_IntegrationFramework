package com.opentext.itom.ucmdb.integration.push.repo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableColumnMeta {
    private static final Logger log = LoggerFactory.getLogger(TableColumnMeta.class);
    public static final String RELATION_TABLE_ENDA_COLUMNNAME = "end1";
    public static final String RELATION_TABLE_ENDB_COLUMNNAME = "end2";
    private final String columnName;
    private final String columnType;
    private final int columnSize;

    public TableColumnMeta(String columnName, String columnType, int columnSize) {
        if("VARCHAR".equals(columnType) && columnSize <= 0){
            log.debug("[CONVERT]Missing size information, use default value. AttrName = " + columnName);
            columnSize = 100;
        }
        this.columnName = columnName;
        this.columnType = columnType;
        this.columnSize = columnSize;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public int getColumnSize() {
        return columnSize;
    }
}
