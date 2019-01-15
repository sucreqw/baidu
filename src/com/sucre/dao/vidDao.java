package com.sucre.dao;

import com.sucre.entity.Vid;

/**
 * vid dao层接口
 *
 * @author 90650
 */
public interface vidDao {
    //取vid
    public Vid getVid(int index, Vid v);
    //加一个vid
    public void add(String vid);
    //删除一个vid
    public void remove(int index);
    //从文件加载vid
    public void loadVid(String fileName);
    //返回vid的数量
    public int getSize();
}
