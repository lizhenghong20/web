package cn.farwalker.ravv.service.goodsext.consult.biz.impl;

import cn.farwalker.ravv.common.constants.MessageTypeEnum;
import cn.farwalker.ravv.service.goods.base.biz.IGoodsBiz;
import cn.farwalker.ravv.service.goods.base.model.GoodsBo;
import cn.farwalker.ravv.service.goods.image.biz.IGoodsImageBiz;
import cn.farwalker.ravv.service.goods.image.model.GoodsImageBo;
import cn.farwalker.ravv.service.goods.price.dao.IGoodsPriceDao;
import cn.farwalker.ravv.service.goodsext.consult.biz.IGoodsConsultBiz;
import cn.farwalker.ravv.service.goodsext.consult.biz.IGoodsConsultService;
import cn.farwalker.ravv.service.goodsext.consult.model.*;
import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.ordergoods.biz.IOrderGoodsBiz;
import cn.farwalker.ravv.service.order.ordergoods.model.OrderGoodsBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.sys.message.biz.ISystemMessageBiz;
import cn.farwalker.ravv.service.sys.message.model.SystemMessageBo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class GoodsConsultServiceImpl implements IGoodsConsultService {

    @Autowired
    private IGoodsConsultBiz iGoodsConsultBiz;

    @Autowired
    private IGoodsBiz iGoodsBiz;

    @Autowired
    private IGoodsImageBiz iGoodsImageBiz;

    @Autowired
    private IMemberBiz iMemberBiz;

    @Autowired
    private IOrderInfoBiz iOrderInfoBiz;

    @Autowired
    private IOrderGoodsBiz iOrderGoodsBiz;

    @Autowired
    private ISystemMessageBiz iSystemMessageBiz;

    @Autowired
    private IGoodsPriceDao iGoodsPriceDao;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addQuestion(GoodsConsultBo question) {
        EntityWrapper<GoodsConsultBo> queryConsult = new EntityWrapper<>();
        queryConsult.eq(GoodsConsultBo.Key.consultContent.toString(), question.getConsultContent());
        queryConsult.eq(GoodsConsultBo.Key.goodsId.toString(), question.getGoodsId());
        int count = 0;
        count = iGoodsConsultBiz.selectCount(queryConsult);
        if(count != 0)
            return "The question had already exists";
        MemberBo memberBo = new MemberBo();
        memberBo = iMemberBiz.selectById(question.getMemberId());
        String originalName = "Anonymous User";
        if(memberBo.getFirstname() != null && memberBo.getLastname() != null){
            originalName = memberBo.getFirstname() + " " + memberBo.getLastname();
        }
        //如果匿名，处理名称
        if(question.getAuthorName().equals("true")){
            originalName = Tools.string.getAnonymous(originalName);
        }
        question.setAuthorName(originalName);
        Date now = new Date();
        question.setConsultTime(now);
        question.setGmtCreate(now);
        question.setGmtModified(now);
        if(!iGoodsConsultBiz.insert(question))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        //查出购买过该商品的用户
        List<OrderGoodsBo> allOrderList = iOrderGoodsBiz.selectList(Condition.create()
                .eq(OrderGoodsBo.Key.goodsId.toString(), question.getGoodsId()));
        if(allOrderList.size() != 0){
            //获取到所有购买过该商品的用户id
            Set<Long> buyerIdSet = new HashSet<>();
            allOrderList.forEach(item->{
                //遍历订单号，查看buyerId
                OrderInfoBo orderInfoBo = new OrderInfoBo();
                orderInfoBo = iOrderInfoBiz.selectById(item.getOrderId());
                if(orderInfoBo != null)
                    buyerIdSet.add(orderInfoBo.getBuyerId());
            });
            //给这些用户推送消息
            List<Long> buyerList = new ArrayList<>(buyerIdSet);
            //根据goodsId查出主图
            EntityWrapper<GoodsImageBo> queryImage = new EntityWrapper<>();
            queryImage.eq(GoodsImageBo.Key.goodsId.toString(), question.getGoodsId());
            queryImage.eq(GoodsImageBo.Key.imgPosition.toString(), "MAJOR");
            GoodsImageBo goodsImageBo = iGoodsImageBiz.selectOne(queryImage);
            String goodsImage = goodsImageBo.getImgUrl();
            if(goodsImageBo != null){
                if(Tools.string.isNotEmpty(goodsImageBo.getImgUrl()))
                    goodsImage = goodsImageBo.getImgUrl();
                else {
                    throw new WakaException("商品图片为空" + goodsImageBo.getGoodsId());
                }
            }
            String imgae = goodsImage;

            List<SystemMessageBo> systemList = new ArrayList<>();
            buyerList.forEach(item->{
                //如果购买的用户不是提问人，就推送消息
                if(!item.equals(question.getMemberId())){
                    SystemMessageBo questionMessage = new SystemMessageBo();
                    questionMessage.setIcon(imgae);
                    questionMessage.setHaveRead(false);
                    questionMessage.setPublishMemberId(question.getMemberId());
                    //在问答类型中，content应为consultId
                    questionMessage.setConsultId(question.getId());
                    questionMessage.setRecieveMemberId(item);
                    questionMessage.setTitle("Someone is asking you for help...");
                    questionMessage.setQaType(MessageTypeEnum.QUESTION.getKey());
                    questionMessage.setType(MessageTypeEnum.CONSULT.getKey());
                    questionMessage.setPublishTime(now);
                    questionMessage.setGmtCreate(now);
                    questionMessage.setGmtModified(now);
                    systemList.add(questionMessage);
                }
            });
            if(!iSystemMessageBiz.insertBatch(systemList))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "push question message");
        }
        return "submit successful";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addAnswer(GoodsConsultBo answer) {
        //判断是否购买过该商品
        //获取所有包含此商品的订单号
        List<OrderGoodsBo> allGoodsList = iOrderGoodsBiz.selectList(Condition.create()
                                            .eq(OrderGoodsBo.Key.goodsId.toString(), answer.getGoodsId()));
        List<OrderInfoBo> allBuyerList = new ArrayList<>();
        if(allGoodsList.size() == 0)
            throw new WakaException(RavvExceptionEnum.GOODS_DATA_ERROR + "您没有购买过该商品");
        allGoodsList.forEach(item->{
            //遍历订单号，查看buyerId
            OrderInfoBo orderInfoBo = new OrderInfoBo();
            orderInfoBo = iOrderInfoBiz.selectById(item.getOrderId());
            if(orderInfoBo == null)
                throw new WakaException("order_info订单被删除,order_goods表里关联订单未删除");
            // 订单完成之后才可评价
            if(orderInfoBo.getOrderStatus().equals(OrderStatusEnum.SING_GOODS)
                    || orderInfoBo.getOrderStatus().equals(OrderStatusEnum.TRADE_CLOSE)){
                if(orderInfoBo.getBuyerId().equals(answer.getMemberId()))
                    allBuyerList.add(orderInfoBo);
            }
        });
        if(allBuyerList.size() == 0){
            throw new WakaException(RavvExceptionEnum.GOODS_DATA_ERROR + "您没有购买过该商品");
        }
        //判断该问题是否存在
        GoodsConsultBo question = iGoodsConsultBiz.selectById(answer.getReplyId());
        if(question == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        if(question.getMemberId().equals(answer.getMemberId()))
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "不能回复自己");
        //查询是否已回答过该问题
        GoodsConsultBo myAnswer = new GoodsConsultBo();
        myAnswer = iGoodsConsultBiz.selectOne(Condition.create().eq(GoodsConsultBo.Key.replyId.toString(),answer.getReplyId())
                                                .eq(GoodsConsultBo.Key.memberId.toString(), answer.getMemberId()));
        if(myAnswer != null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR + "您已回答过该问题");
        //添加回答
        String originalName = "Anonymous User";
        MemberBo memberBo = new MemberBo();
        memberBo = iMemberBiz.selectById(answer.getMemberId());
        if(memberBo.getFirstname() != null && memberBo.getLastname() != null){
            originalName = memberBo.getFirstname() + " " + memberBo.getLastname();
        }
        //如果匿名，处理名称
        if(answer.getAuthorName().equals("true")){
            originalName = Tools.string.getAnonymous(originalName);
        }
        answer.setAuthorName(originalName);
        Date now = new Date();
        answer.setGmtModified(now);
        answer.setGmtCreate(now);
        answer.setReplyTime(now);
        if(!iGoodsConsultBiz.insert(answer))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        //推送消息给提问人
        SystemMessageBo answerMessage = new SystemMessageBo();
        //根据goodsId查出主图
        EntityWrapper<GoodsImageBo> queryImage = new EntityWrapper<>();
        queryImage.eq(GoodsImageBo.Key.goodsId.toString(), answer.getGoodsId());
        queryImage.eq(GoodsImageBo.Key.imgPosition.toString(), "MAJOR");
        GoodsImageBo goodsImageBo = new GoodsImageBo();
        goodsImageBo = iGoodsImageBiz.selectOne(queryImage);
        if(goodsImageBo != null){
            if(Tools.string.isNotEmpty(goodsImageBo.getImgUrl()))
                answerMessage.setIcon(goodsImageBo.getImgUrl());
        }

        answerMessage.setHaveRead(false);
        answerMessage.setPublishMemberId(answer.getMemberId());
        answerMessage.setConsultId(answer.getReplyId());
        answerMessage.setRecieveMemberId(question.getMemberId());
        answerMessage.setTitle("Your question received a new answer");
        answerMessage.setQaType(MessageTypeEnum.ANSWER.getKey());
        answerMessage.setType(MessageTypeEnum.CONSULT.getKey());
        answerMessage.setPublishTime(now);
        answerMessage.setGmtCreate(now);
        answerMessage.setGmtModified(now);
        if(!iSystemMessageBiz.insert(answerMessage))
            throw new WakaException(RavvExceptionEnum.INSERT_ERROR + "push answer message");
        return "submit successful";
    }

    /**
     * @description: 获取该商品的所有问答
     * @param: goodsId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/27 10:16
     */
    @Override
    public List<GoodsConsultVo> getConsultList(Long goodsId, int currentPage, int pageSize) {
        //返回的list
        List<GoodsConsultVo> goodsConsultVoList = new ArrayList<>();
        //查询出商品详情
//        GoodsBo goodsBo = iGoodsBiz.selectById(goodsId);
        //查询出该商品的问题
        Page<GoodsConsultBo> page = new Page<>(currentPage, pageSize);
        EntityWrapper<GoodsConsultBo> queryConsult = new EntityWrapper<>();
        queryConsult.eq(GoodsConsultBo.Key.goodsId.toString(), goodsId);
        queryConsult.isNull(GoodsConsultBo.Key.replyContent.toString());
        Page<GoodsConsultBo> consultBoPage = iGoodsConsultBiz.selectPage(page, queryConsult);
        List<GoodsConsultBo> consultBoList = consultBoPage.getRecords();
        if(consultBoList.size() == 0)
            return goodsConsultVoList;
        consultBoList.forEach(item->{
            GoodsConsultVo consultVo = new GoodsConsultVo();
            consultVo.setQuestion(item);
            //根据问题id查询出所有回答
            EntityWrapper<GoodsConsultBo> queryAnswer = new EntityWrapper<>();
            queryAnswer.eq(GoodsConsultBo.Key.replyId.toString(), item.getId());
            List<GoodsConsultBo> answerList = new ArrayList<>();
            answerList = iGoodsConsultBiz.selectList(queryAnswer);
            consultVo.setAnswer(answerList);
//            consultVo.setGoodsBo(goodsBo);
            goodsConsultVoList.add(consultVo);
        });


        return goodsConsultVoList;
    }

    /**
     * @description: web端搜索问题
     * @param: question
     * @return goodsConsultVo
     * @author Mr.Simple
     * @date 2018/12/27 14:27
     */
    @Override
    public List<GoodsConsultVo> searchQuestion(String question) {
        List<GoodsConsultVo> returnList = new ArrayList<>();
        //查询出问题
        EntityWrapper<GoodsConsultBo> queryConsult = new EntityWrapper<>();
        queryConsult.like(GoodsConsultBo.Key.consultContent.toString(), question);
        List<GoodsConsultBo> questionList = iGoodsConsultBiz.selectList(queryConsult);
        if(questionList.size() == 0)
            return returnList;
        questionList.forEach(item->{
            GoodsConsultVo consultVo = new GoodsConsultVo();
            consultVo.setQuestion(item);
            List<GoodsConsultBo> answerList = new ArrayList<>();
            //查询出回答
            EntityWrapper<GoodsConsultBo> queryAnswer = new EntityWrapper<>();
            queryAnswer.eq(GoodsConsultBo.Key.replyId.toString(), item.getId());
            answerList = iGoodsConsultBiz.selectList(queryAnswer);
            consultVo.setAnswer(answerList);
            returnList.add(consultVo);
        });


//        //查询出商品详情
//        GoodsBo goodsBo = iGoodsBiz.selectById(questionBo.getGoodsId());
//        consultVo.setGoodsBo(goodsBo);
        return returnList;
    }

    /**
     * @description: app端搜索问题
     * @param: question
     * @return list
     * @author Mr.Simple
     * @date 2018/12/27 16:23
     */
    @Override
    public List<GoodsQuestionVo> searchQuestionForApp(String question) {
        List<GoodsQuestionVo> questionVoList = new ArrayList<>();
        EntityWrapper<GoodsConsultBo> queryConsult = new EntityWrapper<>();
        queryConsult.like(GoodsConsultBo.Key.consultContent.toString(), question);
        List<GoodsConsultBo> questionList = iGoodsConsultBiz.selectList(queryConsult);
        if(questionList.size() == 0)
            return questionVoList;
        questionList.forEach(item->{
            GoodsQuestionVo questionVo = new GoodsQuestionVo();
            questionVo = getQuestion(item);
            questionVoList.add(questionVo);
        });
        return questionVoList;
    }

    /**
     * @description: 个人中心查看我的提问
     * @param: memberId
     * @return list
     * @author Mr.Simple
     * @date 2018/12/27 11:36
     */
    @Override
    public List<GoodsQuestionVo> myQuestion(Long memberId) {
        List<GoodsQuestionVo> questionVoList = new ArrayList<>();
        EntityWrapper<GoodsConsultBo> queryConsult = new EntityWrapper<>();
        queryConsult.eq(GoodsConsultBo.Key.memberId.toString(), memberId);
        queryConsult.isNull(GoodsConsultBo.Key.replyContent.toString());
        List<GoodsConsultBo> questionList = iGoodsConsultBiz.selectList(queryConsult);
        if(questionList.size() == 0)
            return questionVoList;
        questionList.forEach(item->{
            GoodsQuestionVo questionVo = new GoodsQuestionVo();
            questionVo = getQuestion(item);

            questionVoList.add(questionVo);
        });
        return questionVoList;
    }

    /**
     * @description: 问答详情
     * @param: consultId
     * @return goodsQaVo
     * @author Mr.Simple
     * @date 2018/12/27 16:15
     */
    @Override
    public GoodsQaVo questionDetail(Long memberId, Long consultId) {
        GoodsQaVo qaVo = new GoodsQaVo();
        //查出该问题的所有信息
        GoodsConsultBo question = iGoodsConsultBiz.selectById(consultId);
        if(question == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        qaVo.setQuestion(question);
        //查出该问题的所有回答
        EntityWrapper<GoodsConsultBo> queryAnswer = new EntityWrapper<>();
        queryAnswer.eq(GoodsConsultBo.Key.replyId.toString(), consultId);
        List<GoodsConsultBo> answerList = iGoodsConsultBiz.selectList(queryAnswer);
        qaVo.setAnswer(answerList);
        //查询出商品主图
        EntityWrapper<GoodsImageBo> queryImage = new EntityWrapper<>();
        queryImage.eq(GoodsImageBo.Key.goodsId.toString(), question.getGoodsId());
        queryImage.eq(GoodsImageBo.Key.imgPosition.toString(), "MAJOR");
        GoodsImageBo goodsImageBo = iGoodsImageBiz.selectOne(queryImage);
        if(goodsImageBo != null){
            if(Tools.string.isNotEmpty(goodsImageBo.getImgUrl()))
                qaVo.setImgUrl(QiniuUtil.getFullPath(goodsImageBo.getImgUrl()));
        }

        //查询出最低价
        BigDecimal price = iGoodsPriceDao.getMinPriceMyGoodsId(question.getGoodsId());
        qaVo.setPrice(price);
        //查询商品名称
        GoodsBo goodsBo = new GoodsBo();
        goodsBo = iGoodsBiz.selectById(question.getGoodsId());
        qaVo.setGoodsName(goodsBo.getGoodsName());
        //查看是否是我的提问
        if(memberId != 0 && question.getMemberId().equals(memberId)){
            qaVo.setMyQuestion(true);
        }
        else{
            qaVo.setMyQuestion(false);
        }


        return qaVo;
    }

    @Override
    public List<GoodsAnswerVo> myAnswer(Long memberId) {
        List<GoodsAnswerVo> answerVoList = new ArrayList<>();
        EntityWrapper<GoodsConsultBo> queryAnswer = new EntityWrapper<>();
        queryAnswer.eq(GoodsConsultBo.Key.memberId.toString(), memberId);
        queryAnswer.isNull(GoodsConsultBo.Key.consultContent.toString());
        List<GoodsConsultBo> answerList = iGoodsConsultBiz.selectList(queryAnswer);
        if(answerList.size() == 0)
            return answerVoList;
        answerList.forEach(item->{
            GoodsAnswerVo answerVo = new GoodsAnswerVo();
            answerVo.setAnswer(item);
            //通过replyId查出问题
            GoodsConsultBo question = new GoodsConsultBo();
            question = iGoodsConsultBiz.selectById(item.getReplyId());
            answerVo.setQuestion(question);
            //查询出商品主图
            EntityWrapper<GoodsImageBo> queryImage = new EntityWrapper<>();
            queryImage.eq(GoodsImageBo.Key.goodsId.toString(), item.getGoodsId());
            queryImage.eq(GoodsImageBo.Key.imgPosition.toString(), "MAJOR");
            GoodsImageBo goodsImageBo = new GoodsImageBo();
            goodsImageBo = iGoodsImageBiz.selectOne(queryImage);
//            if(goodsImageBo == null)
//                throw new WakaException("商品图片不存在,goodsId" + item.getGoodsId());
            if(goodsImageBo != null){
                if(Tools.string.isNotEmpty(goodsImageBo.getImgUrl()))
                    answerVo.setImgUrl(QiniuUtil.getFullPath(goodsImageBo.getImgUrl()));
            }

            answerVoList.add(answerVo);
        });
        return answerVoList;
    }


    private GoodsQuestionVo getQuestion(GoodsConsultBo item){
        GoodsQuestionVo questionVo = new GoodsQuestionVo();
        questionVo.setQuestion(item);
        //查出该问题的回答数
        EntityWrapper<GoodsConsultBo> queryAnswer = new EntityWrapper<>();
        queryAnswer.eq(GoodsConsultBo.Key.replyId.toString(), item.getId());
        int answerCount = 0;
        answerCount = iGoodsConsultBiz.selectCount(queryAnswer);
        if(answerCount < 0)
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        questionVo.setAnswerCount(answerCount);
        //查询出商品主图
        EntityWrapper<GoodsImageBo> queryImage = new EntityWrapper<>();
        queryImage.eq(GoodsImageBo.Key.goodsId.toString(), item.getGoodsId());
        queryImage.eq(GoodsImageBo.Key.imgPosition.toString(), "MAJOR");
        GoodsImageBo goodsImageBo = new GoodsImageBo();
        goodsImageBo = iGoodsImageBiz.selectOne(queryImage);
//        if(goodsImageBo == null)
//            throw new WakaException("商品图片不存在,goodsId" + item.getGoodsId());
        if(goodsImageBo != null){
            if(Tools.string.isNotEmpty(goodsImageBo.getImgUrl()))
                questionVo.setImgUrl(QiniuUtil.getFullPath(goodsImageBo.getImgUrl()));
        }


        return questionVo;
    }
}
