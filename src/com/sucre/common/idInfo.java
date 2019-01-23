package com.sucre.common;

import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.utils.MyUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class idInfo {
    private String password = "";
    private String nickfile = "";
    private MutiList list;
    public static idInfo idInfo = new idInfo();

    private idInfo() {
    }

    /**
     * 从文件加载注册配置信息。
     *
     * @param filename
     */
    public void load(String filename) {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(new File(filename)));
            password = (properties.getProperty("password"));
            nickfile = (properties.getProperty("nick"));
        } catch (FileNotFoundException e) {
            System.out.println("id文件未找到：" + e.getMessage());

        } catch (IOException e) {
            System.out.println("导入id文件错误：" + e.getMessage());

        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickfile() {
        return nickfile;
    }

    public void setNickfile(String nickfile) {
        this.nickfile = nickfile;
    }

    public void loadNick() {
        try {
            list = new MutiList();
            list.loadFromFile(nickfile);
        } catch (Exception e) {
            MyUtil.print("昵称加载失败。" , Factor.getGui());
            //System.out.println(e.getMessage());
        }
    }

    /**
     * 随机取一个名字
     *
     * @return
     */
    public String getRandNick() {
         return list.get(Integer.parseInt(MyUtil.getRand(list.getSize()-1,0)));
    }

    /**
     * 返回单例的实例。
     *
     * @return
     */
    public static idInfo getInstance() {
        return idInfo;
    }
}
