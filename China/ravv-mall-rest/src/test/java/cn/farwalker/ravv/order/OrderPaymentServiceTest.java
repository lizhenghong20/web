package cn.farwalker.ravv.order;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.farwalker.RestApplication;
import cn.farwalker.ravv.service.order.constants.PaymentPlatformEnum;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderPaymentService;
import cn.farwalker.ravv.service.payment.paymentlog.model.MemberPaymentLogBo;
import cn.farwalker.waka.util.Tools;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=RestApplication.class)
public class OrderPaymentServiceTest {
	@Resource
	private IOrderPaymentService service;
	
	/** 拆单测试*/
	@Test
	public void updatePaymentCallbackMulti() throws Exception {
		Long orderId =1085471779700813826l;
		String json ="{\"id\":null,\"account\":null,\"bank\":null,\"curMoney\":null,\"currencyCode\":null,\"disabled\":null,\"gmtCreate\":null,\"gmtModified\":null,\"ip\":null,\"memberId\":null,\"memo\":null,\"money\":null,\"operatorId\":null,\"orderId\":" +
				orderId + ",\"payAccount\":null,\"payBeginTime\":null,\"payConfirmTime\":null,\"payMethod\":null,\"payType\":{\"key\":\"PayPal\",\"label\":\"PayPal\",\"value\":\"PayPal\"},\"paycost\":null,\"payedTime\":1545207310789,\"remark\":null,\"returnUrl\":null,\"status\":null,\"tradeNo\":null}{\"id\":null,\"account\":null,\"bank\":null,\"curMoney\":null,\"currencyCode\":null,\"disabled\":null,\"gmtCreate\":null,\"gmtModified\":null,\"ip\":null,\"memberId\":null,\"memo\":null,\"money\":null,\"operatorId\":null,\"orderId\":1075303514331590657,\"payAccount\":null,\"payBeginTime\":null,\"payConfirmTime\":null,\"payMethod\":null,\"payType\":{\"key\":\"PayPal\",\"label\":\"PayPal\",\"value\":\"PayPal\"},\"paycost\":null,\"payedTime\":1545207310789,\"remark\":null,\"returnUrl\":null,\"status\":null,\"tradeNo\":null}";
		Map m = Tools.json.toMap(json);
		MemberPaymentLogBo logBo = Tools.bean.mapToObject(m, new MemberPaymentLogBo());
		Boolean b = Boolean.FALSE;
		int i =0;
		while(i++ < 3){
			b = service.updatePaymentCallback(orderId,PaymentPlatformEnum.PayPal, logBo);
		}
		Assert.assertTrue("支付回调处理(支付成功时，需要更新库存)", b.booleanValue());
	}
	/** 没拆单测试*/
	@Test
	public void updatePaymentCallbackSinge() throws Exception {
		Long orderId =1085433214434426882l;
		String json ="{\"id\":null,\"account\":null,\"bank\":null,\"curMoney\":null,\"currencyCode\":null,\"disabled\":null,\"gmtCreate\":null,\"gmtModified\":null,\"ip\":null,\"memberId\":null,\"memo\":null,\"money\":null,\"operatorId\":null,\"orderId\":" +
				orderId + ",\"payAccount\":null,\"payBeginTime\":null,\"payConfirmTime\":null,\"payMethod\":null,\"payType\":{\"key\":\"PayPal\",\"label\":\"PayPal\",\"value\":\"PayPal\"},\"paycost\":null,\"payedTime\":1545207310789,\"remark\":null,\"returnUrl\":null,\"status\":null,\"tradeNo\":null}{\"id\":null,\"account\":null,\"bank\":null,\"curMoney\":null,\"currencyCode\":null,\"disabled\":null,\"gmtCreate\":null,\"gmtModified\":null,\"ip\":null,\"memberId\":null,\"memo\":null,\"money\":null,\"operatorId\":null,\"orderId\":1075303514331590657,\"payAccount\":null,\"payBeginTime\":null,\"payConfirmTime\":null,\"payMethod\":null,\"payType\":{\"key\":\"PayPal\",\"label\":\"PayPal\",\"value\":\"PayPal\"},\"paycost\":null,\"payedTime\":1545207310789,\"remark\":null,\"returnUrl\":null,\"status\":null,\"tradeNo\":null}";
		Map m = Tools.json.toMap(json);
		MemberPaymentLogBo logBo = Tools.bean.mapToObject(m, new MemberPaymentLogBo());
		Boolean b = Boolean.FALSE;
		int i =0;
		while(i++ < 3){
			b = service.updatePaymentCallback(orderId,PaymentPlatformEnum.PayPal, logBo);
		}
		Assert.assertTrue("支付回调处理(支付成功时，需要更新库存)", b.booleanValue());
	}
}
