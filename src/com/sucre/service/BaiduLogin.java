package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.entity.Baidu;
import com.sucre.impl.CommonImpl;
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

        Baidu baidu=new Baidu();
        CommonImpl baiduimpl= Controller.getInstance().ImplId;
        baidu=(Baidu) baiduimpl.get(index-1, baidu);
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

                        String pass= URLEncoder.encode(RsaUtils.encryptBase64(baidu.getPass(),RsaKey));
                        //最后登录。
                        ret=net.goPost("passport.baidu.com", 443,loging(cookie,baidu.getId(),pass,token,key));
                        if(!MyUtil.isEmpty(ret))
                            cookie=MyUtil.getAllCookie(ret);

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
                + "&crypttype=12&ppui_logintime=29328&countrycode=&fp_uid=&fp_info=&loginversion=v4&ds=d3hMaElTTY4w0ybxpYmQhV2nygbIQiuBs4ekC8MgkQDoq%2FRLhyOuF4Txo9WyUVy8l060nCFP08Jl9p33Jfm6TbJbvyhndAB37xf9Yh3WA6gsbvS1FQWpoE790ZdcBHJj7jWG1TRdTYBTfX52n7dZXTZooFAROJphgJ8%2BCrOIH4uZmoMOQVhzZGxtDU3uCq7LnFHXBwN7tWOE3NVdesJ2DhqnlTkag5UgmJNC6ZVXgBela9ykt%2BF92nhnb21tVbf8rSVRwFpJCRId52NyKPZ3GC2OwF0%2FdoR8dmlc4jproEFRe%2F%2Fddap1epCfuVKU3cG%2BjbspI6DGb0SPQcx7D1YzDUVePoDSvBKNacFGImVUkUC6g2ZfZABla%2B0g3vcw1KMaj5TYTNHrSqXQKFjQFW6o9h0EsvYsDnZxzBglHkFD1eXlZMHeHLqbSZ%2FwaVSkKWRXlj9TWuNN1BcSU3KYv%2Bms1V%2FK5VuQlfAeLD2rUZC804wIq2ft0TAYYbAbYBySKNF2Tp99kXFUyTgpUOPvCnWUZHflXVO1EoVAv%2BP%2BG35%2B08M%3D&tk=4300igFTr%2BCSTqcKHNocm%2FmK%2BnAIzoc0fh3SPJKSRl9RDYA%3D&dv=tk0.98469829039677511550924584821%40qqt0Tupn9f2maJtJlsJ3upCyetJyhP2yePQCpO93lTMLJfpn2w2Xg-Bmgg8uGhn3QrtJyPCyhnJuYRCyey9dIwnUuZArgypkt%7EPkJ-8kqfIvuBGy5hnIePJuptCRpPCjIRAC5BELyO8kEgpkTYpn2f2maJtJlsJ3upCyetJyhP2yePQCpO93lTMLJfpN2d2sg-Bmgg8uGhn3QrtJyPCyhnJuYRCyey9dIwnUuZArgdpkKRPq__vt0LvBk4y8kJRBmgwBn9a8uGhn3QrtJyPCyhnJuYRCyeUMde-AC5G9UpiA0ImQ0lYBkty8k2gBrga2RJw2waJtJlsJ3upCyetJyhP2yeP90uR9jQi9UGYBktd8k2dp1gw2kKj21aJtJlsJ3upCyetJyhP2yePMLIZEUIwJ0uR9-aTEUIfPq__urr6wr-Ffn99h-th2rgw8k9aItpQ0fg8N3%7EpkElBk4l2k2lpN9jpnvapnJgBn4-pnK-Bk4aht0yr6sG-9s2W8wegECpR90ewQml1ELO3QrlNMd-iQN4i7daiAdOD8sIDA0IU6LlOAq__Fto2mgg8kvRpk3fpNJd8kvRpNEfpRE%7E8kvRpNEf2n2dp1gj2NK_&traceid=07302E01&callback=parent.bd__pcbs__r66f0y\r\n";

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
