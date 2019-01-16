package com.sucre.impl;

import com.sucre.dao.BaiduDao;
import com.sucre.dao.CommonDao;
import com.sucre.entity.Baidu;
import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.utils.MyUtil;

import java.util.List;

public class BaiduImpl implements BaiduDao {
   //用来装载账号列表数据
    private MutiList list=new MutiList();

    /**
     * 导入指定文件到id列表，可通用。
     * @param fileName
     */
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
