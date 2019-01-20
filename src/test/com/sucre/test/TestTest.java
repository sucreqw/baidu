package test.com.sucre.test; 

import com.sucre.utils.RsaUtils;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

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
    System.out.println(RsaUtils.encryptBase64("test","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0bMtdmD8cAjUXMjYTsjbl2uSgBoVYkyZxm4neHDT5QqLPf+VJvx0KDlxNG3PlGocUJFF0mkpa565G1poV4ovbuyXgLcEqNrg9jqGrM3KfxDAYhEbqNIusT+2RTvI1dNCEdu54aeRjAONJ02xKkP9+8E1n4fFxb988SoDnAmNj8wIDAQAB"));
//TODO: Test goes here... 
} 


} 
