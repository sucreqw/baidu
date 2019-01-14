package com.sucre.impl;

import com.sucre.dao.vidDao;
import com.sucre.entity.Vid;
import com.sucre.factor.Factor;
import com.sucre.listUtil.MutiList;
import com.sucre.utils.MyUtil;

import java.util.List;

public class VidImpl implements vidDao{
	MutiList list=new MutiList();
	@Override
	public Vid getVid(int index, Vid v) {
		//Vid vid=new Vid(list.get(index));
		v.setVids(list.get(index));
		return v;
	}

	@Override
	public void add(String vid) {
		list.add(vid);
	}
	@Override
	public void remove(int index) {
		list.remove(index);
	}
	public void loadVid(String fileName) {
		try {
			// 加载文件
			list.loadFromFile(fileName);
			MyUtil.print("导入成功<==>" + String.valueOf(list.getSize()), Factor.getGui());
		} catch (Exception e) {
			MyUtil.print("导入错误：" + e.getMessage(), Factor.getGui());
		}
		
	}
	public List getlist() {
		return list;
	}

	@Override
	public int getSize() {
		return list.getSize();
	}

}
