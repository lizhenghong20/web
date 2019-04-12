package cn.farwalker.ravv.basearea;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.alibaba.fastjson.JSONObject;

import cn.farwalker.ravv.service.base.area.constant.CountryCodeEnum;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.standard.controller.BaseRestJUnitController;
import cn.farwalker.standard.util.JwtUtil;
import cn.farwalker.waka.auth.converter.BaseTransferEntity;

public class BaseareaControllerTest extends BaseRestJUnitController{
	private static final Logger log = LoggerFactory.getLogger(BaseAreaController.class);
	
	@Test
	public void submit() throws Exception {
		BaseAreaBo form = new BaseAreaBo();
//		form.setCountryCode(CountryCodeEnum.CHINA.getKey());
		form.setPid(1065857428501639169L);
		form.setName("珠海市");
		form.setCode("4404");
		
		BaseTransferEntity baseTransferEntity = JwtUtil.wrapAsTransferEntity(form, this.randomKey);
		
		MvcResult result = mockMvc.perform(post("/basearea/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.header("Authorization", "Bearer " + this.token)
				.header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
				.content(JSONObject.toJSONString(baseTransferEntity))
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()) //模拟发出post请求
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
				.andReturn();//返回执行结果
				String msg = result.getResponse().getContentAsString();
				log.info(msg);
		}
}
