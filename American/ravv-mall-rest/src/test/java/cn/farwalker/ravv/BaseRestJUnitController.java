package cn.farwalker.ravv;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.farwalker.RestApplication;
import cn.farwalker.ravv.service.member.pam.member.model.AuthLoginVo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=RestApplication.class)
public class BaseRestJUnitController {
	@Test  
    public void contextLoads() {  
    }  
	
	protected MockMvc mockMvc;
	private MockHttpSession mokcSession;
	@Autowired    
	protected WebApplicationContext context;
	
	protected AuthLoginVo authLoginVo;
	
	protected String token;
	
	protected String randomKey;
	
	@Before
	public void setup() throws Exception {
		this.mokcSession = new MockHttpSession();
 		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		
		MockHttpServletRequestBuilder request = post("/auth/email_login")
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.param("email", "linyouchao8@163.com").param("password", "87654321")
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.session(mokcSession);
		
		//先登录
		MvcResult auth = mockMvc.perform(request)
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();
		
		String json = auth.getResponse().getContentAsString();
		JsonResult rs = Tools.json.toObject(json, JsonResult.class);
		if(rs.isSuccess()){
			this.authLoginVo =  Tools.bean.mapToObject((Map)rs.getData(),AuthLoginVo.class);
			this.token = authLoginVo.getToken();
			this.randomKey = authLoginVo.getRandomKey();
		}
		else{
			throw new RuntimeException(rs.getMessage());
		}
	}

	public MockHttpSession getMokcSession() {
		return mokcSession;
	}
}
