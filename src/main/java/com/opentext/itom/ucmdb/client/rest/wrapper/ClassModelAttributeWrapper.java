package com.opentext.itom.ucmdb.client.rest.wrapper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

public class ClassModelAttributeWrapper {

    @Size(min = 2, max = 200)
    @Pattern(regexp = "[_a-zA-Z\\d]+")
    public String name;
    public String displayName;
    @NotNull
    public String type;
    public List<ClassModelQualifierWrapper> qualifiers;
    public String typeInfo;
    public boolean isDeprecated;
    public boolean editable;
    public boolean isTrackedForHistory;
    @SuppressWarnings("squid:ClassVariableVisibilityCheck")
    public Integer valueSize;
    public String description;
    public boolean isCreatedByFactory;
    public String defaultValue;
    // This is used for setting default value don't use defaultValue.
    public Object resolvedDefaultValue;
    public boolean isPartiallyOverride;
    public boolean isOverride;
    // Indicate this attribute was modified or not.
    public boolean isModifiedByUser;
    public boolean isExternal;
    public boolean isClassAttribute;
    // Indicate this attribute should be modified or not.
    // Right now backend server has issue to identify if attribute is changed.
    // This is temp attribute to workaround fix backend server issue.
    public boolean isChanged;
}
