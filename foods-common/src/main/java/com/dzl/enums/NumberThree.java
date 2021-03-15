package com.dzl.enums;


/*
* 性别枚举
* */
public enum NumberThree {
    one(1,"一级大分类"),
    two(2,"二级大分类"),
    three(3,"三级大分类");

    public final Integer type;
    public final String value;

    NumberThree(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
