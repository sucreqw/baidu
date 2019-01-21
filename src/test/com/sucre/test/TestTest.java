package test.com.sucre.test; 

import com.sucre.utils.RsaUtils;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.net.URLEncoder;
import java.util.Base64;

/** 
* Test Tester. 
* 
* @author <Authors name> 
* @since <pre>һ�� 20, 2019</pre> 
* @version 1.0 
*/ 
public class TestTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: test1() 
* 
*/ 
@Test
public void testTest1() throws Exception {
    String key=RsaUtils.bytesToHexString(Base64.getDecoder().decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDwOhxXyR+rpFoB7WQb/EQGc2V2JSxFNz1ACwMEdY/tcmDY21/S2xiJuOxCGNhi1zaZB/qty/Xla+TzB2zQncaCs++b/FGYBCoCumbbMl9Q7nlQSb3tCNOaNdV2gIrqdTTeJWEKLCK3iBLwWqFImAT5EtTjdpwxmr2wmx/eO5+eHQIDAQAB"));
    key=key.replace("010001","");
    //key="00F03A1C57C91FABA45A01ED641BFC4406736576252C45373D400B0304758FED7260D8DB5FD2DB1889B8EC4218D862D7369907FAADCBF5E56BE4F3076CD09DC682B3EF9BFC5198042A02BA66DB325F50EE795049BDED08D39A35D576808AEA7534DE25610A2C22B78812F05AA1489804F912D4E3769C319ABDB09B1FDE3B9F9E1D";
    System.out.println(URLEncoder.encode(RsaUtils.encryptBase64("wqwqwqwq",key)));
//TODO: Test goes here... 
} 


} 
