package cn.farwalker.ravv.service.payment.bank.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.payment.bank.model.MemberBankBo;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.ravv.service.payment.bank.dao.IMemberBankDao;
import cn.farwalker.ravv.service.payment.bank.biz.IMemberBankBiz;

/**
 * 会员银行卡<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class MemberBankBizImpl extends ServiceImpl<IMemberBankDao,MemberBankBo> implements IMemberBankBiz{

	@Resource
	private IMemberBankBiz memberBankBiz;
	
	@Override
	public List<MemberBankBo> getBankCardByMemberId(Long memberId) {
		if(null == memberId) {
			return null;
		}
		Wrapper<MemberBankBo> wrapper = new EntityWrapper<>();
		wrapper.eq(MemberBankBo.Key.memberId.toString(), memberId);
		
		List<MemberBankBo> bankCardList = memberBankBiz.selectList(wrapper);
		if(Tools.collection.isNotEmpty(bankCardList)) {
			for(MemberBankBo bankCard : bankCardList) {
				//只显示银行卡后四位数字
				Integer length = bankCard.getCardNumber().length();
				StringBuilder hideCardNumber = new StringBuilder(bankCard.getCardNumber());
				hideCardNumber.replace(0, length - 4, "*");
				
				bankCard.setCardNumber(hideCardNumber.toString());
			}
		}
				
		return bankCardList;
	}
}