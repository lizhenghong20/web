package cn.farwalker.ravv.service.order.operationlog.biz.impl;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.farwalker.ravv.service.order.operationlog.biz.IOrderOperationLogBiz;
import cn.farwalker.ravv.service.order.operationlog.dao.IOrderOperationLogDao;
import cn.farwalker.ravv.service.order.operationlog.model.OrderOperationLogBo;

/**
 * 订单操作日志<br/>
 * <br/>
 * //手写的注释:以"//"开始 <br/>
 * @author generateModel.java
 */
@Service
public class OrderOperationLogBizImpl extends ServiceImpl<IOrderOperationLogDao,OrderOperationLogBo> implements IOrderOperationLogBiz{
}