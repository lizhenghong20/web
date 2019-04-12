package cn.farwalker.ravv.admin.profitallot;

import cn.farwalker.ravv.admin.profitallot.dto.MemberPromoteInfoVo;
import cn.farwalker.ravv.admin.profitallot.dto.MemberRelativeTreeVo;
import cn.farwalker.ravv.admin.profitallot.dto.RebatedMonthVo;
import cn.farwalker.ravv.service.member.basememeber.constants.MemberSubordinate;
import cn.farwalker.ravv.service.member.basememeber.model.MemberSimpleInfoVo;
import cn.farwalker.ravv.service.sale.profitallot.constants.DistStatusEnum;
import cn.farwalker.ravv.service.sale.profitallot.model.ProfitAllotInfoVo;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.Date;

public interface AdminProfitallotService {

    MemberPromoteInfoVo getPromoteInfo(Long memberId);

    MemberRelativeTreeVo getRelativeTree(Long memberId, Integer start, Integer size);

    Page<MemberSimpleInfoVo> getRelativeByLevel(Long memberId, MemberSubordinate subordinate,
                                                 Integer start, Integer size);

    Page<ProfitAllotInfoVo> getRebatedListByStatus(Long memberId, DistStatusEnum status,
                                                    Integer start, Integer size);

    Page<RebatedMonthVo> getRebatedList(Long memberId, Integer start, Integer size);

    Page<RebatedMonthVo> getMonthRebatedlist(Long memberId, Date month, Integer start, Integer size);
}
