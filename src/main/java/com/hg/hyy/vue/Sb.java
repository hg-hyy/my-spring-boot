package com.hg.hyy.vue;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Sb {
    static char chars[] = { '个', '十', '百', '千', '万' };

    public static void sb() throws Exception {
        FileOutputStream fis = new FileOutputStream("logs\\luck.cpp");
        OutputStreamWriter osw = new OutputStreamWriter(fis);
        BufferedWriter bw = new BufferedWriter(osw);// 文件流
        bw.write("#include<iostream>\n\n\n");
        bw.write("using namespace std;\n\n\n");
        bw.write("int main(){\n");
        bw.write(" \t\tint x;\n");
        bw.write(" \t\tcout<<\"请输入一个不多于5位的正整数\";\n");
        bw.write("\t\tcin>>x;\n");
        bw.write("\t\tswitch(x){\n");
        for (int i = 1; i <= 99999; i++) {
            bw.write("\t\t\tcase " + i + ":\n");
            int t = (i + "").length();
            StringBuilder temp = new StringBuilder(String.valueOf(i));
            bw.write("\t\t\t\tcout<<\"是" + t + "位数\";\n");
            int cnt = t - 1;
            for (int j = 0; j < t; j++) {
                bw.write("\t\t\t\tcout<<\"" + chars[j] + "位数是：" + temp.charAt(cnt--) + "\";" + "\n");
            }

            bw.write("\t\t\t\tcout<<\"倒过来是：" + temp.reverse() + "\";" + "\n");
            bw.write("\t\t\t\tbreak;\n");
        }
        bw.write("\t}\n");
        bw.write("}\n");
        bw.close();
    }
}