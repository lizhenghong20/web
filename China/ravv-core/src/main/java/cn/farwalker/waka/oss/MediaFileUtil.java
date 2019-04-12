package cn.farwalker.waka.oss;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import cn.farwalker.waka.core.WakaObject;
import cn.farwalker.waka.oss.qiniu.QiniuProperties;
import cn.farwalker.waka.util.Tools;

public class MediaFileUtil {

	public static final String ImageType = ".jpg.jpeg.png.dwt.dwg.dws.dxf.psd.ai.rar.zip.cdr.eps";

	public static final String HTTP_1="http://";
	public static final String HTTP_2="HTTP://";
	public static final String HTTPS_1="https://";
	public static final String HTTPS_2="HTTPS://";

	/** 本地文件位置 */
	public static final String D_LocalRoot = "mediafile/";

	private static String domain;
	private MediaFileUtil(){

	}
	/** 下载取得安全访问链接(使用对象可以无缝切换oss)(为了兼容以前的AliOss) */
	public static StringBuilder getHttpURL (String resurl, boolean download) {
	    if(Tools.string.isEmpty(resurl)){
	        return null;
	    }
		StringBuilder sb = new StringBuilder();
		if(resurl.startsWith(HTTP_1)||resurl.startsWith(HTTP_2)||
				resurl.startsWith(HTTPS_1)||resurl.startsWith(HTTPS_2)){
			sb.append(resurl);
		}else{
			sb.append(getQiniuAddress()).append(resurl);
			if (download) {
				// https://developer.qiniu.com/kodo/manual/download-setting
				// 对于qiniu, 如果下载, 可以通过attname 参数来指定下载文件名, 对于文件名中的中文, 需要进行编码处理
				final int index = resurl.lastIndexOf("/");
				final String name = resurl.substring(index + 1);
				final String encodedName = Tools.encode.utf8UrlEncode(name);
				sb.append("?attname=").append(encodedName);
			}
		}

        return sb;
    }


	private static String getQiniuAddress() {
		if (Tools.string.isEmpty(domain)) {
			QiniuProperties pro = Tools.springContext.getBean(QiniuProperties.class);
			domain = pro.getDomain();
		}
		return domain;
	}

	/** 下载取得安全访问链接 ,统一使用 getHttpURL()*/
	/*public static StringBuilder getHttpURLDownLoad(String url) {
		AliOssClient client = AliOssClient.getInstance();
		StringBuilder sb = client.getOpenStorageURL(ContentType.STREAM, url);
		return sb;
	}*/

	/** 检查文件名及扩展名,如果有汉字文件名，则返回随机数 */
	public static String checkExtname(String name, WakaObject<String> outmsg) {
		int idx = name.lastIndexOf('.');
		String sname = "", ext = name;
		if (idx > 0) {
			sname = name.substring(0, idx).toLowerCase();
			if (sname.getBytes(Charset.forName("UTF-8")).length != sname.length()) {
				sname = "a" + String.valueOf((int) (Math.random() * 10000));
			}
			ext = name.substring(idx + 1).toLowerCase();
		}

		if (ImageType.indexOf(ext) >= 0) {
			return sname + "." + ext;
		} else {
			outmsg.setValue("模板只支持:" + ImageType);
			return null;
		}
	}

	/** 判断是否图片文件 */
	public static boolean isImage(String fileName) {
		if (Tools.string.isEmpty(fileName)) {
			return false;
		}
		String name = fileName.toLowerCase();
		return (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".bmp") || name.endsWith(".gif"));
	}

	public static String getExtname(String name, WakaObject<String> outmsg) {
		int idx = name.lastIndexOf('.');
		String sname = "", ext = name;
		if (idx > 0) {
			sname = name.substring(0, idx).toLowerCase();
			if (sname.getBytes(Charset.forName("UTF-8")).length != sname.length()) {
				sname = "a" + String.valueOf((int) (Math.random() * 10000));
			}
			ext = name.substring(idx + 1);
		}
		return ext;
	}

	/**
	 * 取得本地文件
	 *
	 * @param modelName
	 *            模块名
	 * @param id
	 *            id
	 * @param field
	 *            字段名
	 * @return
	 */
	public static List<String> getMediaLocal(String modelName, String id, String field) {
		StringBuilder path = getPathHas(modelName, id, field);
		File fp = new File(path.toString());
		List<String> rs = new ArrayList<String>();

		if (fp.exists()) {
			String[] fs = fp.list();
			for (String f : fs) {
				rs.add(path.toString() + f);
			}
		}
		return rs;
	}

	/**
	 * 取得本地文件存放路径
	 *
	 * @param modelName
	 *            模块名
	 * @param id
	 *            id
	 * @param field
	 *            字段名
	 * @return
	 */
	public static StringBuilder getPathHas(String modelName, String id, String field) {
		String p;
		try {
			p = Tools.env.getRootPath();
		} catch (OssException e) {
			p = Tools.env.getRootPath(true, MediaFileUtil.class);
		}

		int hashcode = Math.abs(id.hashCode()) % (1024 * 4);// 求余
		if (Tools.string.isEmpty(field)) {
			field = "_emptyfield";
		}
		StringBuilder path = new StringBuilder(p);
		path.append(MediaFileUtil.D_LocalRoot).append(modelName.trim().toLowerCase()).append('/').append(field.trim().toLowerCase()).append("_has")
				.append(hashcode).append('/').append(id).append('/');
		return path;
	}
}
