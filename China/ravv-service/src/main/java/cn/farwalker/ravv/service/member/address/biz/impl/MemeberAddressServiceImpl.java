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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/12/13.
 */
@Service
public class MemeberAddressServiceImpl implements IMemberAddressService {
    @Autowired
    private IMemberAddressBiz iMemberAddressBiz;

    @Autowired
    private IBaseAreaBiz iBaseAreaBiz;

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String addAddress(Long memberId,MemberAddressBo memberAddressBo){
        long areaId = memberAddressBo.getAreaId();
        if(areaId == 0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        BaseAreaBo queryBaseArea =  iBaseAreaBiz.selectById(areaId);
        if(queryBaseArea == null||queryBaseArea.getPid()==0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        //拼出地址
        String areaName = appendAreaName(queryBaseArea);

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
        long areaId = memberAddressBo.getAreaId();
        if(areaId == 0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        BaseAreaBo queryBaseArea =  iBaseAreaBiz.selectById(areaId);
        if(queryBaseArea == null||queryBaseArea.getPid()==0)
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        //拼出地址
        String areaName = appendAreaName(queryBaseArea);
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

    public List<BaseAreaBo> getProvince(){
        return  iBaseAreaBiz.selectList(Condition.create().eq( BaseAreaBo.Key.pid.toString(),0)
        .eq(BaseAreaBo.Key.countryCode.toString(),"CHINA"));
    }

    public List<BaseAreaBo> getCity(Long provinceId){
            return iBaseAreaBiz.selectList(Condition.create().eq(BaseAreaBo.Key.pid.toString(),provinceId)
            .eq(BaseAreaBo.Key.countryCode.toString(), "CHINA"));
    }

    /**
     * @description: 获取县/区列表
     * @param:
     * @return list
     * @author Mr.Simple
     * @date 2019/4/15 10:38
     */
    @Override
    public List<BaseAreaBo> getArea(Long cityId) {
        return iBaseAreaBiz.selectList(Condition.create().eq(BaseAreaBo.Key.pid.toString(), cityId));
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
    public String setFullPath() {
        //先获取到所有直辖市和省份信息，设置fullpath
        List<BaseAreaBo> firstLevel = iBaseAreaBiz.selectList(Condition.create()
                                                    .ne(BaseAreaBo.Key.pid.toString(), 0L));
        if(firstLevel.size() == 0){
            throw new WakaException(RavvExceptionEnum.SELECT_ERROR);
        }
        List<BaseAreaBo> updateBo = new ArrayList<>();
        firstLevel.forEach(item->{
            BaseAreaBo baseAreaBo = new BaseAreaBo();
            BeanUtils.copyProperties(item,baseAreaBo);
            BaseAreaBo first = new BaseAreaBo();
            BaseAreaBo second = new BaseAreaBo();
            BaseAreaBo third = new BaseAreaBo();
            StringBuffer stringBuffer = new StringBuffer();
            //查找上级
            if(baseAreaBo.getPid() != 0){
                first = iBaseAreaBiz.selectById(baseAreaBo.getPid());
                if(first == null){
                    throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "baseAreaBo.getId()" + baseAreaBo.getId());
                }
                if(first.getPid() != 0){
                    second = iBaseAreaBiz.selectById(first.getPid());
                    if(second == null){
                        throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "first.getId()" + first.getId());
                    }
                    if(second.getPid() != 0){
                        third = iBaseAreaBiz.selectById(second.getPid());
                    } else{
                        stringBuffer.append(second.getId().toString())
                                    .append("/")
                                    .append(first.getId())
                                    .append("/")
                                    .append(baseAreaBo.getId());
                    }

                } else {
                    stringBuffer.append(first.getId())
                                .append("/")
                                .append(baseAreaBo.getId());
                }
            }
            baseAreaBo.setFullPath(stringBuffer.toString());
            updateBo.add(baseAreaBo);
        });
        if(!iBaseAreaBiz.updateBatchById(updateBo)){
            throw new WakaException(RavvExceptionEnum.UPDATE_ERROR);
        }
        return "success";
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

    private String appendAreaName(BaseAreaBo queryBaseArea){
        BaseAreaBo first = new BaseAreaBo();
        BaseAreaBo second = new BaseAreaBo();
        StringBuffer stringBuffer = new StringBuffer(queryBaseArea.getName());
        //查找上级
        if(queryBaseArea.getPid() != 0){
            first = iBaseAreaBiz.selectById(queryBaseArea.getPid());
            if(first == null){
                throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "baseAreaBo.getId()" + queryBaseArea.getId());
            }
            stringBuffer.append(",").append(first.getName());
            if(first.getPid() != 0){
                second = iBaseAreaBiz.selectById(first.getPid());
                if(second == null){
                    throw new WakaException(RavvExceptionEnum.SELECT_ERROR + "first.getId()" + first.getId());
                }
                stringBuffer.append(",").append(second.getName());

            }
        }
        return stringBuffer.toString();
    }


}
