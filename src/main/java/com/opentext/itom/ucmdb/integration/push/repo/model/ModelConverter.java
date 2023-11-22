package com.opentext.itom.ucmdb.integration.push.repo.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ModelConverter {
    private static final Logger log = LoggerFactory.getLogger(ModelConverter.class);
    public static TableMeta convertClassType2Table(ClassTypeMeta meta) {
        TableMeta rlt = null;
        if(meta.isRelation()){
            rlt = new TableMeta(TableMeta.generateRelationTableName(meta.getClassName(), meta.getEnd1Class(), meta.getEnd2Class()));
            TableColumnMeta column  = new TableColumnMeta(TableColumnMeta.RELATION_TABLE_ENDA_COLUMNNAME, "VARCHAR", 20);
            rlt.getColumnList().add(column);
            column  = new TableColumnMeta(TableColumnMeta.RELATION_TABLE_ENDB_COLUMNNAME, "VARCHAR", 20);
            rlt.getColumnList().add(column);
        } else{
            rlt = new TableMeta(meta.getClassName());
        }
        for(AttributeMeta attr : meta.getAttributeMetaList()){
            if(attr.getAttrType() == null){
                log.error("[TABLECONVERT]Null Attribute Type detected in class: " + meta.getClassName());
                continue;
            }
            TableColumnMeta column = new TableColumnMeta(attr.getAttrName(), convertAttrType2ColumnType(attr.getAttrType()), attr.getAttrSize());
            rlt.getColumnList().add(column);
        }
        return rlt;
    }

    private static String convertAttrType2ColumnType(AttributeTypeEnum attrType) {
        String rlt = null;
        switch (attrType){
            case ATTR_TYPE_STRING -> rlt = "VARCHAR";
            case ATTR_TYPE_INTEGER -> rlt = "INTEGER";
            case ATTR_TYPE_BOOLEAN -> rlt = "BOOLEAN";
            case ATTR_TYPE_DATE -> rlt = "DATE";
            case ATTR_TYPE_LIST -> rlt = "VARCHAR";
            case ATTR_TYPE_ENUM -> rlt = "VARCHAR";
            case ATTR_TYPE_STRINGLIST -> rlt = "VARCHAR";
            default -> {rlt = "other"; log.error("[TABLECONVER]Unrecognized attr type: " + attrType.getText());}
        }
        return rlt;
    }

    public static String convertColumnMeta2TableType(TableColumnMeta columnMeta) {
        String rlt = columnMeta.getColumnName() + "\t";
        switch (columnMeta.getColumnType()){
            case "VARCHAR"-> rlt += columnMeta.getColumnType() + "(" + String.valueOf(columnMeta.getColumnSize()) + ")";
            case "INTEGER", "BOOLEAN", "DATE" -> rlt += columnMeta.getColumnType();
            default -> {rlt = ""; log.error("[CONVERT]Unrecognized table column type: " + columnMeta.getColumnType());}
        }
        return rlt;
    }

    public static Map<String, TableMeta> convertClassMetaMap2TableMetaMap(Map<String, ClassTypeMeta> classTypeMetaMap) {
        if(classTypeMetaMap == null){
            return null;
        }
        Map<String, TableMeta> rlt = new HashMap<String, TableMeta>();
        for(String key : classTypeMetaMap.keySet()){
            ClassTypeMeta classTypeMeta = classTypeMetaMap.get(key);
            TableMeta tableMeta = convertClassType2Table(classTypeMeta);
            rlt.put(tableMeta.getTableName(), tableMeta);
        }
        return rlt;
    }
}
