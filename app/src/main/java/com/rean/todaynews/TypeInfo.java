package com.rean.todaynews;

public class TypeInfo {
    private String typeName;
    private Integer typeId;

    public TypeInfo(String typeName, Integer typeId) {
        this.typeName = typeName;
        this.typeId = typeId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
