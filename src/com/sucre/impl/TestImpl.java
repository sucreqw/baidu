package com.sucre.impl;

import com.sucre.dao.CommonDao;
import com.sucre.entity.CommonEntity;
import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.utils.MyUtil;

import java.util.List;

public class TestImpl implements CommonDao {
    //用来装载账号列表数据
    private MutiList list = new MutiList();

    @Override
    public void loadList(String fileName) {
        try {
            // 加载文件
            list.loadFromFile(fileName);
            MyUtil.print("导入成功<==>" + String.valueOf(list.getSize()), Factor.getGui());
        } catch (Exception e) {
            MyUtil.print("导入错误：" + e.getMessage(), Factor.getGui());
        }
    }

    @Override
    public <T> CommonEntity<T> get(int index, CommonEntity<T> entity) {

        //String[] temp =list.get(index).split("[^@.-_\\w]");
        String[] temp =list.get(index).split("\\|");
        switch (temp.length) {
            case 2:
                return load(temp[0],temp[1],entity);
            //break;

            default:
                return load(list.get(index),entity);
            //break;
        }
        //return null;
    }

    @Override
    public <T> CommonEntity<T>  load(String id, String pass, CommonEntity<T> entity) {
        entity.setId(id);
        entity.setPass(pass);
        return entity;
    }

    @Override
    public <T> CommonEntity<T> load(String inputData, CommonEntity<T> entity) {
        entity.load(inputData);
        return entity;
    }

    @Override
    public <T> List<T> getPage(int page) {
        return null;
    }

    @Override
    public <T> List<T> getCounts(int counts, String mission) {
        return null;
    }

    @Override
    public int getsize() {
        return 0;
    }

    @Override
    public <T> CommonEntity<T> add(CommonEntity<T> entity) {
        return null;
    }

    @Override
    public <T> CommonEntity<T> update(CommonEntity<T> entity) {
        return null;
    }
}
