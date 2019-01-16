package com.sucre.service;

import com.sucre.factor.Factor;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.MyUtil;

public class BaiduRegister extends Thread4Net {
    private String mission;
    public BaiduRegister(int l, int u, boolean isCircle,String mission) {
        super(l, u, isCircle);
        this.mission=mission;
    }

    /**
     * 具体的业务逻辑实现。
     * @param index 备用参数，以防止有业务需求
     * @return
     */
    @Override
    public int doWork(int index) {
        MyUtil.print("it's a test:" + Thread.currentThread().getName() +"<==>"+ index, Factor.getGui());
        return 1;
    }

    private byte[] getdata(String cookies, String uid) {
        StringBuilder data = new StringBuilder(900);
        String temp = "";
        data.append("GET /v2/?regphonesend&token=2723184baf19f302f42ae2b07ea62f04&tpl=mn&subpro=&apiver=v3&tt=1547543270267&is_voice_sms=0&phone=13602930017&countrycode=&gid=3648783-08E3-41CF-BE3C-2E7319E03712&isexchangeable=1&dv=tk0.68892140016435891547534972575%40kkv0NI9woZBmfDpDyvD3u9t~apD~hSB~aS8t9EN3yQJFDZ9CoY9vYP46YY2urh7386pD~St~h7Dulwt~a~NPH-7IuiG6YPBwqzSkNP2kqZHsu4r~nh7HaSDu9ptw9StzHwGtn4LF~E2kLx4knl9wLZBmfDpDyvD3u9t~apD~hSB~aS8t9EN3yQJFDZ9CLw9tYz46YY2urh7386pD~St~h7Dulwt~a~NPH-7IuiG6YP4ksYSq__tv0oI97p-2ksz9-Ye9wox2urh7386pD~St~h7Dulwt~aIJzniSkD-BmY-9ksZB7qYBkUZHsu4r~nh7HaSDu9ptw9Stz9~GP8ENzr4LF~EHznQNvhENcY~47oZBCBw2ksfB7p-2urh7386pD~St~h7Dulwt~aYM5agGtY~47UZBCUe2kswBws-2urh7386pD~St~h7Dulwt~aYLt9w8Pa-GvY~97UZBwpY2ksx9CpP2urh7386pD~St~h7Dulwt~aPGtnTGcEkJPrESq__IffMwfFlnLANhFvhB-Y-2kNfuvj85ZY2CLe4k3-B7pYBksP9kB~4k3f97pz97Bx47N-97N~xvoB7sf2kLw2ksz9koZ9Cp-2ksyBCqZB7qeBmYf47oY2ksz97NZ9wLz&traceid=&callback=bd__cbs__f5onbz HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg&tt=1547534970575&overseas=undefined&gid=2534C71-1E7D-4D90-89C9-2407876AC2BC&tpl=mn&u=https%3A%2F%2Fwww.baidu.com%2F\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: \r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }


}
