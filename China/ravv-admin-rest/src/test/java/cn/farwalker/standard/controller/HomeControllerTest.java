package cn.farwalker.standard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;


public class HomeControllerTest extends BaseRestJUnitController {

	@Test
	public void add() throws Exception {
		MvcResult result = mockMvc.perform(post("/home/banner/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
}
