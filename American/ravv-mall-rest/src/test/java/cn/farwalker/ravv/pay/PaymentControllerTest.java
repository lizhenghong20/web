package cn.farwalker.ravv.pay;

import cn.farwalker.ravv.BaseRestJUnitController;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class PaymentControllerTest extends BaseRestJUnitController {

    @Test
    @Rollback(false)
    public void payByAll() throws Exception{
        Long orderId = 1110818454283317249L;
//        String payType = "Advance";
//        String original = "123456";
//        String payPassword = Tools.md5.md5Hex(original).toLowerCase();
//
//        Long memberId = 1067707280516718594L;
//        this.getMokcSession().setAttribute("memberId", memberId);
//        MockHttpServletRequestBuilder request = post("/pay/pay_by_order")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .header("Authorization", "Bearer " + this.token)
//                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
//                .param("orderId",orderId.toString())
//                .param("payType", payType)
//                .param("payPassword", payPassword)
//                .accept(MediaType.APPLICATION_JSON_UTF8)
//                .session(getMokcSession());
//        MvcResult result = mockMvc.perform(request)
//                .andExpect(status().isOk()) //模拟发出post请求
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))//预期返回值类型
//                .andReturn();//返回执行结果
//        String json = result.getResponse().getContentAsString();
//        JsonResult rs = Tools.json.toObject(json, JsonResult.class);
//        if(rs.isSuccess()){
//            ;//
//        }
//        else{
//            throw new RuntimeException(rs.getMessage());
//        }
    }
}
