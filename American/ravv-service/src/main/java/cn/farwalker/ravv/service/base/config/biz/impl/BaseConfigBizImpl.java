package cn.farwalker.ravv.service.base.config.biz.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import cn.farwalker.ravv.service.base.config.model.BaseConfigBo;
import cn.farwalker.ravv.service.base.config.dao.IBaseConfigDao;
import cn.farwalker.ravv.service.base.config.biz.IBaseConfigBiz;

/**
 * auth配置类<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class BaseConfigBizImpl extends ServiceImpl<IBaseConfigDao,BaseConfigBo> implements IBaseConfigBiz{
}