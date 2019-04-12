package cn.farwalker.ravv.service.youtube.livegoods.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import com.cangwu.frame.orm.ddl.annotation.DDLColumn;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by asus on 2019/1/3.
 */
@Data
public class AddGoodsToVideoForm implements Serializable{

    private String description;


    private String previewDefaultUrl;


    private String previewHighUrl;


    private String previewMediumUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;


    private String title;

    private String youtubeVideoId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date endTime;
    String goodIds;
}
