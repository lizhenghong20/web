package cn.farwalker.ravv.merchant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.standard.controller.BaseRestJUnitController;
import cn.farwalker.standard.util.JwtUtil;
import cn.farwalker.waka.auth.converter.BaseTransferEntity;

import com.alibaba.fastjson.JSONObject;
import com.cangwu.frame.web.crud.QueryFilter;


public class MerchantControllerTest  extends BaseRestJUnitController{
	private static final Logger log = LoggerFactory.getLogger(MerchantController.class);
	@Test
	public void list() throws Exception {
		//需要原始字段
		List<QueryFilter> query = new ArrayList<>();
		{
			/*QueryFilter f1 = new QueryFilter();
			f1.setField(MerchantBo.Key.memberId.toString());
			f1.setStartValue("abc");
			f1.setFieldtype("string");
			f1.setLogic("or");
		
			QueryFilter f2 = new QueryFilter();
			f2.setField(MerchantBo.Key.lastLoginTime.toString());
			f2.setStartValue("2018-10-12");
			f2.setFieldtype("DATE");
			f2.setLogic("and");
			query.add(f1);
			query.add(f2);*/
		}
		String sortfield = "-" + MerchantBo.Key.account.toString();
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(query, this.randomKey);
		String content = JSONObject.toJSONString(baseTransferEntity);
		log.debug(content);
		
		MvcResult result = mockMvc.perform(post("/merchant/list")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.content(content)
				.param("sortfield",sortfield)
				.param("start", "0").param("size", "100") //省略参数
				
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		String msg = result.getResponse().getContentAsString();
		log.info(msg);
	} 
	
	//@Test
	public void get() throws Exception {
		MvcResult result = mockMvc.perform(post("/merchant/get")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.param("id", "991330479947010")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
		String msg = result.getResponse().getContentAsString();
		log.info(msg);
	}
	
 
}
