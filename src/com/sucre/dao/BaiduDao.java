package com.sucre.dao;

import com.sucre.entity.Baidu;

import java.util.List;

public interface BaiduDao {

    //从文件加载数据
    public void loadList(String fileName);
    //根据索引取一条数据
    public Baidu get(int index, Baidu  weibo);
    //根据id,pass加载一个类
    public Baidu  load(String id,String pass,Baidu  weibo);
    //把文件的数据加载到类
    public Baidu  load(String inputData,Baidu  weibo);
    //取一页的数据
    public List<Baidu > getPage(int page);
    //取指定数量数据
    public List<Baidu > getCounts(int counts,String mission);
    //取数据总数
    public int getsize();
    //添加一条数据
    public void add(Baidu  weibo);
    //更新一条数据
    public void update(Baidu  weibo);
}
