package cn.farwalker.ravv.service.member.address.biz.impl;

import cn.farwalker.ravv.service.base.area.biz.IBaseAreaBiz;
import cn.farwalker.ravv.service.base.area.model.AreaFullVo;
import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.base.config.biz.IBaseConfigBiz;
import cn.farwalker.ravv.service.base.config.model.BaseConfigBo;
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
import com.smartystreets.api.ClientBuilder;
import com.smartystreets.api.StaticCredentials;
import com.smartystreets.api.exceptions.SmartyException;
import com.smartystreets.api.us_street.Candidate;
import com.smartystreets.api.us_street.Client;
import com.smartystreets.api.us_street.Lookup;
import com.smartystreets.api.us_street.MatchType;
import com.taxjar.Taxjar;
import com.taxjar.exception.TaxjarException;
import com.taxjar.model.taxes.Tax;
import com.taxjar.model.taxes.TaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private IBaseConfigBiz baseConfigBiz;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addAddress(Long memberId,MemberAddressBo memberAddressBo){
        Candidate candidate = verifyAddress(memberAddressBo);
        String zipCode = candidate.getComponents().getZipCode();
        memberAddressBo.setZip(zipCode);
        long areaId = memberAddressBo.getAreaId();
        if(areaId == 0)
            throw new WakaException(RavvExceptionEnum.ADDRESS_IS_INVAILD);
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
    public String updateAddress(Long memberId,MemberAddressBo memberAddressBo){
        Candidate candidate = verifyAddress(memberAddressBo);
        String zipCode = candidate.getComponents().getZipCode();
        memberAddressBo.setZip(zipCode);
        long areaId = memberAddressBo.getAreaId();
        if(areaId == 0)
            throw new WakaException(RavvExceptionEnum.ADDRESS_IS_INVAILD);
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

    private Candidate verifyAddress(MemberAddressBo addressBo){
        //查出authId,authToken
        BaseConfigBo addressConfig = baseConfigBiz.selectOne(Condition.create()
                                                .eq(BaseConfigBo.Key.configType.toString(), "OUTOGENERATED"));
        //根据当前areaId查出城市名称
        BaseAreaBo city = iBaseAreaBiz.selectById(addressBo.getAreaId());
        if(city == null || city.getPid() == 0){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        String cityName = city.getName();
        //根据城市的父id查出州短名
        BaseAreaBo state = iBaseAreaBiz.selectById(city.getPid());
        if(state == null){
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        }
        String stateName = state.getShortName();
        //解析出街道1，街道2
        String[] allStreets = addressBo.getAddress().split(",");
        Lookup lookup = new Lookup();
//        lookup.setInputId("24601");
        lookup.setStreet(allStreets[0]);
        if(allStreets.length == 2){
            lookup.setStreet2(allStreets[1]);
        }
        lookup.setState(stateName);
        lookup.setCity(cityName);
        lookup.setMaxCandidates(1);
        lookup.setMatch(MatchType.STRICT);

        StaticCredentials credentials = new StaticCredentials(addressConfig.getAuthId(), addressConfig.getAuthToken());
        Client client = new ClientBuilder(credentials)
                .buildUsStreetApiClient();
        try {
            client.send(lookup);
        }
        catch (SmartyException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        ArrayList<Candidate> results = lookup.getResult();
        if (results.isEmpty()) {
            throw new WakaException(RavvExceptionEnum.ADDRESS_IS_INVAILD);
        }

        Candidate firstCandidate = results.get(0);
        return firstCandidate;
    }


}
