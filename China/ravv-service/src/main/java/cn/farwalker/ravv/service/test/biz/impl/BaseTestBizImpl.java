package cn.farwalker.ravv.service.test.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.test.model.BaseTestBo;
import cn.farwalker.ravv.service.test.dao.IBaseTestDao;
import cn.farwalker.ravv.service.test.biz.IBaseTestBiz;

/**
 * 测试<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class BaseTestBizImpl extends ServiceImpl<IBaseTestDao,BaseTestBo> implements IBaseTestBiz{
}