package cn.farwalker.ravv.service.payment.paymentlog.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.ravv.service.payment.paymentlog.dao.IMemberPaymentLogDao;
import cn.farwalker.ravv.service.payment.paymentlog.biz.IMemberPaymentLogBiz;

/**
 * 会员支付日志<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class MemberPaymentLogBizImpl extends ServiceImpl<IMemberPaymentLogDao,MemberPaymentLogBo> implements IMemberPaymentLogBiz{
}