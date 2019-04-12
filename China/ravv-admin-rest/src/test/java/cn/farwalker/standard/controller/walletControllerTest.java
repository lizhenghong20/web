package cn.farwalker.standard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;


public class walletControllerTest extends BaseRestJUnitController{

	
//	@Test
	public void withdrawList() throws Exception {
		MvcResult result = mockMvc.perform(post("/wallet/withdraw/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("memberId", "991255667300356097")
				.param("current", "1")
				.param("size", "10")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
//	@Test
	public void frozenList() throws Exception {
		MvcResult result = mockMvc.perform(post("/wallet/frozen/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("memberId", "995188912572260354")
				.param("current", "1")
				.param("size", "10")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
//	@Test
	public void depositList() throws Exception {
		MvcResult result = mockMvc.perform(post("/wallet/deposit/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("memberId", "995188912572260354")
				.param("current", "1")
				.param("size", "10")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
//	@Test
	public void getMinoMoney() throws Exception {
		MvcResult result = mockMvc.perform(post("/wallet/withdraw/minmoney")
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
	public void rewardList() throws Exception {
		MvcResult result = mockMvc.perform(post("/wallet/reward/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("memberId", "995188912572260354")
				.param("current", "1")
				.param("size", "10")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
	@Test
	public void rewardTotal() throws Exception {
		MvcResult result = mockMvc.perform(post("/wallet/reward/total")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("memberId", "991255667300356097")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
//	@Test
	public void advance() throws Exception {
		MvcResult result = mockMvc.perform(post("/wallet/advance")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.param("memberId", "995188912572260354")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
//	@Test
	public void get() throws Exception {
		MvcResult result = mockMvc.perform(post("/order/get")
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
}
