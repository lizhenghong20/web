package cn.farwalker.ravv.admin.member;

import cn.farwalker.ravv.service.member.basememeber.model.MemberBo;
import cn.farwalker.ravv.service.member.basememeber.model.MemberVo;
import cn.farwalker.ravv.service.member.level.model.MemberLevelBo;
import com.baomidou.mybatisplus.plugins.Page;
import com.cangwu.frame.web.crud.QueryFilter;

import java.util.List;

public interface AdminMemberService {
    Boolean createMember(MemberBo vo);

    Boolean deleteMemebr(Long id);

    MemberVo getMember(Long id);

    Page<MemberVo> getMemberList(List<QueryFilter> query, Integer start, Integer size,
                           String sortfield);

    Boolean updateMember(MemberBo vo, Long userId);

    Boolean createMemberLevel( MemberLevelBo vo);

    Boolean deleteMemberLevel(Long id);

    MemberLevelBo getMemberLevel(Long id);

    Page<MemberLevelBo> getMemberLevelList(List<QueryFilter> query, Integer start, Integer size,
                                String sortfield);

    Object updateMemberLevel(MemberLevelBo vo);
}
