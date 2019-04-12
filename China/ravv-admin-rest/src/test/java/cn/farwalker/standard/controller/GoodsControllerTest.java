package cn.farwalker.standard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;


public class GoodsControllerTest  extends BaseRestJUnitController{

//	@Test
	public void list() throws Exception {
		MvcResult result = mockMvc.perform(post("/goods/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("memberId", "991255667300356097")
				.param("date", "2018-04-28")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
	@Test
	public void homeList() throws Exception {
		MvcResult result = mockMvc.perform(post("/home/goods/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
	
//	@Test
	public void get() throws Exception {
		MvcResult result = mockMvc.perform(post("/goods/get")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("goodsId", "991330474779947010")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
//	@Test
	public void viewLog() throws Exception {
		MvcResult result = mockMvc.perform(post("/goods/view/log")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("memberId", "991255667300356097")
				.param("goodsId", "991330474779947010")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
//	@Test
	public void getSimple() throws Exception {
		MvcResult result = mockMvc.perform(post("/goods/simple")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("skuId", "996232376067969026")
				.param("goodsId", "991330474779947010")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
}
