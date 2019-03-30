import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class TestJunit5 {
	String str1,str2,str3,str4,str5;
	@Before public void beforeEachTest(){
		str1 = new String("abc");
		str2 = new String("abc");
		str3 = null;
		str4 = "abc";
		str5 = "abd";
			
	}

	@Test
	public void testAssertion1() {
		assertEquals(str1, str2);
	}
	@Test public void testAssertion2(){
		assertNotNull(str1);
	}
	@Test public void testAssertion3(){
		assertNull(str3);
	}
	@Test public void testAssertion4(){
		assertSame(str4, str5);
	}
	@Test public void testAssertion5(){
		assertNotSame(str1, str3);
	}
	
	
	
	

}
