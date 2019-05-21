package cn.farwalker.ravv.service.member.address.biz;

import cn.farwalker.ravv.service.base.area.model.BaseAreaBo;
import cn.farwalker.ravv.service.member.address.model.MemberAddressBo;
import com.taxjar.exception.TaxjarException;

import java.util.List;

/**
 * Created by asus on 2018/12/13.
 */
public interface IMemberAddressService {
    public Long addAddress(Long memberId,MemberAddressBo memberAddressBo)  throws TaxjarException;
    public String delAddress(Long memberAddressId);

    public Long updateAddress(Long memberId,MemberAddressBo memberAddressBo) throws TaxjarException;
    public List<MemberAddressBo> getAddress(Long memberId);

    public List<BaseAreaBo> getStates();

    public List<BaseAreaBo> getCity(Long stateId);

    public MemberAddressBo getDefaultAddress(Long memberId);
}
