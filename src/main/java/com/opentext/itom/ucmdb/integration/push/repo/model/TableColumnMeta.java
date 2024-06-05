package com.opentext.itom.ucmdb.integration.push.repo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableColumnMeta implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(TableColumnMeta.class);
    public static final String RELATION_TABLE_ENDA_COLUMNNAME = "end1";
    public static final String RELATION_TABLE_ENDB_COLUMNNAME = "end2";
    public static final String RELATION_TABLE_ENDATYPE_COLUMNNAME = "end1_type";
    public static final String RELATION_TABLE_ENDBTYPE_COLUMNNAME = "end2_type";
    public static final String RELATION_TABLE_ENDA_ACTUALTYPE_COLUMNNAME = "end1_actual_type";
    public static final String RELATION_TABLE_ENDB_ACTUALTYPE_COLUMNNAME = "end2_actual_type";
    public static final String RELATION_TABLE_TYPE_COLUMNNAME = "type";
    public static final String RELATION_TABLE_ID_COLUMNNAME = "global_id";
    public static final String RELATION_TABLE_LASTACCESSTIME_COLUMNNAME = "root_lastaccesstime";

    public static final List<String> relationColumnList = Arrays.asList(
            RELATION_TABLE_ID_COLUMNNAME, RELATION_TABLE_TYPE_COLUMNNAME
            , RELATION_TABLE_ENDA_COLUMNNAME, RELATION_TABLE_ENDB_COLUMNNAME
            , RELATION_TABLE_ENDATYPE_COLUMNNAME, RELATION_TABLE_ENDBTYPE_COLUMNNAME
            , RELATION_TABLE_ENDA_ACTUALTYPE_COLUMNNAME, RELATION_TABLE_ENDB_ACTUALTYPE_COLUMNNAME);
    private final String columnName;
    private final String columnType;
    private final int columnSize;

    public TableColumnMeta(String columnName, String columnType, int columnSize) {
        if ("VARCHAR".equals(columnType) && columnSize <= 0) {
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
