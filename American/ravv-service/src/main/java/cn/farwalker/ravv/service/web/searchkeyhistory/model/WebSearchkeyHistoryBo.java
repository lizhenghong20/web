package cn.farwalker.ravv.service.web.searchkeyhistory.model;
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
import cn.farwalker.ravv.service.web.searchkeyhistory.biz.IWebSearchkeyHistoryBiz;
import cn.farwalker.ravv.service.web.searchkeyhistory.constants.SearchTypeEnum;

/**
 * 搜索关键字历史
 * 
 * @author generateModel.java
 */
@TableName(WebSearchkeyHistoryBo.TABLE_NAME)
@DDLTable(name=WebSearchkeyHistoryBo.TABLE_NAME,comment="搜索关键字历史")
public class WebSearchkeyHistoryBo extends Model<WebSearchkeyHistoryBo> implements BaseBo{
     /** 字段名枚举 */
    public static enum Key implements IFieldKey{
        gmtCreate("gmt_create"),
        gmtModified("gmt_modified"),
        id("id"),
        memberId("member_id"),
        searchCount("search_count"),
        searchType("search_type"),
        word("word");
        private final String column;
        private Key(String k){
            this.column = k;
        }
        @Override
        public String toString(){
            return column;
        }
    }
    /** 数据表名:web_searchkey_history*/
    public static final String TABLE_NAME = "web_searchkey_history";
    private static final long serialVersionUID = -120215458L;

    @TableField(value="gmt_create",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT)
    @DDLColumn(name="gmt_create",comment="创建日期")
    private Date gmtCreate;

    @TableField(value="gmt_modified",strategy=FieldStrategy.NOT_EMPTY,fill=FieldFill.INSERT_UPDATE)
    @DDLColumn(name="gmt_modified",comment="修改日期")
    private Date gmtModified;

    @TableId(value="id",type=IdType.ID_WORKER)
    @DDLColumn(name="id",comment="")
    private Long id;

    @TableField("member_id")
    @DDLColumn(name="member_id",comment="会员id")
    private Long memberId;

    @TableField("search_count")
    @DDLColumn(name="search_count",comment="搜索次数")
    private Integer searchCount;

    @TableField("search_type")
    @DDLColumn(name="search_type",comment="搜索类型",length=63)
    private SearchTypeEnum searchType;

    @TableField("word")
    @DDLColumn(name="word",comment="搜索文字",length=127)
    private String word;
    /** 创建日期*/
    public Date getGmtCreate(){
        return gmtCreate;
    }
    /** 创建日期*/
    public void setGmtCreate(Date gmtCreate){
        this.gmtCreate =gmtCreate;
    }
    /** 修改日期*/
    public Date getGmtModified(){
        return gmtModified;
    }
    /** 修改日期*/
    public void setGmtModified(Date gmtModified){
        this.gmtModified =gmtModified;
    }
    /** null*/
    public Long getId(){
        return id;
    }
    /** null*/
    public void setId(Long id){
        this.id =id;
    }
    /** 会员id*/
    public Long getMemberId(){
        return memberId;
    }
    /** 会员id*/
    public void setMemberId(Long memberId){
        this.memberId =memberId;
    }
    /** 搜索次数*/
    public Integer getSearchCount(){
        return searchCount;
    }
    /** 搜索次数*/
    public void setSearchCount(Integer searchCount){
        this.searchCount =searchCount;
    }
    /** 搜索类型*/
    public SearchTypeEnum getSearchType(){
        return searchType;
    }
    /** 搜索类型*/
    public void setSearchType(SearchTypeEnum searchType){
        this.searchType =searchType;
    }
    /** 搜索文字*/
    public String getWord(){
        return word;
    }
    /** 搜索文字*/
    public void setWord(String word){
        this.word =word;
    }
    @Override
    protected Serializable pkVal(){
        return id;
    }
}