package com.sucre.dao;

import com.sucre.entity.CommonEntity;

import java.util.List;

public interface CommonDao {
    //从文件加载数据
    public void loadList(String fileName);

    //根据索引取一条数据
    public <T> CommonEntity<T> get(int index, CommonEntity<T> entity);

    //根据id,pass加载一个类
    public <T> CommonEntity<T> load(String id, String pass, CommonEntity<T> entity);

    //把文件的数据加载到类
    public <T> CommonEntity<T> load(String inputData, CommonEntity<T> entity);

    //取一页的数据
    public <T> List<T> getPage(int page);

    //取指定数量数据
    public <T> List<T> getCounts(int counts, String mission);

    //取数据总数
    public int getsize();

    //添加一条数据
    public <T> CommonEntity<T> add(CommonEntity<T> entity);

    //更新一条数据
    public <T> CommonEntity<T> update(CommonEntity<T> entity);

    //返回对象的数据列表。
    public List getlist();
}


