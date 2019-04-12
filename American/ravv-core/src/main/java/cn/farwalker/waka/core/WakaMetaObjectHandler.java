package cn.farwalker.waka.core;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;

@Component
public class WakaMetaObjectHandler extends MetaObjectHandler {
	private static final String GmtCreate="gmtCreate",GmtModified="gmtModified";
    @Override  
    public void insertFill(MetaObject metaObject) {  
        Object gmt = getFieldValByName(GmtCreate, metaObject);
        if (gmt != null) {
        	return ;
        }
    	Date d = new Date(System.currentTimeMillis());
        setFieldValByName(GmtCreate, d, metaObject);
        //同时把更新日期也更新了
        Object mdf = getFieldValByName(GmtModified, metaObject);
        if(mdf == null){
        	setFieldValByName(GmtModified,d, metaObject);  
        } 
    }  
  
    @Override  
    public void updateFill(MetaObject metaObject) {  
    	//Object mdf = getFieldValByName(GmtModified, metaObject);
        //if(mdf == null){
        	Date d = new Date(System.currentTimeMillis());
        	setFieldValByName(GmtModified,d, metaObject);  
        //} 
    }
}  