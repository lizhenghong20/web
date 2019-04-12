package cn.farwalker.ravv.generator;

import java.io.*;
import java.util.Date;

/**
 * Created by asus on 2018/11/24.
 */
public class SplitApi {
    public static void main(String[] args) throws Exception{
        try {
            File file = new File("F:\\ravv\\接口文档\\注册登录相关接口\\AuthController接口文档2018-11-16.md");
            file.getParent();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String fileName = "prefix";
            String s;
            StringBuffer t = new StringBuffer();
            while((s = bufferedReader.readLine())!=null){
                //在内容中
                if(!s.startsWith("########")){
                    t.append(s);
                    t.append("\n");
                    if(s.startsWith("url:")){
                        String[] tmp = s.split("/");
                        fileName = tmp[tmp.length-1];
                    }
                }else {

                    File newFile = new File(file.getParent() + File.separator + fileName);
                    FileWriter fileWriter = new FileWriter(newFile);
                    fileWriter.write(t.toString());
                    fileWriter.flush();
                    fileWriter.close();
                    t = new StringBuffer();
                    fileName = "newFile" + new Date().getTime();
                }

            }
            File newFile = new File(file.getParent()+ File.separator + fileName);
            FileWriter fileWriter = new FileWriter(newFile);
            fileWriter.write(t.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
