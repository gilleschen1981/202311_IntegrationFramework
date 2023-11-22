package com.opentext.itom.ucmdb.integration.push.repo.model.ci;

// Relation is also a CIEntity, with globalid
public class CIRelation extends CIEntity{
    public static final String RELATION_ID_SEPARATOR = "_";
    private String end1Id;

    private String end2Id;

    public CIRelation(String ciType, String end1Id, String end2Id) {
        super(ciType + RELATION_ID_SEPARATOR + end1Id + RELATION_ID_SEPARATOR + end2Id, ciType);
        this.end1Id = end1Id;
        this.end2Id = end2Id;
    }

    public String getEnd1Id() {
        return end1Id;
    }

    public String getEnd2Id() {
        return end2Id;
    }
}
