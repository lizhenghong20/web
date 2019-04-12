package cn.farwalker.standard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;

import cn.farwalker.ravv.rest.RestApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=RestApplication.class)
public class BaseRestJUnitController {
	@Test  
    public void contextLoads() {  
    }  
	
	protected MockMvc mockMvc;

	@Autowired    
	protected WebApplicationContext context;
	
	protected AuthResultVO authResultVO;
	
	protected String token;
	
	protected String randomKey;
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
		//先登录
		MvcResult auth = mockMvc.perform(post("/auth")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.param("account", "admin").param("memberId", "46").param("password", "670704cc040ce37e6ddc7b227c59b012")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();
		this.authResultVO = JSONObject.parseObject(auth.getResponse().getContentAsString(), AuthResultVO.class);
		this.token = authResultVO.getToken();
		this.randomKey = authResultVO.getRandomKey();
	}
}
