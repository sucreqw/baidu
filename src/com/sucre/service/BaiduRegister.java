package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.factor.Factor;
import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.JsUtil;
import com.sucre.utils.MyUtil;
import com.sucre.utils.RsaUtils;

import java.net.URLEncoder;
import java.util.Base64;

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
                //取往后包要用到的cookie
                String cookie="";
                ret=net.goPost("passport.baidu.com",443,getCookie());
                if(!MyUtil.isEmpty(ret)){cookie=MyUtil.getAllCookie(ret);}
                //取往后包要用到的token
                String token="";
                ret=net.goPost("passport.baidu.com",443,getToken(cookie));
                if(!MyUtil.isEmpty(ret)){token=MyUtil.midWord("token\" : \"","\", ",ret);}
                //百度用来记录钥的key
                String key="";
                //取加密要用到的rsa key 公钥
                String RsaKey="";

                ret=net.goPost("passport.baidu.com",443,getKey(cookie,token));
                if(!MyUtil.isEmpty(ret)){
                    key=MyUtil.midWord("\"key\":'","',",ret);
                    RsaKey=MyUtil.midWord("BEGIN PUBLIC KEY-----\\n","\\n-----END PUBLIC KEY",  ret);
                    RsaKey=RsaKey.replace("\\n","");
                    RsaKey=RsaKey.replace("\\/","/");
                    RsaKey=RsaUtils.bytesToHexString(Base64.getDecoder().decode(RsaKey));
                    RsaKey=RsaKey.replace("010001","");
                    RsaKey= JsUtil.runJS("toarray",RsaKey);
                    RsaKey=RsaKey.substring(6,RsaKey.length());
                   // System.out.println(RsaKey+"《==》");
                }

                //第一步先取到图片的hash
                ret = net.goPost("passport.baidu.com", 443, getPicHash(cookie,token));
                if (!MyUtil.isEmpty(ret)) {
                    //String cookie=MyUtil.getAllCookie(ret);
                    //返回的verifyStr再到下一步取真正的图片
                    String verifyStr = MyUtil.midWord("verifyStr\" : \"", "\",", ret);
                    String verifySign = MyUtil.midWord("verifySign\" : \"", "\",", ret);
                    MyUtil.print("取到图片token", Factor.getGui());
                    byte[] rets = net.goPostByte("passport.baidu.com", 443, getPic(verifyStr,cookie));

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
                            ret = net.goPost("passport.baidu.com", 443, sendSMS(phone, verifySign, URLEncoder.encode(code), verifyStr,cookie,token));
                            if(!MyUtil.isEmpty(ret)){
                                String retNO=MyUtil.midWord("no\": \"","\"",ret);
                               // if("0".equals(retNO)){
                                    MyUtil.print("手机验证码发送成功，请接收后输入并回车！",Factor.getGui());
                                    String pass=Factor.getidInfo().getPassword();
                                    String nick;
                                    nick = URLEncoder.encode(Factor.getidInfo().getRandNick());
                                    String sms=Factor.getSms().getCode();
                                    String rsapass= URLEncoder.encode(RsaUtils.encryptBase64(pass,RsaKey));
                                    ret=net.goPost("passport.baidu.com",443,regSubmit(cookie,URLEncoder.encode(code),token,sms,phone,key,verifySign,verifyStr,nick,rsapass));
                                   if(!MyUtil.isEmpty(ret)){
                                       System.out.println(ret);
                                   }

                               // }else{
                                //    MyUtil.print("验证码发送失败，错误码："+ retNO,Factor.getGui());
                               // }
                            }


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
    private byte[] getPicHash(String cookie,String token) {

        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        data.append("GET /v2/?regsmscodestr&token="+ token +"&tpl=mn&apiver=v3&tt=" + MyUtil.getTime() + "&traceid=&callback=bd__cbs__pe5erq HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg&tt=1547777473167&overseas=undefined&gid=E93D98B-635A-4A72-BB6A-040B7E662075&tpl=mn&u=https%3A%2F%2Fwww.baidu.com%2F\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
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
    private byte[] getPic(String token,String cookie) {
        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        data.append("GET /cgi-bin/genimage?" + token + " HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: image/webp,image/apng,image/*,*/*;q=0.8\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg&tt=1547777473167&overseas=undefined&gid=E93D98B-635A-4A72-BB6A-040B7E662075&tpl=mn&u=https%3A%2F%2Fwww.baidu.com%2F\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

    /**
     * 发送手机验证码
     * @param phone 手机号码
     * @param verifySign gethash返回的sign
     * @param code 图片验证码
     * @param verifyStr gethash返回的str
     * @param cookie
     * @return
     */
    private byte[] sendSMS(String phone, String verifySign, String code, String verifyStr,String cookie,String token) {
        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        data.append("GET /v2/?regphonesend&token="+ token + "&tpl=&subpro=&apiver=v3&tt=" + MyUtil.getTime() + "&is_voice_sms=0&phone=" + phone + "&countrycode=&isexchangeable=1&confirmverifycode=" + code + "&vcodesign=" + verifySign + "&dv=tk0.30553823763859911547534972575%40mmk0TzBk7ftmKJ7Jc6J32sC-x7J-hTt-xTLCsEQ3cRM0JftWtKsz~XBH~~82vhn3LH7J-TC-hnJ2jYC-x-QXGInZ2SOH~ItYUeTkUY8kqfGp2Bv-uhnGxTJ2s7CYsTCzGYOCuBN0-E8k4XtkvjBk7ftmKJ7Jc6J32sC-x7J-hTt-xTLCsEQ3cRM0JftWNes6~esm~~82vhn3LH7J-TC-hnJ2jYC-x-QXGInZ2SOH~Ysk7yTq__vk0pzsW7K8kpXBH~cskQfGp2Bv-uhnGxTJ2s7CYsTCzGYOCuBN0-ETkJIBm~ItYJfsnUetmKJ7Jc6J32sC-x7J-hTt-xTQrRiMZGjsn7~8k4Ktm~zskUY82vhn3LH7J-TC-hnJ2jYC-x~PrxDOC~ytn4ftW4~8kpIsWNz82vhn3LH7J-TC-hnJ2jYC-x~PrxDOJKRNZGfTkJctI~Ytkqftn7~tnUfGp2Bv-uhnGxTJ2s7CYsTCzhRQzszMzu3GzuRQ6hEQV~_zttPCtBkCn7QhBkhtI~I8kQKGkpLrf~8Wt~snJYBk4YsYNYBkJcBnpKsn7zsYQzskQ-tkJ~wkotn7K8kNe8kpztkJfsW7I8kpctWqftnqetm~KBn4~8kpztWqfsYN-&vcodestr=" + verifyStr + "&traceid=&callback=bd__cbs__qi1x6e HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg&tt=1547777473167&overseas=undefined&gid=E93D98B-635A-4A72-BB6A-040B7E662075&tpl=mn&u=https%3A%2F%2Fwww.baidu.com%2F\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    /**
     *取注册的cookie
     * @return
     */
    private byte[] getCookie() {
        StringBuilder data = new StringBuilder(900);
       // String temp = "";
        data.append("GET https://passport.baidu.com/v2/?reg HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
        data.append("Referer: https://www.baidu.com/\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    /**
     * 取注册用的token值
     * @param cookie getcookie返回的cookie
     * @return
     */
    private byte[] getToken(String cookie) {
        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        data.append("GET https://passport.baidu.com/v2/api/?getapi&tpl=&apiver=v3&tt=gettime&class=regPhone&gid=5658FDF-51D8-4DD8-A68D-5499568B8164&app=&traceid=&callback=bd__cbs__d4odbs HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    /**
     * 取加密的公钥。
     * @param cookie
     * @param token
     * @return
     */
    private byte[] getKey(String cookie, String token) {
        StringBuilder data = new StringBuilder(900);
        //String temp = "";
        data.append("GET https://passport.baidu.com/v2/getpublickey?token="+token +"&tpl=&apiver=v3&tt="+ MyUtil.getTime()+"&gid=5658FDF-51D8-4DD8-A68D-5499568B8164&traceid=&callback=bd__cbs__eyla5q HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    /**
     * 最终的注册包。
     * @param cookie getcookie返回的
     * @param code 图片验证码
     * @param token gettoken返回的
     * @param SMScode 手机验证码
     * @param phone 前面接收短信的手机号
     * @param key rsa公钥
     * @param verifySign 图片验证码sign
     * @param verifyStr 图片验证码str
     * @param nick 账号昵称
     * @param pass 账号密码，经过rsa加密。
     * @return
     */
    private byte[] regSubmit(String cookie, String code,String token,String SMScode,String phone,String key,String verifySign,String verifyStr,String nick,String pass) {
        StringBuilder data = new StringBuilder(900);
        String temp = "staticpage=https%3A%2F%2Fpassport.baidu.com%2Fstatic%2Fpasspc-account%2Fhtml%2Fv3Jump.html&charset=UTF-8&token="+ token +"&tpl=&subpro=&apiver=v3&tt="+MyUtil.getTime()+"&retu=&u=&quick_user=0&regmerge=false&suggestIndex=&suggestType=&codestring=&vcodesign="+ verifySign +"&vcodestr="+ verifyStr+"&gid=5658FDF-51D8-4DD8-A68D-5499568B8164&app=&pass_reg_suggestuserradio_0=&islowpwdcheck=undefined&logRegType=pc_regBasic&isexchangeable=1&exchange=0&sloc=loaded%23%23%23417%2311%231547956223443%23%231547959580024%40email%23%23%23%23%23%23%23%40userName%23328%2316%23134.5%2313%231547956421745%231547956422356%231547956226317%40phone%23328%2316%2331.5%2332%231547956424296%231547956425858%231547956435890%40smscode%23%23%23%23%23%23%23%40verifyCode%23156%2316%2395.5%2322%231547959571771%231547959572368%231547959574853%40password%23328%2316%2393.5%2311%231547959556271%231547959556970%231547959559926%40submit%23350%2350%23123.5%2334.765625%231547959575064%23%231547959577166%40&_username=&_regpass=&_rsakey=&_regfrom=reg&username="+ nick +"&phone="+ phone +"&loginpass="+ pass +"&verifycode="+ code +"&isagree=on&smscode="+ SMScode +"&ppui_regtime=3357022&isvoicesmsreg=0&rsakey="+ key +"&crypttype=12&dv=tk0.62060183987277021547956223186%40kko0lIvI6jpkqTrsotGwnhFryD9oAEH-ADHar-LHntC0wWpksjtFsav4izvI3Tvm%7E9E9j493oAHwyE9whDvwyDBHAWR3jMJ09TvF3jvIQgDk6zt8iipoGhF3B8E9wDHwhF9oX-HwywRxrzFSoOL8i%7EtF3ztkWXvI6jpkqTrsotGwnhFryD9oAEH-ADHar-LHntC0wWpksjtF6jvHizvI3Tvm%7E9E9j493oAHwyE9whDvwyDBHAWR3jMJ09TvF3jvI3%7EDq__to0wyAFR%7EpkE-vmizvFQgpoGhF3B8E9wDHwhF9oX-Hwy-B0nO7HGXv-Cipksgv8izvkqxvIQTrsotGwnhFryD9oAEH-ADHxLNRSwXAkCxpkviA8izvk3gtFCTrsotGwnhFryD9oAEH-ADHxLNRSwXAFsxpkvxA8izvFvjAIsTrsotGwnhFryD9oAEH-6zHwyIJxjS7HnOrxW3LxrfHxLNJaGWRZiav-CTv-Rfpk6%7EAI6xv1%7E9E9j493oAHwyE9whDvInDHxANJSLdRSwDCxoYCxrTDq__Ipp7ypCPPcERhCohv8izpkR%7EEodB2TipICzvkCivFQ-tFQavIRavk6%7EAFEatF9xvI6-vFQxho0Cp74GfR4vepzyiCHA-R2yzBmj1C0W3B8jIJxfNBI6N5anWLz%7EwJSGWLSWYL0E_xosvmiipks-Ak3TAIvapks-AICTA-Cgpks-AICTvFvxA1iavIQ_&countrycode=&traceid=&callback=parent.bd__pcbs__bfa597\r\n";
        data.append("POST https://passport.baidu.com/v2/api/?regphone HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Content-Length: "+ temp.length() +"\r\n");
        data.append("Cache-Control: max-age=0\r\n");
        data.append("Origin: https://passport.baidu.com\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?reg\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");

        return data.toString().getBytes();
    }

}
