package cn.farwalker.ravv.service.member.basememeber.model;

import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorBo;
import cn.farwalker.ravv.service.youtube.liveanchor.model.YoutubeLiveAnchorVo;
import lombok.Data;

/**
 * @description: 附加属性的扩展，例如收藏商品数量，关注主播数量，最近浏览量
 * @param:
 * @return
 * @author Mr.Simple
 * @date 2019/1/8 11:10
 */
@Data
public class MemberExVo extends MemberBo{
    boolean isBirth;
    int favoriteCount = 0;
    int followingCount = 0;
    int viewCount = 0;
}
