package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Configuration Item (CI)
 */
public class DataInConfigurationItem extends DataInElement {

    public DataInConfigurationItem(String ucmdbId, String type) {
        super(ucmdbId, type);
    }
}
