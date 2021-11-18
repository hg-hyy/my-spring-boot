package com.hg.hyy.controller;

import com.hg.hyy.entity.Sb;
import com.hg.hyy.pojo.Receiver;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "test")
@RestController
@RequestMapping("/test")
public class TsetController {
  private static final Logger log = LoggerFactory.getLogger(VueController.class);

  @ApiOperation("88万行代码")
  @GetMapping("/sb")
  public String sb() {
    try {
      Sb.sb();
    } catch (Exception e) {

      e.printStackTrace();
    }
    return "ok";
  }

  @ApiOperation("测试日志")
  @GetMapping("/log")
  public String log() {

    log.error("我是一条error日志");
    return "log is log";
  }

  private Receiver receiver;
  private StringRedisTemplate template;

  @Autowired
  public void setTemplate(StringRedisTemplate template) {
    this.template = template;
  }

  @Autowired
  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @ApiOperation("测试redis")
  @GetMapping("/redis")
  public void redis() throws InterruptedException {

    while (receiver.getCount() == 0) {

      log.error("Sending message...");
      template.convertAndSend("chat", "Hello from Redis!");
      Thread.sleep(500L);
    }
  }
}
