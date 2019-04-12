package cn.farwalker.ravv.deal;

import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.member.address.biz.IMemberAddressService;
import cn.farwalker.ravv.service.member.address.model.MemberAddressBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import com.taxjar.exception.TaxjarException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by asus on 2018/12/13.
 */
@Slf4j
@RestController
@RequestMapping("/shipping_address")
public class ShippingAddressController {

    @Autowired
    private IMemberAddressService iMemberAddressService;

    @RequestMapping("add_address")
    public JsonResult<String> addAddress(HttpSession session, MemberAddressBo memberAddressBo){
        try {
                long memberId = (long)session.getAttribute("memberId");
                if(memberId == 0)
                    throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
               return JsonResult.newSuccess(iMemberAddressService.addAddress(memberId,memberAddressBo));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (TaxjarException e){
            log.error("", e);
            return  JsonResult.newFail(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("update_address")
    public JsonResult<String> updateAddress(HttpSession session, MemberAddressBo memberAddressBo){
        try {
            if(memberAddressBo.getId().longValue() == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            long memberId = (long)session.getAttribute("memberId");
            if(memberId == 0)
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            return JsonResult.newSuccess(iMemberAddressService.updateAddress(memberId,memberAddressBo));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (TaxjarException e){
            log.error("", e);
            return  JsonResult.newFail(e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("get_address")
    public JsonResult<List<MemberAddressBo>> getAddress(HttpSession session){
        try {

            long memberId = (long)session.getAttribute("memberId");
            if(memberId == 0)
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            return JsonResult.newSuccess(iMemberAddressService.getAddress(memberId));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("get_default_address")
    public JsonResult<MemberAddressBo> getDefaultAddress(HttpSession session){
        try {

            long memberId = (long)session.getAttribute("memberId");
            if(memberId == 0)
                throw new WakaException(RavvExceptionEnum.TOKEN_PARSE_MEMBER_ID_FAILED);
            return JsonResult.newSuccess(iMemberAddressService.getDefaultAddress(memberId));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("del_address")
    public JsonResult<String> delAddress(Long memberAddressId){
        try {
            if(memberAddressId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iMemberAddressService.delAddress(memberAddressId));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("get_states")
    public JsonResult<List<BaseAreaBo>> getStatus(){
        try {
            return JsonResult.newSuccess(iMemberAddressService.getStates());

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }

    @RequestMapping("get_city")
    public JsonResult<List<BaseAreaBo>> getCity(Long areaId){
        try {
            if(areaId == 0)
                throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
            return JsonResult.newSuccess(iMemberAddressService.getCity(areaId));

        } catch (WakaException e) {
            log.error("", e);
            return JsonResult.newFail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("", e);
            return JsonResult.newFail(e.getMessage());

        }
    }


}
