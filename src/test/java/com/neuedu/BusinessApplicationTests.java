package com.neuedu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BusinessApplicationTests {

	@Test
	public void contextLoads() {
		float a=0.02f;
		float b=0.03f;
		System.out.println(a+b);
	}

}
