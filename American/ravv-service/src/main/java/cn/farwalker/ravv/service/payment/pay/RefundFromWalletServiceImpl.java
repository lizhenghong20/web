package cn.farwalker.ravv.service.payment.pay;

import cn.farwalker.ravv.service.member.basememeber.biz.IMemberBiz;
import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.order.constants.PayStatusEnum;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.paypal.RefundForm;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by asus on 2019/3/13.
 */
@Service("refundFromWalletServiceImpl")
public class RefundFromWalletServiceImpl implements IRefundService {
    @Autowired
    IMemberPaymentLogBiz iMemberPaymentLogBiz;

    @Autowired
    private IMemberBiz memberBiz;


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String refund(RefundForm refundForm){

        if(refundForm.getRefundTotalAmount().doubleValue() < 0 || refundForm.getOrderId() == null
                || refundForm.getMemberId()==null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        MemberPaymentLogBo queryLogBo = iMemberPaymentLogBiz.selectOne(Condition.create()
                .eq(MemberPaymentLogBo.Key.orderId.toString(),refundForm.getOrderId())
                .eq(MemberPaymentLogBo.Key.memberId.toString(),refundForm.getMemberId())
                .eq(MemberPaymentLogBo.Key.status.toString(), PayStatusEnum.PAID.toString())
                .eq(MemberPaymentLogBo.Key.payType.toString(), PaymentPlatformEnum.Advance.getKey()));
        if(queryLogBo == null)
            throw new WakaException("当前参数查询不到支付信息");

        if(refundForm.getRefundTotalAmount().doubleValue() != 0)
            if( refundForm.getRefundTotalAmount().doubleValue() >  queryLogBo.getTotalAmount().doubleValue())
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);

        MemberPaymentLogBo insertBo = new MemberPaymentLogBo();
        Tools.bean.copyProperties(queryLogBo,insertBo);
        insertBo.setStatus(PayStatusEnum.REFUND);
        insertBo.setReturnOrderId(refundForm.getReturnOrderId());
        insertBo.setRefundTime(new Date());
        insertBo.setId(null);
        insertBo.setGmtModified(null);
        insertBo.setGmtCreate(null);
        insertBo.setPayedTime(null);
        //部分退款更新为输入值，全额退款更新为查询值
        if(refundForm.getRefundTotalAmount().doubleValue() != 0){
            insertBo.setRefundTotalAmount(refundForm.getRefundTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
        }else{
            insertBo.setRefundTotalAmount(queryLogBo.getTotalAmount());
        }
        updateAdvanceAndPaymentLog(refundForm,insertBo);

        return "refund success!";
    }

    /**
     * 支付成功后更新log
     */
    private void updateAdvanceAndPaymentLog(RefundForm refundForm,MemberPaymentLogBo insertBo){
        MemberBo memberBo = memberBiz.selectById(refundForm.getMemberId());
        MemberBo updateMemberBo = new MemberBo();
        updateMemberBo.setAdvance((memberBo.getAdvance() == null ? BigDecimal.ZERO : memberBo.getAdvance()).add(refundForm.getRefundTotalAmount())
                .setScale(2,BigDecimal.ROUND_HALF_UP));
        updateMemberBo.setId(refundForm.getMemberId());
        //更新余额，相当于退款
        if(!memberBiz.updateById(updateMemberBo)){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        iMemberPaymentLogBiz.insert(insertBo);
    }
}
