package com.hg.hyy.mqtt;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hyy
 * @Date: 2021/10/28
 * @email: 
 * @Description: 测试文件
 */
@Api(tags = "mqtt")
@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Autowired
    private MqttPub mqttPub;

    @ApiOperation(value = "发布主题", notes = "测试发布主题")
    @GetMapping(value = "/test")
    public String mqtt_test() {
        mqttPub.publish(0, false, "test/test", "hello mqtt this is spring");
        return "ok";
    }
}
