package cn.farwalker.waka.config.properties;

import cn.farwalker.waka.util.Tools;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 项目配置
 *
 * @author Jason Chen
 * @Date 2017/5/23 22:31
 */
@Component
@ConfigurationProperties(prefix = WakaProperties.PREFIX)
public class WakaProperties {

    public static final String PREFIX = "waka";
    
    private boolean authOpen = true;

    private boolean signOpen = true;

    private Boolean kaptchaOpen = false;

    private Boolean swaggerOpen = false;

    private String fileUploadPath;
    
    private String restDomainPath;

    private Boolean haveCreatePath = false;

    private Boolean springSessionOpen = false;

    private Integer sessionInvalidateTime = 30 * 60;  //session 失效时间（默认为30分钟 单位：秒）

    private Integer sessionValidationInterval = 15 * 60;  //session 验证失效时间（默认为15分钟 单位：秒）
    /**
     * 废弃,参考 GoodsUtil
     */ 
    public String getFileUploadPath() {
        //如果没有写文件上传路径,保存到临时目录
        if (Tools.string.isEmpty(fileUploadPath)) {
            return getTempPath();
        } else {
            //判断有没有结尾符,没有得加上
            if (!fileUploadPath.endsWith(File.separator)) {
                fileUploadPath = fileUploadPath + File.separator;
            }
            //判断目录存不存在,不存在得加上
            if (haveCreatePath == false) {
                File file = new File(fileUploadPath);
                file.mkdirs();
                haveCreatePath = true;
            }
            return fileUploadPath;
        }
    }
    /**
     * 废弃,参考 GoodsUtil
     * @deprecated
     * @param imageName
     * @return
     */
    public String getRestImageFullPath(String imageName) {
    	return this.getRestDomainPath() + "file/image/" + imageName;// + ".jpg";
    }

    public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }
    

    public String getRestDomainPath() {
		return restDomainPath;
	}

	public void setRestDomainPath(String restDomainPath) {
		this.restDomainPath = restDomainPath;
	}

	public boolean isAuthOpen() {
		return authOpen;
	}

	public void setAuthOpen(boolean authOpen) {
		this.authOpen = authOpen;
	}

	public boolean isSignOpen() {
		return signOpen;
	}

	public void setSignOpen(boolean signOpen) {
		this.signOpen = signOpen;
	}

	public Boolean getKaptchaOpen() {
        return kaptchaOpen;
    }

    public void setKaptchaOpen(Boolean kaptchaOpen) {
        this.kaptchaOpen = kaptchaOpen;
    }

    public Boolean getSwaggerOpen() {
        return swaggerOpen;
    }

    public void setSwaggerOpen(Boolean swaggerOpen) {
        this.swaggerOpen = swaggerOpen;
    }

    public Boolean getSpringSessionOpen() {
        return springSessionOpen;
    }

    public void setSpringSessionOpen(Boolean springSessionOpen) {
        this.springSessionOpen = springSessionOpen;
    }

    public Integer getSessionInvalidateTime() {
        return sessionInvalidateTime;
    }

    public void setSessionInvalidateTime(Integer sessionInvalidateTime) {
        this.sessionInvalidateTime = sessionInvalidateTime;
    }

    public Integer getSessionValidationInterval() {
        return sessionValidationInterval;
    }

    public void setSessionValidationInterval(Integer sessionValidationInterval) {
        this.sessionValidationInterval = sessionValidationInterval;
    }

    public static String getTempPath() {
        return System.getProperty("java.io.tmpdir");
    }

}
