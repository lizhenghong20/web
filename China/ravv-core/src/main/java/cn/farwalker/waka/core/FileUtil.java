package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/27.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileUtil extends FileUtils {
    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    public FileUtil() {
    }

    public static byte[] toByteArray(String filename) {
        File f = new File(filename);
        if(!f.exists()) {
            log.error("文件未找到！" + filename);
            throw new WakaException(RavvExceptionEnum.FILE_NOT_FOUND);
        } else {
            FileChannel channel = null;
            FileInputStream fs = null;

            try {
                fs = new FileInputStream(f);
                channel = fs.getChannel();
                ByteBuffer e = ByteBuffer.allocate((int)channel.size());

                while(channel.read(e) > 0) {
                    ;
                }

                byte[] var6 = e.array();
                return var6;
            } catch (IOException var16) {
                throw new WakaException(RavvExceptionEnum.FILE_READING_ERROR);
            } finally {
                try {
                    channel.close();
                } catch (IOException var15) {
                    throw new WakaException(RavvExceptionEnum.FILE_READING_ERROR);
                }

                try {
                    fs.close();
                } catch (IOException var14) {
                    throw new WakaException(RavvExceptionEnum.FILE_READING_ERROR);
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if(dir.isDirectory()) {
            String[] children = dir.list();

            for(int i = 0; i < children.length; ++i) {
                boolean success = deleteDir(new File(dir, children[i]));
                if(!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}

