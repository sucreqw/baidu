package com.sucre.entity;

/**
 * pojo类
 */
public class Baidu {
    private String id;
    private String pass;
    private String uid;
    private String cookie;
    private String s;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Baidu{" +
                "id='" + id + '\'' +
                ", pass='" + pass + '\'' +
                ", uid='" + uid + '\'' +
                ", cookie='" + cookie + '\'' +
                ", s='" + s + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
