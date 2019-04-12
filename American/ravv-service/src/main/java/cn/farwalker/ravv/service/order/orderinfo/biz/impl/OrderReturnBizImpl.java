package cn.farwalker.ravv.service.order.orderinfo.biz.impl;

import cn.farwalker.ravv.service.order.orderinfo.biz.IOrderReturnBiz;

/**
 */
public class OrderReturnBizImpl implements IOrderReturnBiz{
//	@Resource
//	private IOrderReturnDetailInfoDAO orderReturnDetailInfoDAO;
//	@Resource
//	private IOrderReturnInfoDAO orderReturnInfoDAO;
//	
//	@Resource
//	private PlatformTransactionManager transactionManager;
//
//
//	@Override
//	public OrderSalesReturnsDO getOrderByOrderId(Long id) {		
//		return orderReturnInfoDAO.findOrderInfoDOByPrimaryKey(id);
//	}
//
//	@Override
//	public List<OrderSalesReturnsDetailDO> getOrderGoodsInfoListByOrderId(
//			Long id) {
//		if(id == null || id < 0){
//			return null;
//		}
//		OrderSalesReturnsDetailDO DO = new OrderSalesReturnsDetailDO();
//		DO.setReturnsId(id);
//		return orderReturnDetailInfoDAO.findListByDO(DO);
//	}
//
//	@Override
//	public List<OrderSalesReturnsDO> queryOrderReturnsList(
//			QueryOrderReturnDO query) {
//		List<OrderSalesReturnsDO> orderList=new ArrayList<OrderSalesReturnsDO>();
//		/*if (query.isNeedPageTotal()) {
//			query.setPageNum(orderReturnDetailInfoDAO.findCountByQuery(query));
//		}*/
//		List<Long> ids=orderReturnDetailInfoDAO.findPageOrderIdsByQuery(query);
//		if(CollectionUtils.isEmpty(ids)){
//			return orderList;
//		}
//		for (Long id : ids) {
//			OrderSalesReturnsDO order=orderReturnInfoDAO.findOrderInfoDOByPrimaryKey(id);
//			orderList.add(order);
//		}
//		return orderList;
//	}
//
//	@Override
//	public List<OrderSalesReturnsDetailDO> queryOrderDetailList(
//			QueryOrderReturnDO query) {
//		List<OrderSalesReturnsDetailDO> orderDetailList=null;
//		orderDetailList = orderReturnDetailInfoDAO.findListByQuery(query);
//		return orderDetailList;
//	}
//
//	@Override
//	public BooleanResult updateOrderReturn(OrderSalesReturnsDO DO) {
//		BooleanResult res = new BooleanResult();
//		return res;
//	}
//	
//	
//	@Override
//	public BooleanResult refundConfirmByOrder(List<OrderSalesReturnsDO> orders) {
//		BooleanResult res = new BooleanResult();
//		
//		TransactionDefinition def = new DefaultTransactionDefinition(
//				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//		TransactionStatus status = transactionManager.getTransaction(def);
//		
//		try{
//			
//		for (OrderSalesReturnsDO orderSalesReturnsDO : orders) {
//			OrderSalesReturnsDO DO =  orderReturnInfoDAO.findOrderInfoDOByPrimaryKey(orderSalesReturnsDO.getReturnsId());
//			if(DO == null ){
//				res.addErrorMsg(ErrorConstants.CAN_NOT_FIND_DB_RECORD, "审核对象为空");
//				return res;
//			}
//			//检验修改对象是否属于当前者
//			if(!DO.getSellerId().equals(orderSalesReturnsDO.getSellerId())){
//				res.addErrorMsg("ORDER_RETURN_UPDATE_EXCEPTION", "操作异常");
//				return res;
//			}
//			
//			
//			//更新主单状态
//			DO.setStatus(ReturnsStatusEnum.RETURNS_FINISH.getType());
//			DO.setFinishTime(new Date());	
//			orderReturnInfoDAO.updateOrderInfoDO(DO);
//			//同步退货主表
//			McbfSellth th = new McbfSellth();
//			th.setId(DO.getTshSellthId());
//			th.setWcdate(new Date());
//			th.setState(4);
//			orderReturnInfoDAO.refundConfirm(th);
//			//确认子单
//			QueryOrderReturnDO query = new QueryOrderReturnDO();
//			query.setOrderId(DO.getReturnsId());
//			query.setNeedPagination(false);
//			query.setSellerId(DO.getSellerId());			
//			List<OrderSalesReturnsDetailDO> orderDetials=orderReturnDetailInfoDAO.findListByQuery(query);
//				for (int i=0;i<orderDetials.size();i++) {
//					
//					OrderSalesReturnsDetailDO  orderSalesReturnsDetailDO=orderDetials.get(i);
//					if(orderSalesReturnsDetailDO.getStatus()==ReturnsStatusEnum.UN_SEND.getType()||orderSalesReturnsDetailDO.getStatus()==ReturnsStatusEnum.RETURNS_FINISH.getType()){
//						continue;
//					}
//					orderSalesReturnsDetailDO.setFinishTime(new Date());
//					orderSalesReturnsDetailDO.setStatus(ReturnsStatusEnum.RETURNS_FINISH.getType());
//					orderReturnDetailInfoDAO.updateOrderInfoDO(orderSalesReturnsDetailDO);
//					//同步退货明细表
//					McbfSellths ths = new McbfSellths();
//					ths.setId(orderSalesReturnsDetailDO.getTshSellthsId());
//					ths.setLogissate(2); 
//					orderReturnDetailInfoDAO.refundConfirm(ths);		
//				}
//			}
//		 	transactionManager.commit(status);
//		}catch(Exception e){
//			res.addErrorMsg("BRAND_UPDATE_EXCEPTION", "操作异常");
//			status.setRollbackOnly();
//			transactionManager.rollback(status);
//		}
//		return res;
//	}
//	
//	/**
//	 * 确认明细收货
//	 */
//	@Override
//	public BooleanResult refundConfirmByOrderDetail(List<OrderSalesReturnsDetailDO> orderDetials) {
//		
//		BooleanResult res = new BooleanResult();
//		TransactionDefinition def=null;
//		TransactionStatus status=null;
//		//开启事务	
//		def = new DefaultTransactionDefinition(
//				TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//		status = transactionManager.getTransaction(def);	
//		try{
//		for (int i=0;i<orderDetials.size();i++) {
//			OrderSalesReturnsDetailDO orderSalesReturnsDetailDO=orderDetials.get(i);
//			OrderSalesReturnsDetailDO DO = orderReturnDetailInfoDAO.findOrderReturnInfoDOByPrimaryKey(orderSalesReturnsDetailDO.getId());
//			if(DO.getStatus()==ReturnsStatusEnum.RETURNS_FINISH.getType()){
//				continue;
//			}
//			DO.setFinishTime(new Date());
//			DO.setStatus(ReturnsStatusEnum.RETURNS_FINISH.getType());
//			orderReturnDetailInfoDAO.updateOrderInfoDO(DO);
//			//同步退货明细表
//			McbfSellths ths = new McbfSellths();
//			ths.setId(DO.getTshSellthsId());
//			ths.setLogissate(2); 
//			orderReturnDetailInfoDAO.refundConfirm(ths);
//				OrderSalesReturnsDetailDO checkDO = new OrderSalesReturnsDetailDO();
//				checkDO.setReturnsId(DO.getReturnsId());
//				checkDO.setSellerId(DO.getSellerId());
//				//如果主单下面不存在未确认的 修改主单状态
//				if(!checkOrderStatus(checkDO)){
//					OrderSalesReturnsDO orderDO = orderReturnInfoDAO.findOrderInfoDOByPrimaryKey(DO.getReturnsId());				
//					orderDO.setStatus(ReturnsStatusEnum.RETURNS_FINISH.getType());
//					orderDO.setFinishTime(new Date());			
//					orderReturnInfoDAO.updateOrderInfoDO(orderDO);
//					//判断主表下是否还有别的供应商未签收
//					int count=orderReturnInfoDAO.checkReturnBySupplier(orderDO.getTshSellthId());
//					if(count==0){
//						//同步主表退货表
//						McbfSellth th = new McbfSellth();
//						th.setId(orderDO.getTshSellthId());
//						th.setWcdate(new Date());
//						th.setState(4);
//						orderReturnInfoDAO.refundConfirm(th);
//					}
//				}	
//		}
//		transactionManager.commit(status);
//		}catch(Exception e){
//			res.addErrorMsg("BRAND_UPDATE_EXCEPTION", "操作异常");
//			status.setRollbackOnly();
//			transactionManager.rollback(status);
//			return res;	
//		}		
//		return res;
//	}
//	/**
//	 * 检查主单下是否还存在未确认的子单
//	 */
//	@Override
//	public boolean checkOrderStatus(OrderSalesReturnsDetailDO DO) {
//		DO.setStatus(ReturnsStatusEnum.RETURNS_FINISH.getType());
//		return orderReturnDetailInfoDAO.checkOrderDetailStatus(DO);
//	}
//
//	@Override
//	public PageDO<OrderSalesReturnsDetailDO> geOrderReturnList(OrderSalesReturnsDetailDO returnDo) {
//
//		return orderReturnDetailInfoDAO.findPageDOByDO(returnDo);
//	}

}
