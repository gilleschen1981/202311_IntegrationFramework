package com.opentext.itom.ucmdb.client.rest.wrapper;

import java.util.List;

public class ClassIdentificationWrapper {
    private ClassIdentificationTypeWrapper identificationType;
    private ClassIdentificationTypeWrapper inheritedIdentificationType;
    private String ruleXml;
    private List<String> keyAttributes;

    public ClassIdentificationTypeWrapper getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(ClassIdentificationTypeWrapper identificationType) {
        this.identificationType = identificationType;
    }

    public String getRuleXml() {
        return ruleXml;
    }

    public void setRuleXml(String ruleXml) {
        this.ruleXml = ruleXml;
    }

    public List<String> getKeyAttributes() {
        return keyAttributes;
    }

    public void setKeyAttributes(List<String> keyAttributes) {
        this.keyAttributes = keyAttributes;
    }

    public ClassIdentificationTypeWrapper getInheritedIdentificationType() {
        return inheritedIdentificationType;
    }

    public void setInheritedIdentificationType(ClassIdentificationTypeWrapper inheritedIdentificationType) {
        this.inheritedIdentificationType = inheritedIdentificationType;
    }
}
