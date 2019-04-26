package cn.farwalker.ravv.service.member.address.biz.impl;

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.model.AreaFullVo;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.base.storehouse.biz.IStorehouseBiz;
import cn.farwalker.ravv.service.base.storehouse.model.StorehouseBo;
import cn.farwalker.ravv.service.member.address.biz.IMemberAddressBiz;
import cn.farwalker.ravv.service.member.address.biz.IMemberAddressService;
import cn.farwalker.ravv.service.member.address.model.MemberAddressBo;
import cn.farwalker.ravv.service.order.orderinfo.biz.impl.TaxUtil;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.taxjar.Taxjar;
import com.taxjar.exception.TaxjarException;
import com.taxjar.model.taxes.Tax;
import com.taxjar.model.taxes.TaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/12/13.
 */
@Service
public class MemberAddressServiceImpl implements IMemberAddressService {
    @Autowired
    private IMemberAddressBiz iMemberAddressBiz;

    @Autowired
    private IBaseAreaBiz iBaseAreaBiz;

    @Autowired
    private IStorehouseBiz iStorehouseBiz;

    private static final String D_From_country="from_country",D_From_zip="from_zip"
            ,D_From_state="from_state",D_From_city="from_city",D_From_street="from_street";
    private static final String D_To_country="to_country",D_To_zip="to_zip",D_To_state="to_state"
            ,D_To_city="to_city",D_To_street="to_street";
    private static final String D_Amount="amount",D_Shipping="shipping";

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addAddress(Long memberId,MemberAddressBo memberAddressBo) throws TaxjarException{
        //判断地址和邮编是否匹配
        if(!addressIsValid(memberAddressBo))
            throw new TaxjarException("邮编和地址不匹配");
        long areaId = memberAddressBo.getAreaId();
        if(areaId == 0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        BaseAreaBo queryBaseArea =  iBaseAreaBiz.selectById(areaId);
        if(queryBaseArea == null||queryBaseArea.getPid()==0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        //拼出地址
        String areaName = queryBaseArea.getName();
        if(queryBaseArea.getPid() != 0){
            areaName = areaName + "," +  iBaseAreaBiz.selectById(queryBaseArea.getPid()).getName();
        }

        //查询该用户是否第一次添加地址
        EntityWrapper<MemberAddressBo> firstAddress = new EntityWrapper<>();
        firstAddress.eq(MemberAddressBo.Key.memberId.toString(), memberId);
        List<MemberAddressBo> addressBoList = iMemberAddressBiz.selectList(firstAddress);
        //如果不是第一次添加地址，判断该次添加是否设置默认
        if(addressBoList.size() != 0){
            //如果该次添加设置默认，将之前默认设置为-1
            if(memberAddressBo.getDefaultAddr() == 1){
                isFirst(memberId);
            }
            //插入该条记录
            memberAddressBo.setMemberId(memberId);
            memberAddressBo.setAreaName(areaName);
            if(!iMemberAddressBiz.insert(memberAddressBo))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }//如果是第一次添加，强制设置为默认
        else{
            memberAddressBo.setMemberId(memberId);
            memberAddressBo.setDefaultAddr(1);
            memberAddressBo.setAreaName(areaName);
            if(!iMemberAddressBiz.insert(memberAddressBo))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }

        return "add success";
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String updateAddress(Long memberId,MemberAddressBo memberAddressBo) throws TaxjarException {
        if(!addressIsValid(memberAddressBo))
            throw new TaxjarException("邮编和地址不匹配");
        long areaId = memberAddressBo.getAreaId();
        if(areaId == 0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        BaseAreaBo queryBaseArea =  iBaseAreaBiz.selectById(areaId);
        if(queryBaseArea == null||queryBaseArea.getPid()==0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        //拼出地址
        String areaName = queryBaseArea.getName();
        if(queryBaseArea.getPid() != 0){
            areaName = areaName + "," +  iBaseAreaBiz.selectById(queryBaseArea.getPid()).getName();
        }

        MemberAddressBo query = iMemberAddressBiz.selectById(memberAddressBo.getId());
        if(query == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        //判断该用户是否只有一条地址信息
        EntityWrapper<MemberAddressBo> firstAddress = new EntityWrapper<>();
        firstAddress.eq(MemberAddressBo.Key.memberId.toString(), memberId);
        List<MemberAddressBo> addressBoList = iMemberAddressBiz.selectList(firstAddress);
        //如果不是第一次添加地址，判断该次添加是否设置默认
        if(addressBoList.size() > 1){
            //如果该次添加设置默认，将之前默认设置为-1
            if(memberAddressBo.getDefaultAddr() == 1){
                isFirst(memberId);
            }
            //插入该条记录
            memberAddressBo.setMemberId(memberId);
            memberAddressBo.setAreaName(areaName);
            if(!iMemberAddressBiz.updateById(memberAddressBo))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }//如果是第一次添加，强制设置为默认
        else if(addressBoList.size() == 1){
            memberAddressBo.setMemberId(memberId);
            memberAddressBo.setDefaultAddr(1);
            memberAddressBo.setAreaName(areaName);
            if(!iMemberAddressBiz.updateById(memberAddressBo))
                throw new WakaException(RavvExceptionEnum.INSERT_ERROR);
        }

        return "update success";
    }
    @Override
    public List<MemberAddressBo> getAddress(Long memberId){
        return  iMemberAddressBiz.selectList(Condition.create().eq(MemberAddressBo.Key.memberId.toString(),memberId));
    }

    public List<BaseAreaBo> getStates(){
        return  iBaseAreaBiz.selectList(Condition.create().eq( BaseAreaBo.Key.pid.toString(),0)
        .eq(BaseAreaBo.Key.countryCode.toString(),"USA"));
    }

    public List<BaseAreaBo> getCity(Long stateId){
            return iBaseAreaBiz.selectList(Condition.create().eq(BaseAreaBo.Key.pid.toString(),stateId)
            .eq(BaseAreaBo.Key.countryCode.toString(), "USA"));
    }

    @Override
    public MemberAddressBo getDefaultAddress(Long memberId) {
        EntityWrapper<MemberAddressBo> addressQuery = new EntityWrapper<>();
        addressQuery.eq(MemberAddressBo.Key.memberId.toString(), memberId);
        addressQuery.eq(MemberAddressBo.Key.defaultAddr.toString(), 1);
        MemberAddressBo addressBo = iMemberAddressBiz.selectOne(addressQuery);
        return addressBo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String delAddress(Long memberAddressId){
        MemberAddressBo memberAddressBo = iMemberAddressBiz.selectById(memberAddressId);
        if(memberAddressBo == null)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        iMemberAddressBiz.deleteById(memberAddressId);
        return "delete success";
    }



    private void isFirst(Long memberId){
        //如果该次添加设置默认，将之前默认设置为-1
        EntityWrapper<MemberAddressBo> addressQuery = new EntityWrapper<>();
        addressQuery.eq(MemberAddressBo.Key.memberId.toString(), memberId);
        addressQuery.eq(MemberAddressBo.Key.defaultAddr.toString(), 1);
        List<MemberAddressBo> listDefault = iMemberAddressBiz.selectList(addressQuery);
        if(listDefault.size() != 0){
            for(MemberAddressBo item: listDefault){
                item.setDefaultAddr(-1);
                if(!iMemberAddressBiz.updateById(item)){
                    throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
                }
            }
        }

    }

    //判断地址是否有效
    private boolean addressIsValid(MemberAddressBo memberAddressBo){
        Long storehouseId =  1071337415509893121L;
        StorehouseBo pfrom = iStorehouseBiz.selectById(storehouseId);
        TaxUtil.Address from,to ;{
            AreaFullVo vo = AreaFullVo.getAreaFullVo(pfrom.getAreaid(), iBaseAreaBiz);
            from = new TaxUtil.Address(vo.getProvinceBo().getShortName() ,pfrom.getZip());
            if(vo.getCityBo()!=null){
                from.setCity(vo.getCityBo().getName());
            }
            from.setStreet(pfrom.getAddress());
        }
        {
            AreaFullVo vo = AreaFullVo.getAreaFullVo(memberAddressBo.getAreaId(), iBaseAreaBiz);
            to = new TaxUtil.Address(vo.getProvinceBo().getShortName(), memberAddressBo.getZip());
            if(vo.getCityBo() != null){
                to.setCity(vo.getCityBo().getName());
            }
            to.setStreet(memberAddressBo.getAddress());
        }
        BigDecimal total = new BigDecimal(100.00);
        Tax tax = getTax(total, from, to);

        return true;
    }

    public static Tax getTax(BigDecimal amount, TaxUtil.Address from, TaxUtil.Address to){
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(D_From_country, isRequired(from.getCountry(),"国家"));
            params.put(D_From_zip, isRequired(from.getZip(),"邮编"));
            params.put(D_From_state,isRequired(from.getState(),"州"));
            params.put(D_From_city, isNull(from.getCity(),""));
            params.put(D_From_street, isNull(from.getStreet(),""));

            params.put(D_To_country,isRequired(to.getCountry(),"国家"));
            params.put(D_To_zip, isRequired(to.getZip(),"邮编"));
            params.put(D_To_state,isRequired(to.getState(),"州"));
            params.put(D_To_city, isNull(to.getCity(),""));
            params.put(D_To_street,isNull( to.getStreet(),""));

            String amounts = (amount == null ? "0":amount.toString());
            params.put(D_Amount, amounts);
            //String postFees =(postFee == null ? "0":postFee.toString());
            params.put(D_Shipping, "0");

            Taxjar client = new Taxjar("b08fd0d7ca7573e7fc95dd3070d628c0");
            TaxResponse res = client.taxForOrder(params);
            if(res == null){
                throw new WakaException("计税接口出错,返回null:");
            }
            //Float fx = res.tax.getAmountToCollect();
            //return new BigDecimal(fx);
            return res.tax ;//Tools.json.toJson(res.tax);

        } catch (TaxjarException e) {
            throw new WakaException("地址邮编不匹配，请重新输入",e);
        }
    }


    private static String isNull(String s,String def){
        return (Tools.string.isEmpty(s) ? def: s);
    }
    private static String isRequired(String s,String field){
        if(Tools.string.isEmpty(s)){
            throw new WakaException(field + "为空,不能计算税费");
        }
        return s;
    }


}
