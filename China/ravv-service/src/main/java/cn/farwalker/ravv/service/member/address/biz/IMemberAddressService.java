package cn.farwalker.ravv.service.member.address.biz;

import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.member.address.model.MemberAddressBo;
import com.taxjar.exception.TaxjarException;

import java.util.List;

/**
 * Created by asus on 2018/12/13.
 */
public interface IMemberAddressService {
    public String addAddress(Long memberId,MemberAddressBo memberAddressBo) ;
    public String delAddress(Long memberAddressId);

    public String updateAddress(Long memberId,MemberAddressBo memberAddressBo) ;
    public List<MemberAddressBo> getAddress(Long memberId);

    public List<BaseAreaBo> getProvince();

    public List<BaseAreaBo> getCity(Long provinceId);

    public List<BaseAreaBo> getArea(Long cityId);

    public MemberAddressBo getDefaultAddress(Long memberId);

    public String setFullPath();
}
