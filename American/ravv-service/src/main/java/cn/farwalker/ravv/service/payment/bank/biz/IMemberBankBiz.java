package cn.farwalker.ravv.service.payment.bank.biz;
import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import cn.farwalker.ravv.service.payment.bank.model.MemberBankBo;

/**
 * 会员银行卡<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IMemberBankBiz extends IService<MemberBankBo>{
	
	/**
	 * 根据会员id获取银行列表
	 * @param memberId
	 * @return
	 */
	List<MemberBankBo> getBankCardByMemberId(Long memberId);
	
}