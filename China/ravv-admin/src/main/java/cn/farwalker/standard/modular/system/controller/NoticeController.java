package cn.farwalker.standard.modular.system.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.farwalker.standard.core.temp.BussinessLog;
import cn.farwalker.standard.core.temp.LogObjectHolder;
import cn.farwalker.waka.core.BizExceptionEnum;
import cn.farwalker.waka.core.HttpKit;
import cn.farwalker.waka.core.SuccessTip;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.farwalker.ravv.service.sys.notice.dao.ISysNoticeDao;
import cn.farwalker.ravv.service.sys.notice.dao.ISysNoticeMgrDao;
import cn.farwalker.ravv.service.sys.notice.model.SysNoticeBo;
import cn.farwalker.standard.common.constant.dictmap.NoticeMap;
import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.standard.core.shiro.ShiroKit;
import cn.farwalker.standard.modular.system.warpper.NoticeWrapper;
/**
 * 通知控制器
 *
 * @author Jason Chen
 * @Date 2017-11-09 23:02:21
 */
@Controller
@RequestMapping("/notice")
public class NoticeController{

    private String PREFIX = "/system/notice/";

    @Resource
    private ISysNoticeDao noticeMapper;

    @Resource
    private ISysNoticeMgrDao noticeDao;

    /**
     * 跳转到通知列表首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "notice.html";
    }

    /**
     * 跳转到添加通知
     */
    @RequestMapping("/notice_add")
    public String noticeAdd() {
        return PREFIX + "notice_add.html";
    }

    /**
     * 跳转到修改通知
     */
    @RequestMapping("/notice_update/{noticeId}")
    public String noticeUpdate(@PathVariable Integer noticeId, Model model) {
        SysNoticeBo notice = this.noticeMapper.selectById(noticeId);
        model.addAttribute("notice",notice);
        LogObjectHolder.me().set(notice);
        return PREFIX + "notice_edit.html";
    }

    /**
     * 跳转到首页通知
     */
    @RequestMapping("/hello")
    public String hello() {
        List<Map<String, Object>> notices = noticeDao.list(null);
        HttpKit.getRequest().getSession().setAttribute("noticeList",notices);
        return "/blackboard.html";
    }

    /**
     * 获取通知列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        List<Map<String, Object>> list = this.noticeDao.list(condition);
        return new NoticeWrapper(list).warp();
    }

    /**
     * 新增通知
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增通知",key = "title",dict = NoticeMap.class)
    public Object add(SysNoticeBo notice) {
        if (notice == null || Tools.string.isEmpty(notice.getTitle()) || Tools.string.isEmpty(notice.getContent())) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        notice.setCreater(ShiroKit.getUser().getId());
        notice.insert();
        return new SuccessTip();
    }

    /**
     * 删除通知
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @BussinessLog(value = "删除通知",key = "noticeId",dict = NoticeMap.class)
    public Object delete(@RequestParam Long noticeId) {

        //缓存通知名称
        LogObjectHolder.me().set(ConstantFactory.me().getNoticeTitle(noticeId));

        this.noticeMapper.deleteById(noticeId);

        return new SuccessTip();
    }

    /**
     * 修改通知
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改通知",key = "title",dict = NoticeMap.class)
    public Object update(SysNoticeBo notice) {
        if (notice == null || notice.getId() == null || Tools.string.isEmpty(notice.getTitle()) || Tools.string.isEmpty(notice.getContent())) {
            throw new WakaException(BizExceptionEnum.REQUEST_NULL);
        }
        SysNoticeBo old = this.noticeMapper.selectById(notice.getId());
        old.setTitle(notice.getTitle());
        old.setContent(notice.getContent());
        old.updateById();
        return new SuccessTip();
    }

}
