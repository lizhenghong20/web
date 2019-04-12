package cn.farwalker.ravv.category;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import cn.farwalker.standard.controller.BaseRestJUnitController;

public class CategoryControllerTest extends BaseRestJUnitController {
	
	private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
	
//	@Test
	public void create() throws Exception {
		MvcResult result = mockMvc.perform(post("/category/create_category")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.param("pid", "1064784821958303746")
				.param("categoryName", "女装")
				.param("sequence", "1")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		String msg = result.getResponse().getContentAsString();
		log.info(msg);
	}
	
	@Test
	public void createProValue() throws Exception {
		MvcResult result = mockMvc.perform(post("/category/create_property_value")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.param("propertyId", "1064785495773155329")
				.param("valueName", "L码")
				.param("sequence", "1")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		String msg = result.getResponse().getContentAsString();
		log.info(msg);
	}
	
//	@Test
	public void createProperty() throws Exception {
		MvcResult result = mockMvc.perform(post("/category/create_category_property")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.param("catId", "1064784821958303746")
				.param("propertyName", "尺码")
				.param("sequence", "1")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		String msg = result.getResponse().getContentAsString();
		log.info(msg);
	}
	
//	@Test
	public void categoryListTree() throws Exception {
		MvcResult result = mockMvc.perform(post("/category/category_tree")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		String msg = result.getResponse().getContentAsString();
		log.info(msg);
	}
	
//	@Test
	public void propertyListTree() throws Exception {
		MvcResult result = mockMvc.perform(post("/category/property_tree")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.param("catId", "1062909581682638849")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		String msg = result.getResponse().getContentAsString();
		log.info(msg);
	}

}
