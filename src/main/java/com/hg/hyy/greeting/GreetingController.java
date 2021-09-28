package com.hg.hyy.greeting;

import java.util.concurrent.atomic.AtomicLong;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private static final Logger log = LoggerFactory.getLogger(GreetingController.class);

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		log.info("-------------------------------------");
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
}