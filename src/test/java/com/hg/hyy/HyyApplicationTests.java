package com.hg.hyy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class HyyApplicationTests {

	@Test
	public void contextLoads() {
		PasswordEncoder pw = new BCryptPasswordEncoder();
		String s = pw.encode("123");
		System.out.println(s);

		Boolean ma = pw.matches("123", s);
		System.out.println(ma);
	}

}
