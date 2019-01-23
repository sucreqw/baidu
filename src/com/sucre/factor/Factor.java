package com.sucre.factor;


import com.sucre.common.idInfo;
import com.sucre.gui.Gui;
import com.sucre.service.SMSimpl;
import com.sucre.service.SMSplatform;
import com.sucre.utils.Printer;

public class Factor {
	//返回实现了printer的窗体类。
	public static Printer getGui() {
		return Gui.getInstance();
	}
    //取窗体的对象，操作一些控件和方法
	public static Gui getGuiFrame(){
    	return Gui.getInstance();
    }

    //返回实现了sms平台的类。
	public static SMSplatform getSms(){
		return SMSimpl.getInstance();
	}

	//返回id文件配置实例
	public static idInfo getidInfo(){
		return idInfo.getInstance();
	}
}
