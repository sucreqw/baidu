package com.sucre.service;

import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.JsUtil;
import com.sucre.utils.MyUtil;
import com.sucre.utils.RsaUtils;

import java.net.URLEncoder;
import java.util.Base64;

/**
 * @author sucre chen
 * @Title: BaiduLogin
 * @Package
 * @Description: 百度登录实现。
 * @date 2019-02-23 08:45
 */
public class BaiduLogin extends Thread4Net {


    public BaiduLogin(int l, int u, boolean isCircle) {
        super(l, u, isCircle);
    }

    @Override
    public int doWork(int index) {
        Nets net = new Nets();
        String ret;

        try {
            //取cookie
            ret = net.goPost("passport.baidu.com", 443, getCookie());
            if (!MyUtil.isEmpty(ret)) {
                String cookie=MyUtil.getAllCookie(ret);
                //取token
                ret = net.goPost("passport.baidu.com", 443, getToken(cookie));
                if (!MyUtil.isEmpty(ret)) {
                    String token = MyUtil.midWord("token\" : \"", "\",", ret);
                    //取rsa公钥
                    ret = net.goPost("passport.baidu.com", 443, getPublicKey(cookie,token));
                    if (!MyUtil.isEmpty(ret)) {
                        String key;
                        String RsaKey;

                        key = MyUtil.midWord("\"key\":'", "',", ret);
                        RsaKey = MyUtil.midWord("BEGIN PUBLIC KEY-----\\n", "\\n-----END PUBLIC KEY", ret)
                                .replace("\\n", "")
                                .replace("\\/", "/");
                        RsaKey = RsaUtils.bytesToHexString(Base64.getDecoder().decode(RsaKey))
                                .replace("010001", "");
                        RsaKey = JsUtil.runJS("toarray", RsaKey);
                        RsaKey = RsaKey.substring(6, RsaKey.length());

                        String pass= URLEncoder.encode(RsaUtils.encryptBase64("wqwqwq19890407",RsaKey));
                        //最后登录。
                        ret=net.goPost("passport.baidu.com", 443,loging(cookie,"906509023@qq.com",pass,token,key));
                        if(!MyUtil.isEmpty(ret)){

                        }
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("出错了：" + e.getMessage());
        }
        return 0;
    }


    //登录数据包
    private byte[] loging(String cookie,String id, String pass, String token, String rsakey) {
        StringBuilder data = new StringBuilder(900);
        String t=MyUtil.getTime();
        Long ts= Long.valueOf(t) - 29328;

        String temp = "staticpage=https%3A%2F%2Fpassport.baidu.com%2Fstatic%2Fpasspc-account%2Fhtml%2Fv3Jump.html&charset=UTF-8&token=" + token
                + "&tpl=pp&subpro=&apiver=v3&tt=" + t
                + "&codestring=&safeflg=0&u=https%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%3FrankType%3Dall%26lemmaId%3D75850%26lemmaTitle%3D%25E7%258E%258B%25E4%25BF%258A%25E5%2587%25AF%26isRank%3D1%26bk_share%3Dwechat%26bk_sharefr%3DshushuoRank%26from%3Dsinglemessage%26isappinstalled%3D0&isPhone=&detect=1&gid=CD8C6AB-CC4D-4054-8DF9-8B1DDA0372F3&quick_user=0&logintype=basicLogin&logLoginType=pc_loginBasic&idc=&loginmerge=true&username=" + id
                + "&password=" + pass
                + "&mem_pass=on&rsakey=" + rsakey
                + "&crypttype=12&ppui_logintime=29328&countrycode=&fp_uid=&fp_info=&loginversion=v4&callback=parent.bd__pcbs__r66f0y\r\n";

        data.append("POST https://passport.baidu.com/v2/api/?login HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Content-Length: " + temp.length() + "\r\n");
        data.append("Cache-Control: max-age=0\r\n");
        data.append("Origin: https://passport.baidu.com\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?login&u=https%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%3FrankType%3Dall%26lemmaId%3D75850%26lemmaTitle%3D%25E7%258E%258B%25E4%25BF%258A%25E5%2587%25AF%26isRank%3D1%26bk_share%3Dwechat%26bk_sharefr%3DshushuoRank%26from%3Dsinglemessage%26isappinstalled%3D0\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    //取rsa公钥
    private byte[] getPublicKey(String cookie,String token) {
        StringBuilder data = new StringBuilder(900);
        String temp = "";
        data.append("GET https://passport.baidu.com/v2/getpublickey?token=" + token + "&tpl=pp&apiver=v3&tt=" + MyUtil.getTime() + "&gid=CD8C6AB-CC4D-4054-8DF9-8B1DDA0372F3&loginversion=v4&traceid=&callback=bd__cbs__3nlztj HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?login&u=https%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%3FrankType%3Dall%26lemmaId%3D75850%26lemmaTitle%3D%25E7%258E%258B%25E4%25BF%258A%25E5%2587%25AF%26isRank%3D1%26bk_share%3Dwechat%26bk_sharefr%3DshushuoRank%26from%3Dsinglemessage%26isappinstalled%3D0\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    //取登录用的token
    private byte[] getToken(String cookie) {
        StringBuilder data = new StringBuilder(900);

        data.append("GET /v2/api/?getapi&tpl=pp&apiver=v3&tt=" + MyUtil.getTime() + "&class=login&gid=CD8C6AB-CC4D-4054-8DF9-8B1DDA0372F3&loginversion=v4&logintype=basicLogin&traceid=&callback=bd__cbs__lfeats HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://passport.baidu.com/v2/?login&u=https%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%3FrankType%3Dall%26lemmaId%3D75850%26lemmaTitle%3D%25E7%258E%258B%25E4%25BF%258A%25E5%2587%25AF%26isRank%3D1%26bk_share%3Dwechat%26bk_sharefr%3DshushuoRank%26from%3Dsinglemessage%26isappinstalled%3D0\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: "+ cookie +"\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    //取登录用的cookie
    private byte[] getCookie() {
        StringBuilder data = new StringBuilder(900);
        data.append("GET https://passport.baidu.com/v2/?login&u=https%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%3FrankType%3Dall%26lemmaId%3D75850%26lemmaTitle%3D%25E7%258E%258B%25E4%25BF%258A%25E5%2587%25AF%26isRank%3D1%26bk_share%3Dwechat%26bk_sharefr%3DshushuoRank%26from%3Dsinglemessage%26isappinstalled%3D0 HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Upgrade-Insecure-Requests: 1\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
        data.append("Referer: https://wapbaike.baidu.com/starflower/starrank?rankType=all&lemmaId=75850&lemmaTitle=%E7%8E%8B%E4%BF%8A%E5%87%AF&isRank=1&bk_share=wechat&bk_sharefr=shushuoRank&from=singlemessage&isappinstalled=0\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append("\r\n");

//            StringBuilder data = new StringBuilder(900);
//            String temp = "";
//            data.append("GET https://wapbaike.baidu.com/starflower/starrank?rankType=all&lemmaId=75850&lemmaTitle=%E7%8E%8B%E4%BF%8A%E5%87%AF&isRank=1&bk_share=wechat&bk_sharefr=shushuoRank&from=singlemessage&isappinstalled=0 HTTP/1.1\r\n");
//            data.append("Host: wapbaike.baidu.com\r\n");
//            data.append("Connection: keep-alive\r\n");
//            data.append("Upgrade-Insecure-Requests: 1\r\n");
//            data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
//            data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
//            data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
//            data.append("X-Requested-With: com.baidu.searchbox\r\n");
//            data.append("\r\n");
//            data.append("\r\n");

        return data.toString().getBytes();
    }


}
