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

import cn.farwalker.ravv.rest.RestApplication;
import cn.farwalker.ravv.service.category.basecategory.model.BaseCategoryBo;
import com.alibaba.fastjson.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=RestApplication.class)
public class BaseCategoryControllerTest {

	@Test  
    public void contextLoads() {  
    }  
	
	private MockMvc mockMvc;

	@Autowired    
	private WebApplicationContext context;
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
//	@Test
	public void testAdd() throws Exception {
		BaseCategoryBo category = new BaseCategoryBo();
		category.setCatName("测试分类");
		category.setLeaf(Boolean.TRUE);
		category.setSequence(1);
		//category.setStatus(EnabledStatusEnum.ENABLED );
		category.setPid(994172033376444418L);
		
		MvcResult result = mockMvc.perform(post("/baseCategory/add")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
//				.header("Content-Type", "application/json")
				.content(JSONObject.toJSONString(category))
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
	@Test
	public void testList() throws Exception{ 
		MvcResult result = mockMvc.perform(post("/category/leaf/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
}
