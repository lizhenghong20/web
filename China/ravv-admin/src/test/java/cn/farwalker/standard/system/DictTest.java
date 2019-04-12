package cn.farwalker.standard.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import cn.farwalker.ravv.service.sys.dict.biz.ISysDictBiz;
import cn.farwalker.ravv.service.sys.dict.dao.ISysDictMgrDao;
import cn.farwalker.standard.base.BaseJunit;

/**
 * 字典服务测试
 *
 * @author Jason Chen
 * @date 2017-04-27 17:05
 */
public class DictTest extends BaseJunit {

    @Resource
    ISysDictBiz dictService;

    @Resource
    ISysDictMgrDao dictDao;

    @Test
    public void addTest() {
        String dictName = "这是一个字典测试";
        String dictValues = "1:测试1;2:测试2";
        dictService.addDict(dictName, dictValues);
    }

//    @Test
    public void editTest() {
        dictService.updateDict(16L, "测试", "1:测试1;2:测试2");
    }

//    @Test
    public void deleteTest() {
        this.dictService.deleteDict(16L);
    }

//    @Test
    public void listTest() {
        List<Map<String, Object>> list = this.dictDao.list("性别");
        Assert.assertTrue(list.size() > 0);
    }
}
