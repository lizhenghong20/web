package cn.farwalker.standard.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import cn.farwalker.ravv.service.member.basememeber.model.BankForm;
import cn.farwalker.standard.util.JwtUtil;
import cn.farwalker.waka.auth.converter.BaseTransferEntity;

import com.alibaba.fastjson.JSONObject;


public class BankControllerTest extends BaseRestJUnitController {

	@Test
	public void add() throws Exception {
		BankForm form = new BankForm();
		form.setMemberId(995188912572260354L);//991255667300356097L
		form.setBankName("工商银行");
		form.setBindTime(new Date());
		form.setCardNumber("2355345345345");
		form.setMemberName("刘年");
		
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(form, this.randomKey);
		
		MvcResult result = mockMvc.perform(post("/bank/add")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", "application/json")
				.content(JSONObject.toJSONString(baseTransferEntity))
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		System.out.println(result.getResponse().getContentAsString());
	}
	
}
