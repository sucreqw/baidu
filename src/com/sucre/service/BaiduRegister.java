package com.sucre.service;

import com.sucre.myThread.Thread4Net;

public class BaiduRegister extends Thread4Net {

    public BaiduRegister(int l, int u, boolean isCircle) {
        super(l, u, isCircle);
    }

    /**
     * 具体的业务逻辑实现。
     * @param index
     * @return
     */
    @Override
    public int doWork(int index) {

        return 0;
    }

    
}
