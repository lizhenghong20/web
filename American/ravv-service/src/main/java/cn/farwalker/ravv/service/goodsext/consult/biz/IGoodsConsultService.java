package cn.farwalker.ravv.service.goodsext.consult.biz;

import cn.farwalker.ravv.service.goodsext.consult.model.*;

import java.util.List;

public interface IGoodsConsultService {

    public String addQuestion(GoodsConsultBo question);

    public String addAnswer(GoodsConsultBo answer);

    public List<GoodsConsultVo> getConsultList(Long goodsId, int currentPage, int pageSize);

    public List<GoodsConsultVo> searchQuestion(String question);

    public List<GoodsQuestionVo> searchQuestionForApp(String question);

    public List<GoodsQuestionVo> myQuestion(Long memberId);

    public GoodsQaVo questionDetail(Long memberId, Long consultId);

    public List<GoodsAnswerVo> myAnswer(Long memberId);

}
