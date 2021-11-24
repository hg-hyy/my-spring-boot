package com.hg.hyy.controller;

import com.hg.hyy.entity.Msg;
import com.hg.hyy.entity.Sb;
import com.hg.hyy.influxDB.InfluxDB2Example;
import com.hg.hyy.pojo.Receiver;
import com.hg.hyy.service.MyTestInterfaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

  private MyTestInterfaceService myTestInterface;

  @Autowired
  public void setMyTestInterface(MyTestInterfaceService myTestInterface) {
    this.myTestInterface = myTestInterface;
  }

  private Msg msg;

  @Autowired
  public void setMsg(Msg msg) {
    this.msg = msg;
  }

  @ApiOperation("测试自动装配")
  @GetMapping("/auto")
  public Msg testAutoWire() {
    myTestInterface.autowired();
    msg.setMsg("测试自动装配");
    msg.setCode(1000);
    msg.setData("测试自动装配成功");
    return msg;
  }

  private InfluxDB2Example influxDB2Example;

  @Autowired
  public void setInfluxDB2Example(InfluxDB2Example influxDB2Example) {
    this.influxDB2Example = influxDB2Example;
  }

  @ApiOperation("测试influxdb")
  @GetMapping("/influxdb")
  public String testInfluxdb() {
    influxDB2Example.writeData();
    //    influxDB2Example.readData();
    return "OK";
  }

  private RestTemplate restTemplate;

  @Autowired
  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @ApiOperation("测试influxdb delete")
  @GetMapping("/delete")
  public String delInfluxdb() {

    //    HttpHeaders headers = new HttpHeaders();
    //    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
    //    headers.setContentType(type);
    //    headers.add("Accept", MediaType.APPLICATION_JSON.toString());
    //    JSONObject jsonObj = JSONObject.fromObject(params);
    //    HttpEntity<String> formEntity = new HttpEntity<String>(jsonObj.toString(), headers);
    //    String result = restTemplate.postForObject(url, formEntity, String.class);

    String baseUrl = "http://192.168.118.143:30086";
    String token =
        "2QgWrkVnl8OapyKkMxZ3YNhxQxkJfflvuyWdfRE3NO-kEyeIiD6u8sWh9GONMGvyI4wH2ZbQaV8j4I2Sno5ZoA==";
    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
    factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES);
    restTemplate.setUriTemplateHandler(factory);

    // request body
    Map<String, String> param1 = new HashMap<>();
    param1.put("start", "2021-11-24T00:00:00Z");
    param1.put("stop", "2021-11-25T00:00:00Z");

    // 设置header
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Token " + token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    // 过滤掉账号认证失败的时候抛出的401异常
    restTemplate.setErrorHandler(
        new DefaultResponseErrorHandler() {
          @Override
          public void handleError(@NotNull ClientHttpResponse response) throws IOException {
            if (response.getRawStatusCode() != 401) {
              super.handleError(response);
            }
          }
        });

    HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(param1, headers);
    ResponseEntity<String> responseEntity =
        restTemplate.postForEntity("/api/v2/delete?org=hG&bucket=hg", httpEntity, String.class);

    // parse response
    if (responseEntity.getStatusCode().is2xxSuccessful()) {
      log.error("success:{}", responseEntity.getBody());
    } else {
      log.error(
          "error,statusCode:{},return:{}",
          responseEntity.getStatusCode().value(),
          responseEntity.getBody());
    }
    return "OK";
  }
}
