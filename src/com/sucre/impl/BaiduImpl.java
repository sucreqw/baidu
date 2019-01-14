package com.sucre.impl;

import com.sucre.dao.BaiduDao;
import com.sucre.entity.Baidu;
import com.sucre.listUtil.MutiList;

import java.util.List;

public class BaiduImpl implements BaiduDao {
    //public static weiboDao dao=new WeiboImpl();
    private MutiList list=new MutiList();
    @Override
    public void loadList(String fileName) {

    }

    @Override
    public Baidu get(int index, Baidu weibo) {
        return null;
    }

    @Override
    public Baidu load(String id, String pass, Baidu weibo) {
        return null;
    }

    @Override
    public Baidu load(String inputData, Baidu weibo) {
        return null;
    }

    @Override
    public List<Baidu> getPage(int page) {
        return null;
    }

    @Override
    public List<Baidu> getCounts(int counts, String mission) {
        return null;
    }

    @Override
    public int getsize() {
        return 0;
    }

    @Override
    public void add(Baidu weibo) {

    }

    @Override
    public void update(Baidu weibo) {

    }
    public List getlist() {
        return list;
    }
}
