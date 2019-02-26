package com.sucre.service;

import com.sucre.controller.Controller;
import com.sucre.entity.Baidu;
import com.sucre.factor.Factor;
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
        String ds="";
        String tk="";
        String ak="";

        try {
            //取cookie
            ret = net.goPost("passport.baidu.com", 443, getCookie());
            if (!MyUtil.isEmpty(ret)) {
                String cookie = MyUtil.getAllCookie(ret);
                //取token
                ret = net.goPost("passport.baidu.com", 443, getToken(cookie));
                if (!MyUtil.isEmpty(ret)) {
                    String token = MyUtil.midWord("token\" : \"", "\",", ret);

                    //get tk
                    ret=net.goPost("ss0.bdstatic.com",443,getAK(cookie));
                    if(!MyUtil.isEmpty(ret)){
                       ak=MyUtil.midWord("ak:\"","\"}",ret);
                    }
                    //get dv ds hash
                    ret=net.goPost("passport.baidu.com", 443,getHash(cookie,ak));
                    if(!MyUtil.isEmpty(ret)){
                        tk=MyUtil.midWord("tk\":\"","\",\"",ret);
                        ds=MyUtil.midWord("ds\":\"","\"",ret);

                    }

                    //取rsa公钥
                    ret = net.goPost("passport.baidu.com", 443, getPublicKey(cookie, token));
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

                        String pass = URLEncoder.encode(RsaUtils.encryptBase64(baidu.getPass(), RsaKey));
                        //最后登录。
                        ret = net.goPost("passport.baidu.com", 443, loging(cookie, baidu.getId(), pass, token, key,tk,ds));
                        if (!MyUtil.isEmpty(ret)) {

                            String msgs = MyUtil.midWord("err_no=", "&", ret);
                            if ("0".equals(msgs)) {
                                cookie = MyUtil.getAllCookie(ret);
                                baidu.setCookie(cookie);
                                MyUtil.print("登录成功！" + index, Factor.getGui());
                            } else {
                                MyUtil.print("登录失败！" + index + "<>" + msgs, Factor.getGui());
                            }

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
    private byte[] loging(String cookie,String id, String pass, String token, String rsakey,String tk,String ds) {
        StringBuilder data = new StringBuilder(900);
        String t=MyUtil.getTime();
        Long ts= Long.valueOf(t) - 29328;

        String temp = "staticpage=https%3A%2F%2Fpassport.baidu.com%2Fstatic%2Fpasspc-account%2Fhtml%2Fv3Jump.html&charset=UTF-8&token=" + token
                + "&tpl=pp&subpro=&apiver=v3&tt=" + t
                + "&codestring=&safeflg=0&u=https%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%3FrankType%3Dall%26lemmaId%3D75850%26lemmaTitle%3D%25E7%258E%258B%25E4%25BF%258A%25E5%2587%25AF%26isRank%3D1%26bk_share%3Dwechat%26bk_sharefr%3DshushuoRank%26from%3Dsinglemessage%26isappinstalled%3D0&isPhone=&detect=1&gid=CD4C6AB-CC4D-4054-8DF9-8B4DDA0372F4&quick_user=0&logintype=basicLogin&logLoginType=pc_loginBasic&idc=&loginmerge=true&username=" + id
                + "&password=" + pass
                + "&mem_pass=on&rsakey=" + rsakey
                + "&crypttype=12&ppui_logintime=29328&countrycode=&fp_uid=&fp_info=&loginversion=v4&"
                +"ds="+ URLEncoder.encode(ds)
                +"&tk="+ URLEncoder.encode(tk)+"&"
                +"dv=tk0.07372693476691405"+ ts +"@hhb0BC940W0mzrAr-5rVt7MyxAryhU7txU8M73GV-uPKrW01LQ7MQR7IQQ6tnh4V8CAryUMyh4rtgbMyxyGZH~4ateOCQ~7cSQUkSW0mzrAr-5rVt7MyxAryhU7txU8M73GV-uPKrW9kLZ0jQy7~QQ6tnh4V8CAryUMyh4rtgbMyxyGZH~4ateOCQ~0kSb7MQb9mQQ6tnh4V8CAryUMyh4rtgbMyxyGZH~4ateOCQ~0kVy7DQ_qb0yq040-0IQz9kqW040Q9CzrAr-5rVt7MyxAryhU7txU8M73GV-uPKHg040Q7IQ~0k0W0cq~9CzrAr-5rVt7MyxAryhU7txUGptcGj8fGang04A~0~Qz71GW7koj9Czg04oQ0mQ~7~Qy01VZ65Qz0cAz6k2R7CQj71A-6tnh4V8CAryUMyh4rtgbMyxyGZH~4ateOMQ_CvvN~vzQ068Ghzbi0~Q~6kG~Fbj8pWQ61qj0cG~71Vc7kGZ71Vz7kqy04ry042y04V~0kq-9q__Ibl010W0mQz94qc6koj0kGW04V~0mQz0kSQ6k2-01qW04Vz9mQ-9kG_&traceid=3E8D4B01&callback=parent.bd__pcbs__r66f0y\r\n";

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
        data.append("GET https://passport.baidu.com/v2/getpublickey?token=" + token + "&tpl=pp&apiver=v3&tt=" + MyUtil.getTime() + "&gid=CD4C6AB-CC4D-4054-8DF9-8B4DDA0372F4&loginversion=v4&traceid=&callback=bd__cbs__3nlztj HTTP/1.1\r\n");
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

        data.append("GET /v2/api/?getapi&tpl=pp&apiver=v3&tt=" + MyUtil.getTime() + "&class=login&gid=CD4C6AB-CC4D-4054-8DF9-8B4DDA0372F4&loginversion=v4&logintype=basicLogin&traceid=&callback=bd__cbs__lfeats HTTP/1.1\r\n");
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


    private byte[] getHash(String cookies,String ak) {
        StringBuilder data = new StringBuilder(900);
        data.append("GET https://passport.baidu.com/viewlog?ak="+ ak +"&as=d5a70239&fs=ScxHvkEvoGT2IiKrHi9VByAUHUtlq63oClHwzQ2k3YS212luQuJrvbh5pVMLee9%2FuWT0ZJ6fYBQbmE%2BV5tqOZwAYMQMTfosPJoP26HnZnlJNGnOHFaZP%2FUoVxRAwszrzorAxgyYzWhu%2BX3UOuic%2BXGm8sq%2BMCMNk8gtNRAelQByldIPYNxibaDGvKWRd5EQa1kIcB%2FZQCGJJWDMNAO6q1ZMnMkf%2FLzgd8rhjBGS7LSq1TfiCtZAmaAPlXgEYDBtSBz8r2MG1O1liJw6DybBntCYNvXgsfS%2BhXs2QvphcWSFzxAkH77%2BhldzOctX7TUsQz%2Fpw4NwZMpViwbTbGmqDidkJRFNqlzpRfaPCGzrPvM4jPNbKtICHMmmCsXQf4s4%2B86BeGptefeYQWPVTADWURaj4wzupWjJzUOzuDzV3cWJ32JmiCDN63okxXCH8PmX%2BNfWFPSpmLuh9CMtN5%2B34z9lKsKVdWq2%2F1NKuNGb0pPgWcR%2Bf2KFCZAv1YXUwfnYbyLWRU7VojhiiMFaHrpwsTitKcNpwDzEYZ%2BZ4%2BL7MCNvLQcRRQ59TZ6RWEm8WstghX5gRb93CeTYnXu5DuqUUqb1UisdI6wyg4XOlK6LHmppSNDtjjqKPO8IUtfRImh3RbAKztjiBScCfF0YSGqnmM19U1xvrEmYJXY290bb3bORs4GgEHH6psg062NCfIUrt%2B3R%2BZBDVJ4rvpuxY%2FxBS0GFInu9Y4ZquhssfI4aZkwnAWTKwXm1grWE%2F3gdIJ6T9F%2FGdvQmOWY7ZV6PKHR6aeEsYfkBPRBHScTC2%2B9h2kg7q3AeFUlRmi5PDIIBnMpQ54UV2hSSY2oye5NrCr%2FoZ%2FX2ki0hF6KQQlV4AIEZuDsh2YiAWohzeSFClMH4XqSjyPxYJyac6gDn2zkpK9XEKc%2BanpkIcS8he7unR%2BldtiqltA2oMH9gH%2Bj40K2Cz1J3P6pHirhImNoYpTvuJFwjqn2kBib9pnDvXpmMEkIoCc%2BticzKVNSbZnfh2RlaSAXLtmJgN1UBv9Eha4Npa8w69pkLKfwqQ%2F4QN0HkqWdGWTAxjcmBGPSVQAHW0W3U1LSX7yL1aeGZ9BoT%2ByRWcxiA9aDV%2FdUqNlVfkrIYYHZraHYPm40cWHwAk8iGY3%2BhMeM510TlAR4LRWxYMrXeON0SXsmHGgyN4frV%2BNmeURduDI8BsWG8HdTRxxDp54y8Tfp6Guk4%2Fuc%2B7hhN6%2Fiy77jQBfwKuMMrRHaGTnGa%2FGwodFxtS%2Fbxu4oxzmM6cK5BccACEh4mADXly7%2BwYCIoETL%2FnxamDpK573Nha48g2eGhOWZ2AbQDfMlYPh7gpzDwE2s9lNAqpdINLxUVnkiPu1n60Ui5%2F72bQ4iFe32AhfGhdHzw%3D&callback=jsonpCallbackb1214&v=9901 HTTP/1.1\r\n");
        data.append("Host: passport.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://www.baidu.com/\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("Cookie: "+ cookies +"\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    //get tk
    private byte[] getAK(String cookies) {
        StringBuilder data = new StringBuilder(900);
        data.append("GET https://ss0.bdstatic.com/5LMZfyabBhJ3otebn9fN2DJv/passApi/js/loginv4_ae2aa2f.js HTTP/1.1\r\n");
        data.append("Host: ss0.bdstatic.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3573.0 Safari/537.36\r\n");
        data.append("Accept: */*\r\n");
        data.append("Referer: https://www.baidu.com/\r\n");
        data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
        data.append("X-Requested-With: com.baidu.searchbox\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

}
