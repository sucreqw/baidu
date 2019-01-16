package com.sucre.entity;

import com.sucre.factor.Factor;
import com.sucre.utils.MyUtil;

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
    public Baidu(String id, String pass) {
        this.id = id;
        this.pass = pass;
    }

    // 从文件数据里加载数据！
    public Baidu(String inputdata) {
        load(inputdata);
    }
    public void load(String inputdata){
        try {
            String[] temp = inputdata.split("\\|");
            if (temp.length==8) {
                //key.txt
                this.cookie=temp[6];
                this.uid=temp[5];
                this.s=temp[2];
                this.id="";
                this.pass="";
            }else {
                this.cookie = temp[0];
                this.uid = temp[1];
                this.id = temp[2];
                this.pass = temp[3];
                this.s = temp.length >= 5 ? temp[4]:"";
            }
        } catch (Exception e) {
            MyUtil.print("导入weibo数据出错!", Factor.getGui());
        }
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
