package cn.farwalker.ravv.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;




import cn.farwalker.ravv.BaseRestJUnitController;
import cn.farwalker.ravv.service.order.orderinfo.constants.OrderStatusEnum;
import cn.farwalker.ravv.service.order.orderinfo.model.OrderGoodsSkuVo;
import cn.farwalker.standard.util.JwtUtil;
import cn.farwalker.waka.auth.converter.BaseTransferEntity;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 测试时需要 取消 {@link PaypalConfig#apiContext()}
 * @author Administrator
 *
 */
public class OrderMyControllerTest extends BaseRestJUnitController{
	private static final Logger log = LoggerFactory.getLogger(OrderMyControllerTest.class);
 
	@Test
	public void doListParams() throws Exception { 
		Long memberId =1064332409766268930l;
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity("", this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/order/my/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(contentData)
				.param("tabType", "1")
				//.param("status", Tools.json.toJson(OrderStatusEnum.SING_GOODS))
				.param("lastmonth", "3")
				.param("search", "澳大")
				.param("sortfield", "+id,-order_status")
				
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.session(getMokcSession());
			
		MvcResult result = mockMvc.perform(request)
			.andExpect(status().isOk()) //模拟发出post请求
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
			.andReturn();//返回执行结果
		
		String json = result.getResponse().getContentAsString();
		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
		if(rs.isSuccess()){
			;//
		}
		else{
			throw new RuntimeException(rs.getMessage());
		}
	}
	@Test
	public void doListEmptyParams() throws Exception { 
		Long memberId = 1064332409766268930l;
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity("", this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/order/my/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(contentData)
				/*
				.param("tabType", "0")
				.param("status", Tools.json.toJson(OrderStatusEnum.SING_GOODS))
				.param("lastmonth", "3")
				.param("search", "g")
				.param("sortfield", "+id,-order_status")
				
				.param("search", "澳大")*/
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.session(getMokcSession());
			
		MvcResult result = mockMvc.perform(request)
			.andExpect(status().isOk()) //模拟发出post请求
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
			.andReturn();//返回执行结果
		
		String json = result.getResponse().getContentAsString();
		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
		if(rs.isSuccess()){
			;//
		}
		else{
			throw new RuntimeException(rs.getMessage());
		}
	}
}
