package com.sucre.service;

import com.sucre.entity.Baidu;
import com.sucre.impl.BaiduImpl;
import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;
import com.sucre.utils.MyUtil;
import com.sucre.utils.RsaUtils;

/**
 * @author sucre chen
 * @Title: BaiduLoginMac
 * @Package com.youmi.service
 * @Description: 新浪登录实现mac系统下的接口。
 * @date 2019-02-23 15:38
 */
public class BaiduLoginMac extends Thread4Net {

    public BaiduLoginMac(int l, int u, boolean isCircle) {
        super(l, u, isCircle);
    }

    @Override
    public int doWork(int index) {

        Nets net=new Nets();
        String ret;
        String Key="B3C61EBBA4659C4CE3639287EE871F1F48F7930EA977991C7AFE3CC442FEA49643212E7D570C853F368065CC57A2014666DA8AE7D493FD47D171C0D894EEE3ED7F99F6798B7FFD7B5873227038AD23E3197631A8CB642213B9F27D4901AB0D92BFA27542AE890855396ED92775255C977F5C302F1E7ED4B1E369C12CB6B1822F";
        ret=net.goPost("wappass.baidu.com",443,getServerTime());
        if(!MyUtil.isEmpty(ret)){
            String serverTime=MyUtil.midWord("time\":\"","\"}",ret);

            String pass= RsaUtils.encrypt("wqwqwq19890407"+serverTime,Key,"10001").toLowerCase();
            ret=net.goPost("wappass.baidu.com",443,loging("","906509023%40qq.com",pass,serverTime));
            if(!MyUtil.isEmpty(ret)){

            }

        }
        return 0;
    }


