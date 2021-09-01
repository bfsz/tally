package com.luna.tally.db;

/*
 * 收入 支出 具体类型类
 * */
public class TypeBean {
    int id;
    String typeName; // 类型名称
    int imageId; // 未选中图片id
    int simageId; // 选中图片id
    int kind; // 收入-1 支出 -0

    public TypeBean() {
    }

    public TypeBean(int id, String typeName, int imageId, int simageId, int kind) {
        this.id = id;
        this.typeName = typeName;
        this.imageId = imageId;
        this.simageId = simageId;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSimageId() {
        return simageId;
    }

    public void setSimageId(int simageId) {
        this.simageId = simageId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
