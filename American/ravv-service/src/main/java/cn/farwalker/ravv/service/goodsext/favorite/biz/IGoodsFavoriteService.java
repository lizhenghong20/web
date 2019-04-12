package cn.farwalker.ravv.service.goodsext.favorite.biz;

import cn.farwalker.ravv.service.goodsext.favorite.model.FavoriteFilterVo;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteBo;
import cn.farwalker.ravv.service.goodsext.favorite.model.GoodsFavoriteVo;

import java.util.List;

public interface IGoodsFavoriteService {

    public boolean addFavorite(Long memberId, Long goodsId);

    public String addFavoriteBatch(Long memberId, String goodsIds);

    public String deleteFavorite(Long memberId, Long goodsId);

    public String deleteAllFavorite(Long memberId);

    public List<GoodsFavoriteVo> getAllFavorite(Long memberId, boolean isReduction);

    public String deleteBatchFavorite(Long memberId, String goodsIds);

    public List<FavoriteFilterVo> getFilter(Long memberId, String filter);

    public List<GoodsFavoriteVo> getByInventoryFilter(Long memberId, String filter);

    public List<GoodsFavoriteVo> getByMenuFilter(Long memberId, Long menuId);

}
