package com.opentext.itom.ucmdb.integration.push.framework.target.ucmdb.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Datain relations
 */
public class DataInRelation extends DataInElement {

    @JsonProperty("end1Id")
    public String end1Id;

    @JsonProperty("end2Id")
    public String end2Id;

    public DataInRelation(String ucmdbId, String type, String end1Id, String end2Id) {
        super(ucmdbId, type);
        this.end1Id = end1Id;
        this.end2Id = end2Id;
    }

    public String getEnd1Id() {
        return end1Id;
    }

    public String getEnd2Id() {
        return end2Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataInRelation)) return false;
        DataInRelation that = (DataInRelation) o;
        return Objects.equals(end1Id, that.end1Id) &&
                Objects.equals(end2Id, that.end2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(end1Id, end2Id);
    }
}
