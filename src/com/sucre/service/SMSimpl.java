package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.entity.Vid;

public class SMSimpl implements SMSplatform{
    private static SMSimpl smSimpl=new SMSimpl();
    private int index=0;
    private SMSimpl() {
    }

    public static SMSimpl getInstance(){
        return smSimpl;
    }

    @Override
    public String getPhone() {
        Vid v = new Vid();
        if (index >= Controller.getInstance().getVidImpl().getSize()){index=0;}
        v= Controller.getInstance().getVidImpl().getVid(index,v);

        return v.getVids();
    }

    @Override
    public String getCode() {
        return null;
    }
}
