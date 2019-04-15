package cn.farwalker.ravv.service.member.pam.wechat.model;
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
import cn.farwalker.ravv.service.member.pam.wechat.biz.IPamWechatMemberBiz;

/**
 * 微信用户
 * 
 * @author generateModel.java
 */
@TableName(PamWechatMemberBo.TABLE_NAME)
@DDLTable(name=PamWechatMemberBo.TABLE_NAME,comment="微信用户")
public class PamWechatMemberBo extends Model<PamWechatMemberBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        id("id"),
        memberId("member_id"),
        openId("open_id"),
        nickname("nickname"),
        unionId("union_id"),
        headImgUrl("head_img_url"),
        phone("phone"),
        sex("sex"),
        gmtCreate("gmt_create"),
        
        gmtModified("gmt_modified"),
        ramark("ramark");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:pam_wechat_member*/
    public static final String TABLE_NAME = "pam_wechat_member";
    private static final long serialVersionUID = -563201687L;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="主键",length=20)
    private Long id;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="用户id",length=20)
    private Long memberId;

    @TableField("open_id")
    @DDLColumn(name="open_id",comment="普通用户标识")
    private String openId;

    @TableField("nickname")
    @DDLColumn(name="nickname",comment="用户昵称",length=255)
    private String nickname;

    @TableField("union_id")
    @DDLColumn(name="union_id",comment="用户统一标识")
    private String unionId;

    @TableField("head_img_url")
    @DDLColumn(name="head_img_url",comment="用户头像",length=255)
    private String headImgUrl;

    @TableField("phone")
    @DDLColumn(name="phone",comment="手机号",length=20)
    private String phone;

    @TableField("sex")
    @DDLColumn(name="sex",comment="姓名",length=11)
    private String sex;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建时间")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改时间")
    private Date gmtModified;

    @TableField("ramark")
    @DDLColumn(name="ramark",comment="备注",length=2000)
    private String ramark;
    /** 主键*/
    public Long getId(){
        return id;
    }
    /** 主键*/
    public void setId(Long id){
        this.id =id;
    }
    /** 用户id*/
    public Long getMemberId(){
        return memberId;
    }
    /** 用户id*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 普通用户标识*/
    public String getOpenId(){
        return openId;
    }
    /** 普通用户标识*/
    public void setOpenId(String openId){
        this.openId =openId;
    }
    /** 用户昵称*/
    public String getNickname(){
        return nickname;
    }
    /** 用户昵称*/
    public void setNickname(String nickname){
        this.nickname =nickname;
    }
    /** 用户统一标识*/
    public String getUnionId(){
        return unionId;
    }
    /** 用户统一标识*/
    public void setUnionId(String unionId){
        this.unionId =unionId;
    }
    /** 用户头像*/
    public String getHeadImgUrl(){
        return headImgUrl;
    }
    /** 用户头像*/
    public void setHeadImgUrl(String headImgUrl){
        this.headImgUrl =headImgUrl;
    }
    /** 手机号*/
    public String getPhone(){
        return phone;
    }
    /** 手机号*/
    public void setPhone(String phone){
        this.phone =phone;
    }
    /** 姓名*/
    public String getSex(){
        return sex;
    }
    /** 姓名*/
    public void setSex(String sex){
        this.sex =sex;
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
    public String getRamark(){
        return ramark;
    }
    /** 备注*/
    public void setRamark(String ramark){
        this.ramark =ramark;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}