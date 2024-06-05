package com.opentext.itom.ucmdb.integration.push.framework.target.vertica;

import com.opentext.itom.ucmdb.integration.push.framework.PushResult;
import com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper.CIMapper;
import com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper.MetaDataMapper;
import com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper.MetaDataPOJO;
import com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper.SchemaMapper;
import com.opentext.itom.ucmdb.integration.push.framework.target.vertica.util.ColumnValueConverter;
import com.opentext.itom.ucmdb.integration.push.repo.model.ModelConverter;
import com.opentext.itom.ucmdb.integration.push.repo.model.TableColumnMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.TableMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.TargetMeta;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIBatch;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIEntity;
import com.opentext.itom.ucmdb.integration.push.repo.model.ci.CIRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class VerticaConnectorImpl implements VerticaConnector{

    public static final String SCHEMA_NAME = "CMDB";
    public static final String METADATA_KEY_TIME = "time";
    public static final String METADATA_KEY_SIZE = "size";
    @Autowired
    private SchemaMapper schemaMapper;

    @Autowired
    private CIMapper ciMapper;

    @Autowired
    private MetaDataMapper metaDataMapper;

    Map<String, TableMeta> tableMetaMap;

    public Map<String, TableMeta> getTableMetaMap() {
        if(tableMetaMap == null){
            tableMetaMap = new HashMap<String,TableMeta>();
        }
        return tableMetaMap;
    }

    @Override
    public boolean testConnection() {
        boolean rlt = false;
        try{
            if(schemaMapper.countSchema(SCHEMA_NAME) != 1){
                schemaMapper.createSchema(SCHEMA_NAME);
            }
            schemaMapper.setDefaultSchema(SCHEMA_NAME);

            rlt = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return rlt;
    }

    @Override
    public TargetMeta loadMetadata() {
        Map<String, Object> sqlResult = metaDataMapper.getAll();
        int size = 0;
        TargetMeta rlt = new TargetMeta();
        ObjectInputStream ois = null;
        ByteArrayInputStream bis = null;
        try{
            for(String key : sqlResult.keySet()){
                MetaDataPOJO value = (MetaDataPOJO)sqlResult.get(key);
                if(METADATA_KEY_TIME.equals(key)){
                    rlt.setLastUpdateTimestamp(ByteBuffer.wrap(value.getValue()).getLong());
                } else if (METADATA_KEY_SIZE.equals(key)) {
                    size = ByteBuffer.wrap(value.getValue()).getInt();
                } else{
                    ois = new ObjectInputStream(bis = new ByteArrayInputStream(value.getValue()));
                    TableMeta tableMeta = (TableMeta) ois.readObject();
                    rlt.getTargetTableMetaMap().put(key, tableMeta);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                ois.close();
                bis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.tableMetaMap = rlt.getTargetTableMetaMap();
        return rlt;
    }

    @Override
    public TargetMeta initTargetSystem(Map<String, TableMeta> tableMetaMap) {
        // init ci table
        TargetMeta rlt = new TargetMeta();
        for(TableMeta tableMeta : tableMetaMap.values()){
            ciMapper.createNewTable(tableMeta.getTableName(), tableMeta.getColumnListForTable());
            rlt.getTargetTableMetaMap().put(tableMeta.getClassName(), tableMeta);
        }
        TableMeta relationMeta = TableMeta.createRelationTableMeta();
        ciMapper.createNewTable(relationMeta.getTableName(), relationMeta.getColumnListForTable());
        rlt.getTargetTableMetaMap().put(relationMeta.getClassName(), relationMeta);

        rlt.setLastUpdateTimestamp(System.currentTimeMillis());

        // init metadata table
        metaDataMapper.dropTableIfExist();
        metaDataMapper.createTable();

        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try{
            Map<String, Object> inputMap = new HashMap<>();
            byte[] value_byte = null;
//            Do not set timestamp because the push is not finished.
//            value_byte = ByteBuffer.allocate(Long.BYTES).putLong(rlt.getLastUpdateTimestamp()).array();
//            inputMap.put("key", METADATA_KEY_TIME);
//            inputMap.put("value", value_byte);
//            if(metaDataMapper.checkExist(METADATA_KEY_TIME)){
//                metaDataMapper.updateMetaData(inputMap);
//            } else{
//                metaDataMapper.insertMetaData(inputMap);
//            }
            inputMap.clear();
            value_byte = ByteBuffer.allocate(Integer.BYTES).putInt(rlt.getTargetTableMetaMap().size()).array();
            inputMap.put("key", METADATA_KEY_SIZE);
            inputMap.put("value", value_byte);
            if(metaDataMapper.checkExist(METADATA_KEY_SIZE)){
                metaDataMapper.updateMetaData(inputMap);
            } else{
                metaDataMapper.insertMetaData(inputMap);
            }
            for(String className : rlt.getTargetTableMetaMap().keySet()){
                TableMeta metadata = rlt.getTargetTableMetaMap().get(className);
                oos = new ObjectOutputStream(bos = new ByteArrayOutputStream());
                oos.writeObject(metadata);
                inputMap.clear();
                inputMap.put("key", className);
                inputMap.put("value", bos.toByteArray());
                if(metaDataMapper.checkExist(className)){
                    metaDataMapper.updateMetaData(inputMap);
                } else{
                    metaDataMapper.insertMetaData(inputMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try{
                oos.close();
                bos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.tableMetaMap = rlt.getTargetTableMetaMap();
        return rlt;
    }

    @Override
    public PushResult batchInsert(CIBatch ciBatch) {
        PushResult rlt = new PushResult(System.currentTimeMillis());

        for(String className : getTableMetaMap().keySet()){
            TableMeta tableMeta = getTableMetaMap().get(className);
            Set<CIEntity> ciEntitySet = ciBatch.getCiTypeMap().getOrDefault(tableMeta.getClassName(), new HashSet<CIEntity>());
            for(CIEntity ciEntity : ciEntitySet){
                String global_id = ciEntity.getGlobalId();
                try{
                    String lastAccessTime = ModelConverter.convertColumnValueByType("DATETIME", ciEntity.getAttributeMap().get(TableMeta.ATTRIBUTE_LAST_ACCESS_TIME));
                    if(ciMapper.ciExist(tableMeta.getTableName(), global_id, lastAccessTime)){
                        continue;
                    }
                    Map<String, String> valueMap = new HashMap<>();
//                valueMap.put("root_actualdeletetime", "2022-04-07");
                    for(String key : ciEntity.getAttributeMap().keySet()){
                        if(ciEntity.getAttributeMap().get(key) != null){
                            TableColumnMeta tableColumnMeta = tableMeta.getColumnMetaByName(key);
                            if(tableColumnMeta == null) continue;
                            valueMap.put(key, ModelConverter.convertColumnValueByType(tableColumnMeta.getColumnType(), ciEntity.getAttributeMap().get(key)));
                        }
                    }
                    ciMapper.insertCI(tableMeta.getTableName(), valueMap);
                    rlt.getIdMapping().put(global_id, global_id);
                }catch (Exception e){
                    e.printStackTrace();
                    rlt.getFailureList().add(global_id);
                    rlt.getUnPushedIdSet().add(global_id);
                }

            }
        }

        // insert relationship
        TableMeta relationTableMeta = getTableMetaMap().get(TableMeta.TABLE_RELATION);
        if(relationTableMeta != null){
            for(String parentId : ciBatch.getParentMap().keySet()){
                for(CIRelation relation : ciBatch.getParentMap().get(parentId)){
                    String global_id = relation.getGlobalId();
                    try{
                        CIEntity parentCI = ciBatch.getCiEntityMap().get(parentId);
                        CIEntity childCI = ciBatch.getCiEntityMap().get(relation.getEnd2Id());
                        if(parentCI == null || childCI == null){
                            continue;
                        }
                        if(ciMapper.relationExist(relationTableMeta.getTableName(), relation.getEnd1Id(), relation.getEnd2Id(), relation.getCiType())){
                            continue;
                        }
                        Map<String, String> valueMap = new HashMap<>();
                        valueMap.put(TableColumnMeta.RELATION_TABLE_ID_COLUMNNAME, relation.getGlobalId());
                        valueMap.put(TableColumnMeta.RELATION_TABLE_TYPE_COLUMNNAME, relation.getCiType());
                        valueMap.put(TableColumnMeta.RELATION_TABLE_ENDA_COLUMNNAME, parentCI.getGlobalId());
                        valueMap.put(TableColumnMeta.RELATION_TABLE_ENDB_COLUMNNAME, childCI.getGlobalId());
                        valueMap.put(TableColumnMeta.RELATION_TABLE_ENDATYPE_COLUMNNAME, parentCI.getCiType());
                        valueMap.put(TableColumnMeta.RELATION_TABLE_ENDBTYPE_COLUMNNAME, childCI.getCiType());
                        valueMap.put(TableColumnMeta.RELATION_TABLE_ENDA_ACTUALTYPE_COLUMNNAME, parentCI.getAccurateType());
                        valueMap.put(TableColumnMeta.RELATION_TABLE_ENDB_ACTUALTYPE_COLUMNNAME, childCI.getAccurateType());
                        valueMap.put(TableColumnMeta.RELATION_TABLE_LASTACCESSTIME_COLUMNNAME, ModelConverter.convertColumnValueByType("DATETIME", String.valueOf(System.currentTimeMillis())));
                        ciMapper.insertCI(relationTableMeta.getTableName(), valueMap);
                        rlt.getIdMapping().put(relation.getGlobalId(), global_id);
                    }catch (Exception e){
                        e.printStackTrace();
                        rlt.getFailureList().add(global_id);
                        rlt.getUnPushedIdSet().add(global_id);
                    }
                }
            }
        }

        rlt.setFinishTime(System.currentTimeMillis());
        return rlt;
    }

    @Override
    public void updateFinishTime(long finishTime) {
        Map<String, Object> inputMap = new HashMap<>();
        byte[] value_byte = ByteBuffer.allocate(Long.BYTES).putLong(finishTime).array();
        inputMap.put("key", METADATA_KEY_TIME);
        inputMap.put("value", value_byte);
        if(metaDataMapper.checkExist(METADATA_KEY_TIME)){
            metaDataMapper.updateMetaData(inputMap);
        } else{
            metaDataMapper.insertMetaData(inputMap);
        }
    }
}
