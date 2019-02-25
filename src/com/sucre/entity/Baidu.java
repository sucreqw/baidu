package com.sucre.entity;

/**
 * pojo类
 */
public class Baidu extends CommonEntity{
    public Baidu() {
    }

    public Baidu(String id, String pass) {
        super(id, pass);
    }

    public Baidu(String inputdata) {
        super(inputdata);
    }

    @Override
    public String toString() {
        return "Baidu{} " + super.toString();
    }
}