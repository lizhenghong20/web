package cn.farwalker.ravv.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import cn.farwalker.ravv.BaseRestJUnitController;
import cn.farwalker.ravv.service.paypal.PaypalConfig;
import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderInfoBiz;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderInfoBo;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderReturnSkuVo;
import cn.farwalker.ravv.service.order.time.OrderTaskTimer;
import cn.farwalker.standard.util.JwtUtil;
import cn.farwalker.waka.auth.converter.BaseTransferEntity;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 测试时需要 取消 {@link PaypalConfig#apiContext()}
 * 
 * @author Administrator
 *
 */
public class OrderReturnControllerTest extends BaseRestJUnitController {
	private static final Logger log = LoggerFactory.getLogger(OrderReturnControllerTest.class);
	
	@Resource
	private OrderTaskTimer ottimer;
	
	@Resource
	private IOrderInfoBiz orderInfoBiz;

	@Test
	public void doListParams() throws Exception {
		Long memberId = 1085730962094784513l;

		// String contentData = Tools.json.toJson(baseTransferEntity);
		List<OrderReturnSkuVo> skus = new ArrayList<OrderReturnSkuVo>();
		OrderReturnSkuVo ors = new OrderReturnSkuVo();
		ors.setReasonType("无理由退款");
		ors.setReason("reason.....");
		// ors.setReturnsType(ReturnsTypeEnum.ChangeGoods);
		ors.setGoodsId(1069850092058222594l);
		ors.setSkuId(1070566269105594369l);
		ors.setQuan(1);
		skus.add(ors);

		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(skus, this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/order/return/refund")
				.contentType(MediaType.APPLICATION_JSON_UTF8).header("Authorization", "Bearer " + this.token)
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				// .content(contentData)
				 .param("orderId", "1085793801627086850")
				// .param("returnsType", Tools.json.toJson(ReturnsTypeEnum.ChangeGoods))
//				.param("isReturnFinish", "false").param("start", "0").param("size", "10")
//				.param("orderId", "1085353202235068418").param("reasonType", "选择的原因类型").param("reason", "填写的原因")
				// .param("status",
				// Tools.json.toJson(ReturnsGoodsStatusEnum.exchangeWaitReceived))
				.param("returnsId", "1086151523832795138")
				// .param("sortfield", "+id,-status")

				.accept(MediaType.APPLICATION_JSON_UTF8).session(getMokcSession());

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()) // 模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值类型
				.andReturn();// 返回执行结果

		String json = result.getResponse().getContentAsString();
		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
		if (rs.isSuccess()) {
			;//
		} else {
			throw new RuntimeException(rs.getMessage());
		}
	}

	@Test
	public void sendReturnGoods() throws Exception {
		Long memberId = 1067707280516718594L;
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/order/return/return_shipment")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.param("returnsId", "1085443121614786561")
				.param("logisticsNo", "11111111111110")
				.param("shipmentId", "kuaib")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.session(getMokcSession());

		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk()) // 模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值类型
				.andReturn();// 返回执行结果

		String json = result.getResponse().getContentAsString();
		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
		if (rs.isSuccess()) {
			;//
		} else {
			throw new RuntimeException(rs.getMessage());
		}
	}

	@Test
	public void confirmReceipt() throws Exception {
		Long memberId = 1064332409766268930l;
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/order/return/confirm_receipt")
				.contentType(MediaType.APPLICATION_JSON_UTF8).header("Authorization", "Bearer " + this.token)
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE).param("returnsId", "1085443121614786561")
				.accept(MediaType.APPLICATION_JSON_UTF8).session(getMokcSession());

		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()) // 模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值类型
				.andReturn();// 返回执行结果

		String json = result.getResponse().getContentAsString();
		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
		if (rs.isSuccess()) {
			;//
		} else {
			throw new RuntimeException(rs.getMessage());
		}
	}
	
	@Test
	public void closeReturnOrder() throws Exception {
		OrderInfoBo orderBos = orderInfoBiz.selectById(1100201804859023362l);
		ottimer.closeOrderForReturn(Arrays.asList(orderBos));
	}
	
	
	
}
