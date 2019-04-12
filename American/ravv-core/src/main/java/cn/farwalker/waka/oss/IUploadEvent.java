package cn.farwalker.waka.oss;

import java.io.File;

/** 上传事件*/
public interface IUploadEvent{
    /** 上传进度
     * 
     * @param fs 正在上传的文件
     * @param uploadedSize 已上传的byte
     * @param totalSize 总大小
     * @param speed 速度(btye每秒)
     * @return 是否继续(true继续，false：终止)
     */
    public boolean progress(File fs, long uploadedSize, long totalSize, long speed);
    
    public boolean isCancel();
    
    public void cancel();
    
    public void next();
}