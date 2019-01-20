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
    String key=RsaUtils.bytesToHexString(Base64.getDecoder().decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZkTC1QLUW049STzywCx3S+f66YctA8wJXD3H8rNy4C0QmbWAewjyMKR0vCNsTy1iFsq+9ZJtNvYWtXg6rb5NyBvtw+jnQEb9oSsfgLQkQtZQMN95UY3UvRIbCWOQOqrXcRqe9+RPaj1i8/B6nDvOY9whS4mOOQ6McWYqmTGf3jwIDAQAB"));
    key=key.replace("010001","");
    key="00BFEA4E01F3B39B11892D40FC626C4EB07CB864C893A2DEBB87405BF87B242C7DFA8EBEE1AB37EBB6D9839FCAB42F61AA539875C52E36594BE640ACDC7DD53BBEF8BC8316BE1FBBAC07D0F83EFA88EE71FB2AF2FD3692DA6DE25BDE08FA0C77F22DA772749A8B6793582E596923EEE7234FF67A91D380DEA95739A7299346328F";
    System.out.println(URLEncoder.encode(RsaUtils.encryptBase64("wqwqwqwq",key)));
//TODO: Test goes here... 
} 


} 
