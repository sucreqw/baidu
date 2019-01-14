package com.sucre.controller;

import com.sucre.dao.vidDao;
import com.sucre.entity.Baidu;
import com.sucre.factor.Factor;
import com.sucre.impl.BaiduImpl;
import com.sucre.impl.VidImpl;
import com.sucre.listUtil.MutiList;
import com.sucre.utils.*;

import javax.swing.*;

public class Controller {

	public static Controller controller = new Controller();
	private BaiduImpl BaiduImplId;
	private BaiduImpl BaiduImplCookie;
	private VidImpl vidImpl;
	private boolean stop = false;

	private Controller() {
	}

	/**
	 * 导入ip文件配置。
	 *//*
	public void load() {
		try {
			// 导入换ip配置
			Info info = accounts.getInstance();
			MyUtil.loadADSL("adsl.properties", accounts.getInstance());
			MyUtil.print(info.getADSL() + "<>" + info.getADSLname() + "<>" + info.getADSLpass(), Factor.getGui());
			// 加载js
			JsUtil.loadJs("js.js");
			JdbcConnector.Load("jdbc.properties");
		} catch (Exception e) {
			MyUtil.print("导入文件出错：" + e.getMessage(), Factor.getGui());
		}
	}*/

	/**
	 * 加载指定文件到账号列表
	 * 
	 * @param fileName
	 */
	public void loadWeiboId(String fileName, JTable table, String mission) {
		BaiduImplId = new BaiduImpl();
		BaiduImplId.loadList(fileName);
		// BaiduImplId.getCounts(Integer.parseInt(fileName), mission);
		GuiUtil.loadTableVid(table, (MutiList) BaiduImplId.getlist());
	}

	/**
	 * 加载指定文件到cookie列表
	 * 
	 * @param fileName
	 */
	public void loadWeiboCookie(String fileName, JTable table) {
		BaiduImplCookie = new BaiduImpl();
		BaiduImplCookie.loadList(fileName);
		GuiUtil.loadTableVid(table, (MutiList) BaiduImplCookie.getlist());
	}

	/**
	 * 加载指定文件到vid列表
	 * 
	 * @param fileName
	 * @return
	 */
	public void loadVid(String fileName, JTable table) {
		vidImpl = new VidImpl();
		vidImpl.loadVid(fileName);
		GuiUtil.loadTableVid(table, (MutiList) vidImpl.getlist());

	}

	/**
	 * 添加一个vid
	 * 
	 * @param data
	 * @param table
	 */
	public void addVid(String data, JTable table) {
		if (null == vidImpl) {
			vidImpl = new VidImpl();
		}
		vidImpl.add(data);
		GuiUtil.loadTableVid(table, (MutiList) vidImpl.getlist());
	}

	/**
	 * 取vid的列表对象
	 * 
	 * @return
	 */
	public vidDao getVidImpl() {
		return vidImpl;
	}

	/**
	 * 加载list到列表
	 * 
	 * @param table
	 * @param list
	 */
	public void loadTable(JTable table, MutiList list) {
		GuiUtil.loadTable(table, list);
	}

	public void addCookie(Baidu baidu) {
		if (BaiduImplCookie == null) {
			BaiduImplCookie = new BaiduImpl();
		}
		BaiduImplCookie.add(baidu);
	}

	/**
	 * 	 * 调用批量登录功能
	 * 	 *
	 * 	 * @param thread   线程数量
	 * @param limit    账号总数
	 * @param isCircle 是否循环
	 */
	public void login(int thread, boolean isCircle, int start) {

	}




	/**
	 * 投票类
	 * 
	 * @param start    起始位置
	 * @param thread   线程数
	 * @param isCircle 是否循环
	 * @param mission  任务名称
	 */
	public void vote(int start, int thread, boolean isCircle, String mission) {
		int limit = BaiduImplCookie == null ? 0 : BaiduImplCookie.getsize();
		if (limit == 0 || getVidImpl() == null) {
			MyUtil.print("Cookie或者vid未导入！", Factor.getGui());
			return;
		}
		//SinaVote vote = new SinaVote(start, limit - 1, isCircle, BaiduImplCookie, mission);
		for (int i = 1; i <= thread; i++) {

			//Thread t = new Thread(vote);
			if (i == 1) {
				//t.setName("ip");
			}
			//t.start();
		}
	}

	/**
	 * 取用户定义的多少账号换一次ip
	 * 
	 * @return
	 */
	public int changeIPcount() {
		return Factor.getGuiFrame().getIPcount();
	}

	/**
	 * 定时换ip,秒数
	 */
	public void changeipM(String mission, int s) {
		Thread thread = new Thread() {
			int i = 0;

			public void run() {
				while (true) {
					i++;
                    if(i==s) {
                        MyUtil.changIp(); i=0;}
                    MyUtil.sleeps(1000);
				}
			};
		};
		thread.start();
	}



	/**
	 * 拿到controller的对象实例。
	 * 
	 * @return
	 */
	public static Controller getInstance() {
		return controller;
	}

	/**
	 * 刷新指定数据列表
	 * 
	 * @param cookietable
	 */
	public void refresh(JTable table) {
		if (BaiduImplCookie == null) {
			return;
		}
		GuiUtil.loadTableVid(table, (MutiList) BaiduImplCookie.getlist());
	}

	/**
	 * 刷新指定数据列表(线程回调)
	 * 
	 * @param cookietable
	 */
	public void refresh() {
		Factor.getGuiFrame().refresh();
	}

	/**
	 * 继续任务
	 */
	public void resume() {
		this.stop = false;
	}

	/**
	 * 暂停任务
	 */
	public void stop() {
		this.stop = true;
	}

	/**
	 * 判断是否为暂停状态
	 * 
	 * @return 当前状态。
	 */
	public boolean isStop() {
		return this.stop;
	}

	public int getEndCount() {
		return Factor.getGuiFrame().getCounts();
	}
}
