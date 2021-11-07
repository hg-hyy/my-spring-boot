package com.hg.hyy;

import java.util.HashMap;

import com.hg.hyy.entity.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;
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

		UserInfo userInfo = new UserInfo();
		userInfo.setFemale(true);
		userInfo.setHobbies(new String[] { "yoga", "swimming" });
		userInfo.setDiscount(9.5);
		userInfo.setAge(26);
		userInfo.setFeatures(new HashMap<String, Integer>() {
			private static final long serialVersionUID = 1L;
			{
				put("height", 175);
				put("weight", 70);
			}
		});
		JSONObject jsonObj = new JSONObject(userInfo);
		System.out.println(jsonObj);

		System.out.println("Female: " + jsonObj.getBoolean("female"));
		System.out.println("Discount: " + jsonObj.getDouble("discount"));
		System.out.println("Age: " + jsonObj.getLong("age"));

		// 获取JSONObject类型数据
		JSONObject features = jsonObj.getJSONObject("features");
		String[] names = JSONObject.getNames(features);
		System.out.println("Features: ");
		for (int i = 0; i < names.length; i++) {
			System.out.println("\t" + features.get(names[i]));
		}

		// 获取数组类型数据
		JSONArray hobbies = jsonObj.getJSONArray("hobbies");
		System.out.println("Hobbies: ");
		for (int i = 0; i < hobbies.length(); i++) {
			System.out.println("\t" + hobbies.get(i));
		}

	}
}
