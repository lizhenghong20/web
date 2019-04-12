package cn.farwalker.ravv.sysdict;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.farwalker.ravv.service.sys.dict.biz.ISysDictBiz;
import cn.farwalker.ravv.service.sys.dict.model.SysDictBo;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.util.Tools;

/**
 * 字典<br/>
 * 
 * @author generateModel.java
 */
@RestController
@RequestMapping("/dict")
public class DictController {
	private final static Logger log = LoggerFactory.getLogger(DictController.class);

	@Resource
	private ISysDictBiz sysDictBiz;

	/**
	 * 通过名称获取获取字典数据
	 * 
	 * @param dictName
	 * @return
	 */
	@RequestMapping("/dict_list")
	@ResponseBody
	public JsonResult<List<SysDictBo>> getDictListByName(String dictName) {
		if (Tools.string.isEmpty(dictName)) {
			return JsonResult.newFail("订单id(不能是顶级订单id)不能为空");
		}
		EntityWrapper<SysDictBo> wp = new EntityWrapper<SysDictBo>(null);
		wp.addFilter("pid in (select id from sys_dict where name = {0})", dictName);
		wp.orderBy(SysDictBo.Key.num.toString());
		return JsonResult.newSuccess(sysDictBiz.selectList(wp));
	}
}