package cn.farwalker.ravv.merchant;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.farwalker.ravv.service.merchant.biz.IMerchantBiz;
import cn.farwalker.ravv.service.merchant.biz.IMerchantService;
import cn.farwalker.ravv.service.merchant.model.MerchantBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;

/**
 * 供应商<br/>
 * 供应商 controller<br/>
 * //手写的注释:以"//"开始 <br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {
	private final static Logger log = LoggerFactory.getLogger(MerchantController.class);
	@Resource
	private IMerchantBiz merchantBiz;
	@Resource
	private IMerchantService merchantService;

	protected IMerchantBiz getBiz() {
		return merchantBiz;
	} 
	/**
	 * 取得单条记录
	 * 
	 * @param id
	 *            null<br/>
	 */
	@RequestMapping("/get")
	public JsonResult<MerchantBo> doGet(@RequestParam Long id) {
		// createMethodSinge创建方法
		if (id == null) {
			return JsonResult.newFail("id不能为空");
		}
		MerchantBo rs = getBiz().selectById(id);
		rs.setLogo(QiniuUtil.getFullPath(rs.getLogo()));
		rs.setLicense(QiniuUtil.getFullPath(rs.getLicense()));
		return JsonResult.newSuccess(rs);
	}
 

	/**
	 * 修改密码记录
	 * @param vo
	 */
	@RequestMapping("/pwd")
	public JsonResult<Boolean> doChangePassword( Long merchantId,String oldpwd,String newpwd) {
		// createMethodSinge创建方法
		MerchantBo bo =(merchantId == null ? null: merchantBiz.selectById(merchantId));
		if (bo == null) {
			return JsonResult.newFail("供应商不能为空");
		}
		String pwd = bo.getLoginPassword();
		if(pwd!=null && pwd.equalsIgnoreCase(oldpwd)){
			log.info("供应商修改了密码:" + newpwd);
			bo.setLoginPassword(newpwd);
			merchantBiz.updateById(bo);
			return JsonResult.newSuccess();
		}
		else{
			return JsonResult.newFail("旧密码不匹配");
		}
	}
}