package com.hg.hyy.speech;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.baidu.aip.speech.AipSpeech;

import org.json.JSONObject;

public class Sample {
    // 设置APPID/AK/SK
    private static final String APP_ID = "25121464";
    private static final String API_KEY = "ewwvGklAY8OtymvjFtds1Oxu";
    private static final String SECRET_KEY = "M49P9iOrrwQVxgdiLTqOAgK2t5v4bmR5";

    public static void testSpeech() {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        // client.setHttpProxy("proxy_host", proxy_port); // 设置http代理
        // client.setSocketProxy("proxy_host", proxy_port); // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        // System.setProperty("aip.log4j.conf", "src/main/resources/log4j.properties");

        // 调用接口
        JSONObject res = client.asr("src/main/resources/result.wav", "pcm", 16000, null);
        System.out.println(res.toString(2));
        // asr(client);

    }

    @SuppressWarnings("unused")
    private static void asr(AipSpeech client) {
        // 对本地语音文件进行识别
        String path = "src/main/resources/16k.pcm";
        String path1 = "src/main/resources/result.wav";
        JSONObject asrRes = client.asr(path, "pcm", 16000, null);
        System.out.println(asrRes.toString(2));

        // 对语音二进制数据进行识别
        byte[] data = readFileByBytes(path1);
        JSONObject asrRes2 = client.asr(data, "pcm", 16000, null);
        System.out.println(asrRes2.toString(2));

    }

    // readFileByBytes仅为获取二进制数据示例
    private static byte[] readFileByBytes(String path) {

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(path));
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = bis.read(b)) != -1) {
                System.out.print(new String(b, 0, len));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;

    }
}
