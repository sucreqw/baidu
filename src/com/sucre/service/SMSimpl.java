package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.entity.Vid;
import com.sucre.factor.Factor;
import com.sucre.utils.MyUtil;

public class SMSimpl implements SMSplatform{
    private static SMSimpl smSimpl=new SMSimpl();
    private int index=0;
    private SMSimpl() {
    }

    public static SMSimpl getInstance(){
        return smSimpl;
    }

    @Override
    /**
     * 返回要接收短信的手机号
     */
    public String getPhone() {
        Vid v = new Vid();
        if (index >= Controller.getInstance().getVidImpl().getSize()){index=0;}
        v= Controller.getInstance().getVidImpl().getVid(index,v);
        index++;
        return v.getVids();
    }

    @Override
    /**
     * 返回接收到的短信内容。
     */
    public String getCode() {
       String code="";
       while (true){
           if(Factor.getGuiFrame().getStatus()==2){
               code=Factor.getGuiFrame().filename.getText();
               break;
           }else {
               MyUtil.sleeps(100);
           }
       }
       return code;
    }
}
