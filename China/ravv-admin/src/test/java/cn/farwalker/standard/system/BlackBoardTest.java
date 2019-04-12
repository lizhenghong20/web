package cn.farwalker.standard.system;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.farwalker.ravv.service.sys.notice.dao.ISysNoticeMgrDao;
import cn.farwalker.standard.base.BaseJunit;

/**
 * 首页通知展示测试
 *
 * @author Jason Chen
 * @date 2017-11-21 15:02
 */
public class BlackBoardTest extends BaseJunit {

    @Autowired
    ISysNoticeMgrDao noticeDao;

    @Test
    public void blackBoardTest() {
        List<Map<String, Object>> notices = noticeDao.list(null);
        assertTrue(notices.size() > 0);
    }
}
