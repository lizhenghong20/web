package cn.farwalker.ravv.profitallot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import cn.farwalker.ravv.BaseRestJUnitController;
import cn.farwalker.ravv.service.paypal.PaypalConfig;
import cn.farwalker.standard.util.JwtUtil;
import cn.farwalker.waka.auth.converter.BaseTransferEntity;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 测试时需要 取消 {@link PaypalConfig#apiContext()}
 * @author Administrator
 *
 */
public class ProfitallotControllerTest extends BaseRestJUnitController{
	private static final Logger log = LoggerFactory.getLogger(ProfitallotControllerTest.class);
 
//	@Test
	public void relativetree() throws Exception { 
		Long memberId = 1063756090326241282l;
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity("", this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/profitallot/get_relativetree")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(contentData)
				//.param("status", Tools.json.toJson(OrderStatusEnum.SING_GOODS))
				.param("start", "0")
				.param("size", "7")
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
	
//	@Test
	public void getPromoteInfo() throws Exception { 
		Long memberId = 1063756090326241282l;
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity("", this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/profitallot/get_promoteInfo")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(contentData)
				//.param("status", Tools.json.toJson(OrderStatusEnum.SING_GOODS))
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
	public void getRebatedListByStatus() throws Exception { 
		Long memberId = 1063756090326241282l;
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity("", this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/profitallot/rebatedlist_bystatus")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(contentData)
				.param("start", "0")
				.param("size", "20")
				.param("status", "returned")
				//.param("status", Tools.json.toJson(OrderStatusEnum.SING_GOODS))
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
	
//	@Test
	public void getMonthRebatedlist() throws Exception { 
		Long memberId = 1071356849570045954l;
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity("", this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/profitallot/month_rebatedlist")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(contentData)
				.param("month", "2018-12-01")
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
	
//	@Test
	public void getRelativelevel() throws Exception { 
		Long memberId = 1063756090326241282l;
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity("", this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/profitallot/get_relativelevel")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(contentData)
				.param("subordinate", "second_subordinate")
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
	
//	@Test
	public void getRebatedlist() throws Exception { 
		Long memberId = 1067707280516718594l;
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity("", this.randomKey);
		String contentData = Tools.json.toJson(baseTransferEntity);
		
		this.getMokcSession().setAttribute("memberId", memberId);
		MockHttpServletRequestBuilder request = post("/profitallot/getrebatedlist")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(contentData)
				.param("start", "0")
				.param("start", "10")
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
