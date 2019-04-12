package cn.farwalker.waka.util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

/**
 * 图片工具类
 *
 * @author Administrator
 */
public class ImageUtil {
	public static final ImageUtil util = new ImageUtil(); 
    
    /** 因为类必须为public，所以只能把构造函数给这样控制 */
	private  ImageUtil() {

    }  

    private final ValidateCodeImage validCodeImage = ValidateCodeImage.util;

    /**
     * 把图片放大或缩小到指定尺寸,只支持JPG
     *
     * @param imagefile 图片文件
     * @param width     指定宽度
     * @param height    指定高度
     * @param type      0:无论如何都把图片放大或缩小到指定尺寸,1:原始尺寸比指定尺寸小则保持,2:原始尺寸比指定尺寸大则保持
     * @author 2007-7-30 21:31:46
     */
    public String imageToScale(String imagefile, int width, int height, int type) throws IOException {
        File in = new File(imagefile);

        String fname = in.getName();
        String result;
        int index = fname.indexOf('.');

        if (index > 0) {
            String ext = fname.substring(index + 1);
            result = in.getParent() + "/" + fname.substring(0, index) + "2." + ext;
            ext = fname.substring(index + 1);
        } else
            result = in.getPath() + "." + fname;
        File out = new File(result);

        AffineTransform transform = new AffineTransform();
        BufferedImage srcImg = ImageIO.read(in);

        int imgw = srcImg.getWidth(), imgh = srcImg.getHeight();
        switch (type) {
            case 1: // 原始尺寸比指定尺寸小则保持
                if (imgw < width)
                    width = imgw;
                if (imgh < height)
                    height = imgh;
            case 2:// 2:原始尺寸比指定尺寸大则保持
                if (imgw > width)
                    width = imgw;
                if (imgh > height)
                    height = imgh;
        }
        if (width == imgw && height == imgh)
            return imagefile;

        double w = width / (double) imgw;
        double h = height / (double) imgh;
        transform.setToScale(w, h);
        AffineTransformOp middleAffine = new AffineTransformOp(transform, null);
        BufferedImage img = new BufferedImage(width, height, 5);
        middleAffine.filter(srcImg, img);

        ImageIO.write(img, "jpg", out);
        return result;
    }

    public String createValidateImage(OutputStream os, int vlength) {
        try {
            return validCodeImage.createValidateImage(os, vlength);
        } catch (IOException e) {
            throw new YMException(e.getMessage(), e);
        }
    }

    public void createValidateImage(OutputStream os, char[] vcode) {
        try {
            validCodeImage.createValidateImage(os, vcode);
        } catch (IOException e) {
            throw new YMException(e.getMessage(), e);
        }
    }

    public void writeAsJPEG(final OutputStream os, final BufferedImage image) throws IOException {
        ImageWriter imageWriter = ImageIO.getImageWritersBySuffix("jpeg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        imageWriter.setOutput(ios);

        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);

        // new Compression
        JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
        jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
        jpegParams.setCompressionQuality(0.7F);

        // new Write and clean up
        imageWriter.write(imageMetaData, new IIOImage(image, null, null), null);
        ios.close();
        imageWriter.dispose();
    }

    /**
     * 将图片转换成Base64编码
     * @param imgFile 待处理图片
     * @return
     */
    public String getImageBase64(String imgFile) throws IOException {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream is = null;
        try {
            is = new FileInputStream(imgFile);
            String rs = getImageBase64(is);
            return rs;
        }
        finally{
            Tools.io.close(is);
        }
        
        
    }
    /**
     * 将图片转换成Base64编码(编码不包括 "data:image/jpeg;base64," 、"data:image/png;base64,")等
     * @param imgFile 待处理图片
     * @return
     */
    public String getImageBase64(InputStream is) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[1024 * 16];
        int l ;
        
        while((l = is.read(buf))!=-1){
            os.write(buf, 0, l);
        }
        
        //FileOutputStream fos = new FileOutputStream("d:/temp/a.jpg");
        //fos.write(os.toByteArray());
        //fos.close();
        
        byte[] bye =Tools.encode.base64Encode(os.toByteArray());
        String rs = new String(bye);
        return rs;
    }
    /**
     * 对字节数组字符串进行Base64解码并生成图片
     * @param imgStr 图片数据
     * @param imgFilePath 保存图片全路径地址
     * @return
     */
    public byte[] getImageForBase64(String imageBase64) {
        if (Tools.string.isEmpty(imageBase64)){
            return null; //图像数据为空
        }
        byte[] bye = imageBase64.getBytes();
        byte[] buf = Tools.encode.base64Decode(bye);
        return buf; 
    }
}
