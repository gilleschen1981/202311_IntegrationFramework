package com.opentext.itom.ucmdb.client.rest.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassModelClassWrapper {

    @Size(min = 1, max = 30)
    @Pattern(regexp = "[_a-zA-Z]{1}[_a-zA-Z\\d]*")
    private String name;
    @Size(min = 1, max = 50)
    private String displayName;
    private String superClass;
    @Size(max = 4000)
    private String description;
    private List<ClassModelAttributeWrapper> allAttributes;
    @Size(max = 1000)
    private List<ClassModelAttributeWrapper> classAttributes;
    private String iconName;
    private List<ClassChildInfoWrapper> children;
    private List<String> parents;
    private Set<String> qualifiers;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String classType;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 200)
    private List<ClassModelQualifierWrapper> classQualifiers;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 4000)
    private String defaultLabel;
    private Map<String, Set<String>> validLinks;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClassIdentificationWrapper identification;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TripletWrapper> triplets;
    private boolean editable;
    private ClassScopeWrapper scope;
    private boolean deletable;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AffectedResourceWrapper> affectedResources;
    private boolean isCreatedByFactory;
    private boolean isModifiedByUser;

    public Map<String, Set<String>> getValidLinks() {
        return validLinks;
    }

    public void setValidLinks(Map<String, Set<String>> validLinks) {
        this.validLinks = validLinks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }

    public void setAllAttributes(List<ClassModelAttributeWrapper> allAttributes) {
        this.allAttributes = allAttributes;
    }

    public void setClassAttributes(List<ClassModelAttributeWrapper> classAttributes) {
        this.classAttributes = classAttributes;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public List<ClassModelAttributeWrapper> getAllAttributes() {
        return allAttributes;
    }

    public List<ClassModelAttributeWrapper> getClassAttributes() {
        return classAttributes;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSuperClass() {
        return superClass;
    }

    public String getIconName() {
        return iconName;
    }

    public List<ClassChildInfoWrapper> getChildren() {
        return children;
    }

    public void setChildren(List<ClassChildInfoWrapper> children) {
        this.children = children;
    }

    public List<String> getParents() {
        return parents;
    }

    public void setParents(List<String> parents) {
        this.parents = parents;
    }

    public Set<String> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(Set<String> qualifiers) {
        this.qualifiers = qualifiers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ClassModelQualifierWrapper> getClassQualifiers() {
        return classQualifiers;
    }

    public void setClassQualifiers(List<ClassModelQualifierWrapper> classQualifiers) {
        this.classQualifiers = classQualifiers;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getDefaultLabel() {
        return defaultLabel;
    }

    public void setDefaultLabel(String defaultLabel) {
        this.defaultLabel = defaultLabel;
    }

    public ClassIdentificationWrapper getIdentification() {
        return identification;
    }

    public void setIdentification(ClassIdentificationWrapper identification) {
        this.identification = identification;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public ClassScopeWrapper getScope() {
        return scope;
    }

    public void setScope(ClassScopeWrapper scope) {
        this.scope = scope;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public List<TripletWrapper> getTriplets() {
        return triplets;
    }

    public void setTriplets(List<TripletWrapper> triplets) {
        this.triplets = triplets;
    }

    public List<AffectedResourceWrapper> getAffectedResources() {
        return affectedResources;
    }

    public void setAffectedResources(List<AffectedResourceWrapper> affectedResources) {
        this.affectedResources = affectedResources;
    }

    public boolean isModifiedByUser() {
        return isModifiedByUser;
    }

    public void setModifiedByUser(boolean modifiedByUser) {
        isModifiedByUser = modifiedByUser;
    }

    public boolean isCreatedByFactory() {
        return isCreatedByFactory;
    }

    public void setCreatedByFactory(boolean createdByFactory) {
        isCreatedByFactory = createdByFactory;
    }
}
