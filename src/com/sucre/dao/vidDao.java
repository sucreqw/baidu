package com.sucre.dao;

import com.sucre.entity.Vid;

/**
 * vid dao层接口
 * @author 90650
 *
 */
public interface vidDao {
	public Vid getVid(int index, Vid v);
	public void add(String vid);
	public void remove(int index);
	public void loadVid(String fileName);
	public int getSize(); 
}