    //登录数据包
    private byte[] loging(String cookies, String id,String pass,String serverTime ) {
        StringBuilder data = new StringBuilder(900);
        String temp = "username="+ id
                +"&password="+ pass
                +"&verifycode=&vcodestr=&action=login&"
                +"u=https%253A%252F%252Fwapbaike.baidu.com%252Fstarflower%252Fstarrank%253FrankType%253Dall%2526lemmaId%253D75850%2526lemmaTitle%253D%2525E7%25258E%25258B%2525E4%2525BF%25258A%2525E5%252587%2525AF%2526isRank%253D1%2526bk_share%253Dwechat%2526bk_sharefr%253DshushuoRank%2526from%253Dsinglemessage%2526isappinstalled%253D0%2526uid%253D1550906245379_873%2526traceid%253D3740FB03"
                +"&tpl=wk&tn=&pu=&ssid=&from=&bd_page_type=&uid="+ MyUtil.getTime() +"_873&type=&regtype=&subpro=&adapter=0&skin=default_v2&regist_mode=&login_share_strategy=&client=&clientfrom=&connect=0"
                + "&bindToSmsLogin=&extrajson=&isphone=0&loginmerge=1&getpassUrl=%2Fpassport%2Fgetpass%3Fclientfrom%3D%26adapter%3D0%26ssid%3D%26from%3D%26authsite%3D1%26bd_page_type%3D%26uid%3D1550906245379_873%26pu%3D%26tpl%3Dwk%26u%3Dhttps%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%253FrankType%253Dall%2526lemmaId%253D75850%2526lemmaTitle%253D%2525E7%25258E%25258B%2525E4%2525BF%25258A%2525E5%252587%2525AF%2526isRank%253D1%2526bk_share%253Dwechat%2526bk_sharefr%253DshushuoRank%2526from%253Dsinglemessage%2526isappinstalled%253D0%2526uid%253D1550906245379_873%2526traceid%253D3740FB03%26type%3D%26bdcm%3Dd6b08baeb2119313c17eca8065380cd790238d94%26tn%3D%26regist_mode%3D%26login_share_strategy%3D%26subpro%3D%26skin%3Ddefault_v2%26client%3D%26connect%3D0%26smsLoginLink%3D1%26loginLink%3D%26bindToSmsLogin%3D%26overseas%3D%26is_voice_sms%3D%26subpro%3D%26traceid%3D3740FB03%26hideSLogin%3D%26forcesetpwd%3D%26nousername%3D%26regdomestic%3D%26extrajson%3D"
                +"&dv=tk0.237086531500393161550906246154%40qqr0~mAC4WnmeWKz0aKXcc4zJ-KSpdHGyenkB~LkBf5kqWKtZIODfd0MA34SxTKD9WnCozn2ycAmyy5teiHz3V5MJYHMsVFDc35koY7kvwACnWnmeWKz0aKXcc4zJ-KSpdHGye7kF~LkBf5kqWKtZIODfd0MA34SxTKD9WnCQ~A2y_mrGn1ue5kuzA-yzn1ne5teiHz3V5MhT4lAlKlsQLq__yooOzo~t7E44h~rin-y-5k4-Crl0tWy51uYAYqfA19YnC9ynknxnYoznC9cnkQyA1u~A1ocAq__irL52JVHtJSODx3Hq__IrZn1nWnmye7Cqc5kNx7mye7Cuy5koy7kqWnCQ-nmye7Cuy5kQlAy__&countrycode=&mobilenum="
                +"&servertime="+ serverTime +"&gid=D8ECF2B-784B-493B-9769-EC4AD9FBC416&"
                +"ds=zRi283QQ%2FLgnvz4Xe8THKhvMtNq2cZ%2BMRrzGFNYbl23gYAqwMAxIv2xpUSgntidE2kLySxPQA%2BaV4S7XvAUmJl57tMrD1A42KtYkkXlklbPn18ofJ7jplSGJuphE3eqZ3DEtlI5pSHXaxNTF%2FuPKejjmLbCaw5VJzffYYLyfWhaX99HixQGEs7It54GQKU053asm1rDqm477B6d00hPRsmX%2BgFmm%2Fk45SlShuJjSCJSRlDTwkXd6k%2F6VRX3q%2BlLb569aQUtCFtxwOdZZH0lwvr1lk2KBVVdgtn06vmQE9WFZcFxY%2B9%2BDzWtcbbQyvA9tQ3qfvz3y96%2FPRlUDoz0PKeMO9uMU%2F%2FpkEeaav4C%2F5S%2FpKQMeriKCbI0JRJQp%2BUE45VIhNiHOgaU%2FDCatlbzvOvU5llCT7kCkckpXhPpKbjjzH0He%2Bphgohl0cbdJLPNZTIkNRCNpUtgVWxVGI97%2FBb6gZpfBkK1Sk36w5EtPwMt%2B0X7iMdXk8xme9d%2BWexNi%2BzBaV0Jl8AbHe8KSXsZRFWkbCS1Nf5fDfBYJv69oj8FC8nYFhogldtRuaLY34wJvBjfVcAm4%2BTQuimEwyjEnYNTHVvZ3LHDjIfE6TNTAiq%2FOPauF1SGWtrwc%2BmMLL%2BeF625R6sH9xsv7iIjaO2i424ipP%2BJemwZpEzUXLoOMxd%2FMTIrC5co1JMgOPLkgooJt2uxtkzGZ9Michi0EWtYCNQu8G4nWDPoONnUJ%2Br3XzmmnScRqU%2FmEsrWeJ4PkS78oO8%2ByC8Zv1%2BEVjswHx%2FnH8yXRteuzFLY1QpFiavoaH%2BZ5vvXeePyhm4dXtn3pIq4KJY86MnR3KR1qVj0g1hJQ6uZ%2F3rzxfjSSrpAAjZWkIB67EDwod2AnkwdO4ibrSpKdZBKy3UIKZzKMs3QG4SmzSTyp%2Fol2qeqeCcFDh3NG2oEjf5YfsWI83fMmGBf%2BCjoWDMMq%2FMwmWxpcOYpBjSfeBlwmHqj1%2Bn5yF%2F4lVp92%2Bqk%3D&tk=8520eYOYrTcPaOPrBVBu12%2BfVVF6D3ngxWSec8lqOWtdzg0%3D"
                +"&logLoginType=wap_loginTouch&FP_UID=&\r\n";

        data.append("POST /wp/api/login?tt="+ MyUtil.getTime() +" HTTP/1.1\r\n");
        data.append("Host: wappass.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Content-Length: "+ temp.length() +"\r\n");
        data.append("Accept: application/json\r\n");
        data.append("Origin: https://wappass.baidu.com\r\n");
        data.append("X-Requested-With: XMLHttpRequest\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36\r\n");
        data.append("Content-Type: application/x-www-form-urlencoded\r\n");
        data.append("Referer: https://wappass.baidu.com/passport/login?tpl=wk&authsite=1&u=https%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%3FrankType%3Dall%26lemmaId%3D75850%26lemmaTitle%3D%25E7%258E%258B%25E4%25BF%258A%25E5%2587%25AF%26isRank%3D1%26bk_share%3Dwechat%26bk_sharefr%3DshushuoRank%26from%3Dsinglemessage%26isappinstalled%3D0\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: \r\n");
        data.append("\r\n");
        data.append(temp);
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }

    //取servertime,rsa加密用。
    private byte[] getServerTime() {
        StringBuilder data = new StringBuilder(900);
        String temp = "";
        data.append("GET /wp/api/security/antireplaytoken?tpl=wk&v="+ MyUtil.getTime() +"& HTTP/1.1\r\n");
        data.append("Host: wappass.baidu.com\r\n");
        data.append("Connection: keep-alive\r\n");
        data.append("Accept: application/json\r\n");
        data.append("X-Requested-With: XMLHttpRequest\r\n");
        data.append("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36\r\n");
        data.append("Referer: https://wappass.baidu.com/passport/login?tpl=wk&authsite=1&u=https%3A%2F%2Fwapbaike.baidu.com%2Fstarflower%2Fstarrank%3FrankType%3Dall%26lemmaId%3D75850%26lemmaTitle%3D%25E7%258E%258B%25E4%25BF%258A%25E5%2587%25AF%26isRank%3D1%26bk_share%3Dwechat%26bk_sharefr%3DshushuoRank%26from%3Dsinglemessage%26isappinstalled%3D0\r\n");
        data.append("Accept-Language: en-US,en;q=0.9\r\n");
        data.append("Cookie: \r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        data.append("\r\n");
        return data.toString().getBytes();
    }




}
