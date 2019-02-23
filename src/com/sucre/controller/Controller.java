package com.sucre.controller;

import com.sucre.dao.vidDao;
import com.sucre.entity.CommonEntity;
import com.sucre.factor.Factor;
import com.sucre.impl.CommonImpl;
import com.sucre.impl.VidImpl;
import com.sucre.listUtil.MutiList;
import com.sucre.myThread.Thread4Net;
import com.sucre.service.BaiduLoginMac;
import com.sucre.service.BaiduRegister;
import com.sucre.utils.*;

import javax.swing.*;

public class Controller {

    public static Controller controller = new Controller();
    private CommonImpl ImplId;
    private CommonImpl ImplCookie;
    private VidImpl vidImpl;
    private boolean stop = false;

    private Controller() {
    }

    /**
     * 导入ip文件配置。
     */
	public void load() {
        try {
            // 导入换ip配置
            Info info = accounts.getInstance();
            MyUtil.loadADSL("adsl.properties", accounts.getInstance());
            MyUtil.print(info.getADSL() + "<>" + info.getADSLname() + "<>" + info.getADSLpass(), Factor.getGui());
            // 加载js
            //JsUtil.loadJs("js.js");
            //JdbcConnector.Load("jdbc.properties");

            //加载注册账号配置信息。
            Factor.getidInfo().load("id.properties");
            Factor.getidInfo().loadNick();


        } catch (Exception e) {
            MyUtil.print("导入文件出错：" + e.getMessage(), Factor.getGui());
        }
    }

    /**
     * 加载指定文件到账号列表
     *
     * @param fileName
     */
    public void loadId(String fileName, JTable table, String mission) {
        ImplId = new CommonImpl();
        ImplId.loadList(fileName);
        // BaiduImplId.getCounts(Integer.parseInt(fileName), mission);
        GuiUtil.loadTableVid(table, (MutiList) ImplId.getlist());
    }

    /**
     * 加载指定文件到cookie列表
     *
     * @param fileName
     */
    public void loadCookie(String fileName, JTable table) {
        ImplCookie = new CommonImpl();
        ImplCookie.loadList(fileName);
        GuiUtil.loadTableVid(table, (MutiList) ImplCookie.getlist());
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

    /**
     * 给service层调用，登录出来的cookie要显示出来。
     */
    public void addCookie(CommonEntity entity) {
        if (ImplCookie == null) {
            ImplCookie = new CommonImpl();
        }
        ImplCookie.add(entity);
    }

    /**
     * * 调用批量登录功能
     * *
     * * @param thread   线程数量
     *
     * @param isCircle 是否循环
     */
    public void login(int thread, boolean isCircle, int start) {

    }


    /**
     * 开始任务，全部集合到一个方法里面。
     */
    public void doMission(int start, int thread, boolean isCircle, String mission) {
        int limit = ImplCookie == null ? 0 : ImplCookie.getsize()-1;
        switch (mission) {

            case "注册":
                //加载js 算法
                JsUtil.loadJs("baidu.js");
                BaiduRegister baiduRegister = new BaiduRegister(start, limit, isCircle, mission);
                startThread(baiduRegister,thread);
                break;
            case "登录" :
                JsUtil.loadJs("baidu.js");
                BaiduLoginMac baiduLogin=new BaiduLoginMac(start,limit,isCircle);
                startThread(baiduLogin,thread);
                break;
            default:


        }

    }

    /**
     * 启动线程，开始任务。
     * @param net
     * @param thread
     */
    private void startThread(Thread4Net net, int thread){
        for (int i = 1; i <= thread; i++) {

            Thread t = new Thread(net);
            if (i == 1) {
                t.setName("ip");
            }
            t.start();
        }

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
        int limit = ImplCookie == null ? 0 : ImplCookie.getsize();
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
                    if (i == s) {
                        MyUtil.changIp();
                        i = 0;
                    }
                    MyUtil.sleeps(1000);
                }
            }

            ;
        };
        thread.start();
    }

    /**
     * 把验证码显示到窗体！
     * @param pic 图片的byte
     */
    public void showPic(byte[] pic){
        Factor.getGuiFrame().showPic(pic);
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
     */
    public void refresh(JTable table) {
        if (ImplCookie == null) {
            return;
        }
        GuiUtil.loadTableVid(table, (MutiList) ImplCookie.getlist());
    }

    /**
     * 刷新指定数据列表(线程回调)
     *
     * @param
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

    /**
     * 取结束位置
     *
     * @return
     */
    public int getEndCount() {
        return Factor.getGuiFrame().getCounts();
    }
}
