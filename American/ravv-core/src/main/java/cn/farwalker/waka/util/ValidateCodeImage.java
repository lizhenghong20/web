package cn.farwalker.waka.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 验证码工具类
 *
 * @author Administrator
 */
class ValidateCodeImage {
	public static final ValidateCodeImage util = new ValidateCodeImage(); 
    
    /** 因为类必须为public，所以只能把构造函数给这样控制 */
	private  ValidateCodeImage	() { 
    }
    public static void main(String[] args) { 
        util.createValidateImageFile("c:/vcode.jpg");
    }

    /**
     * 随机创建验证码图片文件
     *
     * @return
     */
    public String createValidateImageFile(String filename) {
        OutputStream out = null;
        try {
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            out = fos;
            String v = createValidateImage(out, 5);
            System.out.println(v);
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeOS(out);
        }
    }

    /**
     * 创建验证码图片
     *
     * @param os      输出流
     * @param vlength 验证码长度
     * @return 返回验证码
     */
    public String createValidateImage(OutputStream os, int vlength) throws IOException {
        // 取随机数
        char[] vcode = new char[vlength];
        Random r = new Random(System.currentTimeMillis());
        // ASC: 0=48,9=57,A=65,Z=90,a=97,z=122
        for (int i = 0; i < vlength; i++) {
            int c = r.nextInt(62);
            if (c < 10)
                vcode[i] = (char) (48 + c);// 数字
            else if (c < 36)
                vcode[i] = (char) (65 - 10 + c);// 大写字母
            else
                vcode[i] = (char) (97 - 36 + c);// 大写字母
        }
        createValidateImage(os, vcode);
        return new String(vcode);
    }

    /**
     * 创建验证码图片
     *
     * @param os    输出流
     * @param vcode 验证码
     * @return 返回验证码
     */
    public void createValidateImage(OutputStream os, char[] vcode) throws IOException {
        try {  
            int random = (int)(Math.random() * 10);
            final int fontSize = 14, WIDTH=fontSize * vcode.length  + random,HEIGHT= fontSize + 4   + random/2; 
            
            Font mFont = new Font("宋体", Font.BOLD, fontSize); // 设置字体
            BufferedImage image = new BufferedImage(WIDTH + 1, HEIGHT + 1, BufferedImage.TYPE_INT_RGB);
            Graphics gra = image.getGraphics();

            gra.setColor(Color.yellow);// 设置背景色
            gra.fillRect(1, 1, WIDTH - 1, HEIGHT - 1);
            gra.setColor(Color.black);// 设置字体色
            gra.setFont(mFont);

            for (int i = 0; i < vcode.length; i++) {
                String s = String.valueOf(vcode[i]); // 输出验证码
                gra.drawString(s, i * fontSize + 4 + random , fontSize+ random/2); // 7为宽度，11为上下高度位置
            } 
            
            //画随机线 
            int lineCount = random / 2;
            if(lineCount <2 ) {
                lineCount =2;
            }
            gra.setColor(new Color(168 - random * 3, 168 + random*4, 168 - random*5));// 设置字体色
            for(int i =0;i < lineCount;i++){
                int r = (int)(Math.random() * 10);
                int x1 = r + lineCount  ,y1 = (HEIGHT * r /10 + lineCount /2);
                int x2 = WIDTH - r ,y2 = (HEIGHT - y1) ;
                if(x2<=x1) {
                    x2 = WIDTH - random;
                }
                if(y2 <= y1){
                    y2 = HEIGHT -random;
                }
                gra.drawLine(x1, y1, x2, y2);
            } 
            
            ////////////////////////////////////////
            Tools.image.writeAsJPEG(os, image);

        } finally {
            closeOS(os);
        }
    }

    private void closeOS(OutputStream stream) {
        if (stream == null)
            return;
        try {
            stream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
