package cn.farwalker.ravv.service.youtube.liveanchor.dao;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * YouTube 主播表<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IYoutubeLiveAnchorDao extends BaseMapper<YoutubeLiveAnchorBo>{
    //根据anchorIdList选出List
    public List<YoutubeLiveAnchorVo> selectAnchorList(Page page, @Param(value = "currentTime")Date currentTime,
                                                                    @Param(value = "anchorIdList")List anchorIdList);

}