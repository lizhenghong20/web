package cn.farwalker.waka.qiniu;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;
import cn.farwalker.waka.oss.qiniu.QiniuUtil.UploadParamVo;

/**
 * 七牛上传框架
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/qiniu")
public class QiniuController{
	
	/**
	 * 取七牛的token
	 * @return
	 */
	@RequestMapping("/token")
	public JsonResult<UploadParamVo> getToken(String tablename ,String fieldname,String id) {
        QiniuUtil util = QiniuUtil.getInstance();
        UploadParamVo pv = util.getUploadParam(tablename, fieldname, id);
        return JsonResult.newSuccess(pv);
	}
	/**
	 * 取七牛的token
	 * @return
	 */
	@RequestMapping("/delete")
	public JsonResult<Boolean> doDelete(String url) {
        QiniuUtil util = QiniuUtil.getInstance();
        //UploadParamVo pv = util.getUploadParam(tablename, fieldname, id);
        String url2 = QiniuUtil.getRelativePath(url);
        util.deleteFileURL(url2);
        return JsonResult.newSuccess(Boolean.TRUE);
	}
}
