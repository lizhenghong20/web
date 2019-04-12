package cn.farwalker.standard.modular.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.standard.core.temp.BussinessLog;
import cn.farwalker.standard.core.temp.Const;
import cn.farwalker.standard.core.temp.LogObjectHolder;
import cn.farwalker.standard.core.temp.Permission;
import cn.farwalker.waka.core.BizExceptionEnum;
import cn.farwalker.waka.core.SuccessTip;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.core.ZTreeNode;
import cn.farwalker.waka.util.Tools;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.farwalker.ravv.service.sys.dict.biz.ISysDictBiz;
import cn.farwalker.ravv.service.sys.dict.dao.ISysDictDao;
import cn.farwalker.ravv.service.sys.dict.dao.ISysDictMgrDao;
import cn.farwalker.ravv.service.sys.dict.model.SysDictBo;
import cn.farwalker.standard.common.constant.dictmap.DictMap;
import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.standard.modular.system.warpper.DictWarpper;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 * 字典控制器
 *
 * @author Jason Chen
 * @Date 2017年4月26日 12:55:31
 */
@Controller
@RequestMapping("/dict")
public class DictController{

	private String PREFIX = "/system/dict/";

	@Resource
	ISysDictMgrDao dictDao;

	@Resource
	ISysDictDao dictMapper;

	@Resource
	ISysDictBiz dictService;

	/**
	 * 跳转到字典管理首页
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "dict.html";
	}

	/**
	 * 跳转到添加字典
	 */
	@RequestMapping("/dict_add")
	public String deptAdd() {
		return PREFIX + "dict_add.html";
	}

	/**
	 * 跳转到修改字典
	 */
	@Permission(Const.ADMIN_NAME)
	@RequestMapping("/dict_edit/{dictId}")
	public String deptUpdate(@PathVariable Long dictId, Model model) {
		SysDictBo dict = dictMapper.selectById(dictId);
		model.addAttribute("dict", dict);
		List<SysDictBo> subDicts = dictMapper.selectList(new EntityWrapper<SysDictBo>().eq(SysDictBo.Key.pid.toString(), dictId));
		model.addAttribute("subDicts", subDicts);
		LogObjectHolder.me().set(dict);
		return PREFIX + "dict_edit.html";
	}

	/**
	 * 通过字典名称获取对应字典
	 */
	@RequestMapping("/dict_list/{dictName}")
	@ResponseBody
	public Object deptList(@PathVariable String dictName, Model model) {
		SysDictBo d = new SysDictBo();
		d.setName(dictName);
		d.setPid(0L);
		SysDictBo dict = dictMapper.selectOne(d);
		model.addAttribute("dict", dict);
		return dictMapper.selectList(new EntityWrapper<SysDictBo>().eq("pid", dict.getId()));
	}
	
	/**
	 * 通过字典名称获取对应枚举返回 ztree数据结构
	 */
	@RequestMapping("/dict_tree/{dictName}")
	@ResponseBody
	public Object deptTreeList(@PathVariable String dictName, Model model) {
		SysDictBo d = new SysDictBo();
		d.setName(dictName);
		d.setPid(0L);
		SysDictBo dict = dictMapper.selectOne(d);
		List<SysDictBo> dictList =  dictMapper.selectList(new EntityWrapper<SysDictBo>().eq(SysDictBo.Key.pid.toString(), dict.getId()));
		List<ZTreeNode> deptZtree = new ArrayList<ZTreeNode>();
		if (CollectionUtils.isNotEmpty(dictList)) {
			for (SysDictBo i : dictList) {
				ZTreeNode deptZtreeNode = new ZTreeNode();
				deptZtreeNode.setId(i.getNum().longValue());
				deptZtreeNode.setpId(0l);
				deptZtreeNode.setName(i.getName());
				deptZtreeNode.setIsOpen(false);
				deptZtreeNode.setChecked(false);
				deptZtree.add(deptZtreeNode);
			}
			return deptZtree;
		}
		return new ArrayList<ZTreeNode>();
	}
	
	/**
	 * 新增字典
	 *
	 * @param dictValues
	 *            格式例如 "1:启用;2:禁用;3:冻结"
	 */
	@BussinessLog(value = "添加字典记录", key = "dictName,dictValues", dict = DictMap.class)
	@RequestMapping(value = "/add")
	@Permission(Const.ADMIN_NAME)
	@ResponseBody
	public Object add(String dictName, String dictValues) {
		if (Tools.string.isEmpty(dictValues) || Tools.string.isEmpty(dictName)) {
			throw new WakaException(BizExceptionEnum.REQUEST_NULL);
		}
		this.dictService.addDict(dictName, dictValues);
		return new SuccessTip();
	}

	/**
	 * 获取所有字典列表
	 */
	@RequestMapping(value = "/list")
	@Permission(Const.ADMIN_NAME)
	@ResponseBody
	public Object list(String condition) {
		List<Map<String, Object>> list = this.dictDao.list(condition);
		return new DictWarpper(list).warp();
	}

	/**
	 * 字典详情
	 */
	@RequestMapping(value = "/detail/{dictId}")
	@Permission(Const.ADMIN_NAME)
	@ResponseBody
	public Object detail(@PathVariable("dictId") Long dictId) {
		return dictMapper.selectById(dictId);
	}

	/**
	 * 修改字典
	 */
	@BussinessLog(value = "修改字典", key = "dictName,dictValues", dict = DictMap.class)
	@RequestMapping(value = "/update")
	@Permission(Const.ADMIN_NAME)
	@ResponseBody
	public Object update(Long dictId, String dictName, String dictValues) {
		if (Tools.string.isEmpty(dictValues) || Tools.string.isEmpty(dictName) ||  dictId == null) {
			throw new WakaException(BizExceptionEnum.REQUEST_NULL);
		}
		dictService.updateDict(dictId, dictName, dictValues);
		return new SuccessTip();
	}

	/**
	 * 删除字典记录
	 */
	@BussinessLog(value = "删除字典记录", key = "dictId", dict = DictMap.class)
	@RequestMapping(value = "/delete")
	@Permission(Const.ADMIN_NAME)
	@ResponseBody
	public Object delete(@RequestParam Long dictId) {

		// 缓存被删除的名称
		LogObjectHolder.me().set(ConstantFactory.me().getDictName(dictId));

		this.dictService.deleteDict(dictId);
		return new SuccessTip();
	}

}
