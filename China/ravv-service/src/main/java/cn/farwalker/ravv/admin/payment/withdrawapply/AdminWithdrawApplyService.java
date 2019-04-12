package cn.farwalker.ravv.admin.payment.withdrawapply;

import cn.farwalker.ravv.admin.payment.withdrawapply.dto.MemberWithdrawApplyVo;
import cn.farwalker.ravv.service.payment.withdrawapply.model.MemberWithdrawApplyBo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.List;

public interface AdminWithdrawApplyService {
    Boolean create(MemberWithdrawApplyBo vo);

    Boolean delete(Long id);

    MemberWithdrawApplyVo getOne(Long id);

    Page<MemberWithdrawApplyVo> getList(List<QueryFilter> query, Integer start, Integer size,
                                        String sortfield);
}
