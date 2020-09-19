package com.source.backend.model;

public enum Type {
    TRASHBIN("trashbin"),
    SHOP("shop"),
    EVENT("event");

    private String type;
    Type(String type) {
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
}
