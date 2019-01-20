package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.MyUtil;

import java.net.URLEncoder;

public class BaiduRegister extends Thread4Net {
    private String mission;

    public BaiduRegister(int l, int u, boolean isCircle, String mission) {
        super(l, u, isCircle);
        this.mission = mission;
    }

    /**
     * 具体的业务逻辑实现。
     *
     * @param index 备用参数，以防止有业务需求
     * @return
     */
    @Override
    public int doWork(int index) {
        Nets net = new Nets();
        String ret;
        switch (mission) {

            case "注册":

                //第一步先取到图片的token
                ret = net.goPost("passport.baidu.com", 443, getPicHash());
                if (!MyUtil.isEmpty(ret)) {
                    //返回的verifyStr再到下一步取真正的图片
                    String verifyStr = MyUtil.midWord("verifyStr\" : \"", "\",", ret);
                    String verifySign = MyUtil.midWord("verifySign\" : \"", "\",", ret);
                    MyUtil.print("取到图片token", Factor.getGui());
                    byte[] rets = net.goPostByte("passport.baidu.com", 443, getPic(verifyStr));

                    if (rets != null) {
                        MyUtil.print("取到图片流,等待用户输入。", Factor.getGui());
                        //MyUtil.outPutData("pic.png", rets);
                        Controller.getInstance().showPic(rets);
                        String code="";
                        int status;
                        boolean RUN=true;
                        //线程阻塞等待用户输入为止。
                        while(RUN) {
                            status = Factor.getGuiFrame().getStatus();
                            switch (status) {
                                case 2:
                                    code = Factor.getGuiFrame().filename.getText();
                                    RUN=false;
                                    break;
                                case 1:

                                    RUN=false;
                                    break;
                                default:
                                    MyUtil.sleeps(100);
                                    break;
                            }
                        }
                        //用户输入完毕
                        if(!MyUtil.isEmpty(code)) {
                            String phone = Factor.getSms().getPhone();
                            ret = net.goPost("passport.baidu.com", 443, sendSMS(phone, verifySign, URLEncoder.encode(code), verifyStr));
                            System.out.println("code:" + code);
                        }
                    }
                }
                break;
        }
        return 1;
    }


    /**
     * 取验证码的hash,token
     *
     * @return
     */
    private byte[] getPicHash() {

        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        data.append("GET /v2/?regsmscodestr&token=2723184baf19f302f42ae2b07ea62f04&tpl=mn&apiver=v3&tt=" + MyUtil.getTime() + "&traceid=&callback=bd__cbs__pe5erq HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg&tt=1547777473167&overseas=undefined&gid=E93D98B-635A-4A72-BB6A-040B7E662075&tpl=mn&u=https%3A%2F%2Fwww.baidu.com%2F\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: \r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    /**
     * 根据上一层取到的token取验证码图片。
     *
     * @param token
     * @return
     */
    private byte[] getPic(String token) {
        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        data.append("GET /cgi-bin/genimage?" + token + " HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: image/webp,image/apng,image/*,*/*;q=0.8\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg&tt=1547777473167&overseas=undefined&gid=E93D98B-635A-4A72-BB6A-040B7E662075&tpl=mn&u=https%3A%2F%2Fwww.baidu.com%2F\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: \r\n");
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    private byte[] sendSMS(String phone, String verifySign, String code, String verifyStr) {
        StringBuilder data = new StringBuilder(900);
        String temp = "";
        data.append("GET /v2/?regphonesend&token=2723184baf19f302f42ae2b07ea62f04&tpl=mn&subpro=&apiver=v3&tt=" + MyUtil.getTime() + "&is_voice_sms=0&phone=" + phone + "&countrycode=&isexchangeable=1&confirmverifycode=" + code + "&vcodesign=" + verifySign + "&dv=tk0.3055382376385991" + MyUtil.getTime() + "%40mmk0TzBk7ftmKJ7Jc6J32sC-x7J-hTt-xTLCsEQ3cRM0JftWtKsz~XBH~~82vhn3LH7J-TC-hnJ2jYC-x-QXGInZ2SOH~ItYUeTkUY8kqfGp2Bv-uhnGxTJ2s7CYsTCzGYOCuBN0-E8k4XtkvjBk7ftmKJ7Jc6J32sC-x7J-hTt-xTLCsEQ3cRM0JftWNes6~esm~~82vhn3LH7J-TC-hnJ2jYC-x-QXGInZ2SOH~Ysk7yTq__vk0pzsW7K8kpXBH~cskQfGp2Bv-uhnGxTJ2s7CYsTCzGYOCuBN0-ETkJIBm~ItYJfsnUetmKJ7Jc6J32sC-x7J-hTt-xTQrRiMZGjsn7~8k4Ktm~zskUY82vhn3LH7J-TC-hnJ2jYC-x~PrxDOC~ytn4ftW4~8kpIsWNz82vhn3LH7J-TC-hnJ2jYC-x~PrxDOJKRNZGfTkJctI~Ytkqftn7~tnUfGp2Bv-uhnGxTJ2s7CYsTCzhRQzszMzu3GzuRQ6hEQV~_zttPCtBkCn7QhBkhtI~I8kQKGkpLrf~8Wt~snJYBk4YsYNYBkJcBnpKsn7zsYQzskQ-tkJ~wkotn7K8kNe8kpztkJfsW7I8kpctWqftnqetm~KBn4~8kpztWqfsYN-&vcodestr=" + verifyStr + "&traceid=&callback=bd__cbs__qi1x6e HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg&tt=1547777473167&overseas=undefined&gid=E93D98B-635A-4A72-BB6A-040B7E662075&tpl=mn&u=https%3A%2F%2Fwww.baidu.com%2F\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: \r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }


}
