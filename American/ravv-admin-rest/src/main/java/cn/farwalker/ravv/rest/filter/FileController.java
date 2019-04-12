//package cn.farwalker.ravv.rest.filter;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import cn.farwalker.waka.config.properties.WakaProperties;
//import cn.farwalker.waka.core.base.controller.BaseController;
//import cn.farwalker.waka.core.util.FileUtil;
//
//@RestController
//@RequestMapping("/file")
//public class FileController{
//
//	@Resource
//    private WakaProperties wakaProperties;
//
//	@Resource
//	private HttpServletResponse response;
//
//	/**
//	 * 获取图片
//	 * @throws InvocationTargetException
//	 * @throws IllegalAccessException
//	 */
//	@RequestMapping(value = "/image/{imageName}")
//	public void get(@PathVariable("imageName") String imageName) {
//		String path = wakaProperties.getFileUploadPath() + imageName + ".jpg";
//		System.out.println("######################################");
//		System.out.println("file path: " + path);
//        try {
//            byte[] bytes = FileUtil.toByteArray(path);
//            response.getOutputStream().write(bytes);
//        }catch (Exception e){
//            //如果找不到图片就返回一个默认图片
//            try {
//                response.sendRedirect("/static/img/girl.gif");
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//	}
//}
