package cn.farwalker.ravv.goods;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import cn.farwalker.ravv.BaseRestJUnitController;
import cn.farwalker.ravv.service.paypal.PaypalConfig;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 测试时需要 取消 {@link PaypalConfig#apiContext()}
 * @author Administrator
 *
 */
public class GoodsWebControllerTest extends BaseRestJUnitController{
	private static final Logger log = LoggerFactory.getLogger(GoodsWebControllerTest.class);
	
//	@Test
	public void search() throws Exception {
		Long memberId =1063965054397698050l;
		
		this.getMokcSession().setAttribute("memberId", memberId);
		
		MockHttpServletRequestBuilder request = post("/goods/search")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.param("currentPage", "1")
				.param("pageSize", "10")
				.param("keyWords", "圣罗兰")
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
	public void getKeyWords() throws Exception {
		Long memberId =1063965054397698050l;
		
		this.getMokcSession().setAttribute("memberId", memberId);
		
		MockHttpServletRequestBuilder request = post("/goods/get_keyword_history")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.param("currentPage", "1")
				.param("pageSize", "10")
				.param("keyWords", "圣罗兰")
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
	public void delKeyWords() throws Exception {
		Long memberId =1063965054397698050l;
		
		this.getMokcSession().setAttribute("memberId", memberId);
		
		MockHttpServletRequestBuilder request = post("/goods/del_keyword_history")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.param("currentPage", "1")
				.param("pageSize", "10")
				.param("keyWords", "圣罗兰")
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
