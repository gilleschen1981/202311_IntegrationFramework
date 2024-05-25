package com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SchemaMapper {
    int countSchema(@Param("schemaName") String schemaName);

    void createSchema(@Param("schemaName") String schemaName);

    void setDefaultSchema(@Param("schemaName") String schemaName);
}
