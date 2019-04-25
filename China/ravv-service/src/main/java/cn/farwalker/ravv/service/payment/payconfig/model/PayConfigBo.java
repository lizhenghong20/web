package cn.farwalker.ravv.service.payment.payconfig.model;
import java.util.Date;
import java.util.List;
import com.cangwu.frame.orm.core.BaseBo;
import com.cangwu.frame.orm.core.annotation.LoadJoinValue;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import com.cangwu.frame.orm.ddl.annotation.DDLTable;
import com.cangwu.frame.orm.core.IFieldKey;
import java.io.Serializable;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import cn.farwalker.ravv.service.payment.payconfig.biz.IPayConfigBiz;

/**
 * 支付配置
 * 
 * @author generateModel.java
 */
@TableName(PayConfigBo.TABLE_NAME)
@DDLTable(name=PayConfigBo.TABLE_NAME,comment="支付配置")
public class PayConfigBo extends Model<PayConfigBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        payType("pay_type"),
        mchId("mch_id"),
        apiKey("api_key"),
        appId("app_id"),
        appSecret("app_secret"),
        notifyUrl("notify_url"),
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        
        remark("remark");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:pay_config*/
    public static final String TABLE_NAME = "pay_config";
    private static final long serialVersionUID = 216684503L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键",length=20)
    private Long id;

    @TableField("pay_type")
    @DDLColumn(name="pay_type",comment="支付方式",length=32)
    private String payType;

    @TableField("mch_id")
    @DDLColumn(name="mch_id",comment="商户号",length=255)
    private String mchId;

    @TableField("api_key")
    @DDLColumn(name="api_key",comment="密钥",length=255)
    private String apiKey;

    @TableField("app_id")
    @DDLColumn(name="app_id",comment="appId")
    private String appId;

    @TableField("app_secret")
    @DDLColumn(name="app_secret",comment="appsecret")
    private String appSecret;

    @TableField("notify_url")
    @DDLColumn(name="notify_url",comment="回调地址",length=255)
    private String notifyUrl;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("remark")
    @DDLColumn(name="remark",comment="备注",length=2000)
    private String remark;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 支付方式*/
    public String getPayType(){
        return payType;
    }
    /** 支付方式*/
    public void setPayType(String payType){
        this.payType =payType;
    }
    /** 商户号*/
    public String getMchId(){
        return mchId;
    }
    /** 商户号*/
    public void setMchId(String mchId){
        this.mchId =mchId;
    }
    /** 密钥*/
    public String getApiKey(){
        return apiKey;
    }
    /** 密钥*/
    public void setApiKey(String apiKey){
        this.apiKey =apiKey;
    }
    /** appId*/
    public String getAppId(){
        return appId;
    }
    /** appId*/
    public void setAppId(String appId){
        this.appId =appId;
    }
    /** appsecret*/
    public String getAppSecret(){
        return appSecret;
    }
    /** appsecret*/
    public void setAppSecret(String appSecret){
        this.appSecret =appSecret;
    }
    /** 回调地址*/
    public String getNotifyUrl(){
        return notifyUrl;
    }
    /** 回调地址*/
    public void setNotifyUrl(String notifyUrl){
        this.notifyUrl =notifyUrl;
    }
    /** 创建时间*/
    public Date getGmtCreate(){
        return gmtCreate;
    }
    /** 创建时间*/
    public void setGmtCreate(Date gmtCreate){
        this.gmtCreate =gmtCreate;
    }
    /** 修改时间*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 修改时间*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** 备注*/
    public String getRemark(){
        return remark;
    }
    /** 备注*/
    public void setRemark(String remark){
        this.remark =remark;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}