package cn.farwalker.ravv.service.member.accountflow.biz;

import com.baomidou.mybatisplus.plugins.Page;

import cn.farwalker.ravv.service.member.accountflow.constants.ChargeTypeEnum;
import cn.farwalker.ravv.service.member.accountflow.model.MemberAccountFlowBo;
import cn.farwalker.ravv.service.member.accountflow.model.MemberAccountVo;
import cn.farwalker.ravv.service.member.accountflow.model.MemberTransactionListVo;

public interface IMemberAccountFlowService {
    public MemberAccountVo getAccountAdvance(Long memberId);

    public MemberTransactionListVo getAllTransaction(Long memberId, int currentPage, int pageSize);

    public MemberTransactionListVo getTransactionByChargeSource(Long memberId, int type, int currentPage, int pageSize);

    public boolean updatePayPassword(Long memberId, String payPassword);

    public String verifiedOldPassword(Long memberId, String payPassword);

    public String updatePayPasswordWithActivationCode(Long memberId, String email, String payPassword, String activationCode);
    
    /**
     * 根据交易流水类型获取交易记录
     * @param memberId 会员id
     * @param type 交易流水类型
     * @param start 页码
     * @param size 每页行数
     * @return
     */
    Page<MemberAccountFlowBo> getTransactionByChargeType(Long memberId, ChargeTypeEnum type, Integer start, Integer size);
}
