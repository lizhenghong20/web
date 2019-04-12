package cn.farwalker.standard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;


public class FileControllerTest extends BaseRestJUnitController {

	@Test
	public void getImage() throws Exception {
		MvcResult result = mockMvc.perform(post("/file/image/0885c0cb-d871-4aed-ac19-397a2e579748.jpg")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
//				.param("imageName", "0885c0cb-d871-4aed-ac19-397a2e579748.jpg")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
}
