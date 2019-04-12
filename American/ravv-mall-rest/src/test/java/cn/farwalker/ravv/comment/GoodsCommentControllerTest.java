package cn.farwalker.ravv.comment;

import cn.farwalker.ravv.BaseRestJUnitController;
import cn.farwalker.ravv.service.goodsext.comment.dao.IGoodsCommentDao;
import cn.farwalker.ravv.service.goodsext.comment.model.GoodsCommentBo;
import cn.farwalker.ravv.service.goodsext.comment.model.OrderSku;
import cn.farwalker.ravv.service.order.ordergoods.dao.IOrderGoodsDao;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;

import com.baomidou.mybatisplus.plugins.Page;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodsCommentControllerTest extends BaseRestJUnitController {
    @Autowired
    private IGoodsCommentDao iGoodsCommentDao;

    @Autowired
    private IOrderGoodsDao iOrderGoodsDao;

    @Test
    public void testComment() throws Exception{
//        Page<GoodsCommentBo> page = new Page<>(0, 10);
//        Long goodsId = Long.parseLong("1063275592839319554");
//
//        List<GoodsCommentBo> originalCommentList = iGoodsCommentDao.selectByCondition(page, goodsId, 1, false, false);
//        if(originalCommentList.size() == 0)
//            System.out.println("Kong");
//        else
//            System.out.println("not");
        OrderSku orderSku = new OrderSku();
        orderSku.setSkuId(Long.parseLong("1070133335948734475"));
        orderSku.setOrderId(Long.parseLong("1073427134577840129"));
        List<OrderSku> orderSkus = new ArrayList<>();
        orderSkus.add(orderSku);
        OrderSku orderSku1 = new OrderSku();
        orderSku1.setSkuId(Long.parseLong("1070133339115433985"));
        orderSku1.setOrderId(Long.parseLong("1073427134577840129"));
        orderSkus.add(orderSku1);
    }
}
