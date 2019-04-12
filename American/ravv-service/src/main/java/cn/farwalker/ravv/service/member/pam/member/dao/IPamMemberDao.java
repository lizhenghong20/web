package cn.farwalker.ravv.service.member.pam.member.dao;
import cn.farwalker.ravv.service.member.pam.member.model.TestParam;
import cn.farwalker.ravv.service.member.pam.member.model.TestVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import cn.farwalker.ravv.service.member.pam.member.model.PamMemberBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PAM_会员登录账号<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
public interface IPamMemberDao extends BaseMapper<PamMemberBo>{

    public TestVo getInfo();

    public int updatePointById(TestParam testVo);
}