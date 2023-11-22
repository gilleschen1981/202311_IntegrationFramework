package com.opentext.itom.ucmdb.client.rest.wrapper;

import javax.validation.constraints.NotNull;

public class ClassModelPropertyWrapper {
    @NotNull
    public String name;
    @NotNull
    public String type;
    public String className;
    public Object value;
}
