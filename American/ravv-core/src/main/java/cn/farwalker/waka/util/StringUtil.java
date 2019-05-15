package cn.farwalker.waka.util;

import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.oss.qiniu.QiniuUtil;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author Administrator
 */
public class StringUtil {

    /** 因为类必须为public，所以只能把构造函数给这样控制 */
    StringUtil() {

    }

    public static void main(String[] args) {
        String s = "1";
        StringUtil stringUtil = new StringUtil();
        List<String> list = stringUtil.convertStringList(s);
        System.out.println(list.size());
        list.forEach(item->{
            System.out.println(item);
        });
//        List<String> lists = Arrays.asList("a", "b", "c", "d");
//        StringUtil u = new StringUtil();
//        StringBuilder sb = u.join(lists, "!-xxx=!");
//        System.out.println(sb.toString());
    }

    /**
     * 字符串合并（不忽略空元素）,如果元素为空也不忽略此元素
     *
     * @param strs
     * @param split 可以为空
     * @return
     */
    public StringBuilder join(Collection<String> strs, String split) {
        StringBuilder sb = new StringBuilder();
        if (strs == null || strs.size() == 0) {
            return sb;
        }
        int sl = (split == null ? 0 : split.length());
        //不忽略空元素,所以不能调用 joinSplit
        for (String s : strs) {
            sb.append(s);
            if (sl > 0) {
                sb.append(split);
            }
        }
        if (sl > 0) {
            int l = sb.length();
            sb.delete(l - sl, l);
        }
        return sb;
    }

    /** 字符串合并（忽略空元素） (a+b+c ...),如果元素为空则忽略此元素 */
    public StringBuilder join(Object... strs) {
        return joinSplit(null, strs);
    }

    /**
     * 字符串合并（忽略空元素） (a+b+c ...),如果元素为空则忽略此元素
     *
     * @param split 分隔符，可以为null或空字符串
     * @param strs  (a+b+c ...)
     */
    public StringBuilder joinSplit(String split, Object... strs) {
        StringBuilder sb = new StringBuilder();
        if (strs == null || strs.length == 0) {
            return sb;
        }
        int sl = (split == null ? 0 : split.length());
        for (Object s : strs) {
            if (s != null && s.toString().length() > 0) {
                sb.append(s.toString());
                if (sl > 0) {
                    sb.append(split);
                }
            }
        }
        if (sl > 0) {
            int l = sb.length();
            sb.delete(l - sl, l);
        }
        return sb;
    }


    public boolean isEmpty(String s) {
        if (s == null || s.length() == 0 || s.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /** 是否包含空元素，其中之一为空，返回true */
    public boolean isAnyEmpty(String... arys) {
        if (arys == null || arys.length == 0) {
            return true;
        }

        for (String s : arys) {
            if (this.isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /** 所有元素是否都为空时返回true,其中之一不为空，返回false */
    public boolean isAllEmpty(String... arys) {
        if (arys == null || arys.length == 0) {
            return true;
        }

        for (String s : arys) {
            if (!this.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /** 所有元素都为空时，返回true，只要有一个不为空，则是false */
    public boolean isNotEmpty(String arys) {
        return !isEmpty(arys);
    }


    /**
     * 首字母大写
     *
     * @param s 字符串
     * @return
     */
    public String firstUpper(String s) {
        if (isEmpty(s)) {
            return s;
        } else {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
    }

    /**
     * 替换字符串(与jdk的区别是不使用正规)
     *
     * @param expression 需要替换的字符串
     * @param find       需要被替换的串
     * @param replace    用来替换的串
     * @return 替换后的串
     * bj 20050110
     */
    public String replace(String exp , String find, String replace) {
    	int idx = exp.indexOf(find);
    	if(idx<0){
    		return exp;
    	}
    	int findlen = find.length();
        StringBuilder sb = new StringBuilder();
        String temp = exp;
        do {
        	String s = temp.substring(0, idx); 
            sb.append(s).append(replace);
            int start = idx + findlen;
            temp = temp.substring(start);
            
            idx =(temp.length()>0 ?  temp.indexOf(find) :-1);
        }while(idx>=0);
        
        if(temp.length()>0){
        	sb.append(temp);
        }

        return sb.toString();
    }

    /**
     * 如果字符串为null或空字符串, 则取预定义值
     *
     * @param s             字符串
     * @param defaultString 如果字符串为null 或空串 时, 返回的默认值
     * @return 如果字符串为null 或空串 时, 则返回默认值, 否则直接返回字符串
     */
    public String nullif(String s, String defaultString) {
        if (isEmpty(s)) {
            return defaultString;
        } else {
            return s;
        }
    }

    /**
     * 返回参数列表中第一个不为空({@link #isNotEmpty(String)})的字符串, 如果全都为空, 则默认返回"" 空字符串
     * <p>
     * 其实类似于{@link #nullif(String, String)} 的方法, 不过这个方法不限定参数的长度
     *
     * @param strs 字符串数组
     * @return 参数列表中第一个不为空的字符串
     */
    public String firstNotEmpty(final String... strs) {
        for (final String str : strs) {
            if (isNotEmpty(str)) {
                return str;
            }
        }
        return "";
    }

    /**
     * 把字符串中的html标签去掉(应用范围比较窄，可以作为参考)
     *
     * @param htmlStr
     * @return
     */
    public String removeHTMLTag(String htmlStr, boolean isReplaceNbsp) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        String rs = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(rs);
        rs = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(rs);
        rs = m_html.replaceAll(""); //过滤html标签

        rs = rs.trim(); //返回文本字符串
        if (isReplaceNbsp) {
            rs = rs.replaceAll("&nbsp;", " ");
        }
        rs = replaceHtml(rs);
        return rs;
    }

    /**
     * 把字符串中的html标签去掉(应用范围比较窄，可以作为参考)
     *
     * @param htmlStr
     * @return
     */
    public String removeHTMLTag(String htmlStr) {
        return removeHTMLTag(htmlStr, false);
    }

    private String replaceHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 还原HTML
        // content = HTMLDecoder.decode(content);
        content = content.replaceAll("[\t\r\n]+", "");
        return content;
    }


    /**
     * 获取字符串的byte 数组表示, 使用指定的编码方式
     *
     * @param content 字符串
     * @param charset 编码方式, 可以为null, 如果为null, 则直接使用{@link String#getBytes()} 方法, 如果不为null, 则使用{@link String#getBytes(String)}
     * @return byte 数组
     */
    public byte[] getBytes(final String content, final String charset) {
        if (isEmpty(content)) {
            return new byte[0];
        }

        if (isEmpty(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("指定的编码集不对, 您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 去除字符串前后的空白字符
     *
     * @param str 字符串, 可能为null
     * @return 去除前后的空白字符后的字符串, 如果参数字符串是null, 则原样返回
     */
    public String trim(final String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    /**
     * 判断两个字符串是否相等, 主要是兼容可能为null 的情况
     *
     * @param str1 字符串, 可能为null
     * @param str2 字符串, 可能为null
     * @return 如果两个字符串都为null, 或者都不是且equals, 则返回true
     */
    public boolean isEquals(final String str1, final String str2) {
        if (str1 == null) {
            return str2 == null;
        }
        return str1.equals(str2);
    }
    
    /**下划线变驼峰*/
	public String convertUnderlineHump(String tableName){
		if(tableName.indexOf('_')==-1){
			return tableName;
		}
		
		char[] ts = tableName.toCharArray();
		int l = ts.length-1;
		if(ts[l]=='_'){
			ts[l]=' ';
		}
		for(int i = l;i >0;i--){
			char c = ts[i],p = ts[i-1];
			if(p=='_'){
				ts[i-1] = Character.toUpperCase(c);
				for(int x = i;x <l;x++){
					ts[x]=ts[x+1];
				}
				ts[l]=' ';
			}
		}
		String s = new String(ts);
		return s.trim();
	}
	/**驼峰变下划线*/
	public String convertHumpUnderline(String className){
		int l = (className==null ? 0: className.length());
		boolean convert = false;
		if(l<2){
			;//return className;
		}
		else{
			for(int i = 1; i < l;i++){
				char  c = className.charAt(i);
				if(c >='A' && c <='Z'){
					convert = true;
					break;
				}
			}
		}
		if(!convert){
			return className;
		}		
		///////////////////////////////////
		StringBuilder sb = new StringBuilder(l* 3/2);
		sb.append(className.charAt(0));
		for(int i = 1; i < l;i++){
			char  c = className.charAt(i);
			if(c >='A' && c <='Z'){
				sb.append('_').append(Character.toLowerCase(c));
			}
			else{
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * @description: 将以（）拼装的字符串拆解
	 * @param: originString
	 * @return list
	 * @author Mr.Simple
	 * @date 2018/11/19 14:04
	 */
	public List<String> convertStringList(String originString){
        if(Tools.string.isEmpty(originString))
            return new ArrayList<>();
        String replaceString = originString.replace("(", "").replace(")", ",");
        String[] s = replaceString.split(",");
        return splicePropertyValues(s);
    }

    /**
     * @Author Mr.Simple
     * @Description 拆分功能 拼接propertyValues查询数据库
     * @Date 16:11 2019/5/14
     * @Param 
     * @return 
     **/
    public List<String> splicePropertyValues(String[] s){
        List<String> list = new ArrayList<>();
        for (int i = 0;i < s.length;i++){
            StringBuffer stringBuffer = new StringBuffer("(");
            stringBuffer.append(s[i]).append(")");
            list.add(stringBuffer.toString());
        }
	    return list;
    }

    /**
     * @Author Mr.Simple
     * @Description 将propertyValueId拆解出来
     * @Date 15:48 2019/5/14
     * @Param
     * @return
     **/
    public List<Long> convertPropertyValueToLong(String originString){
        List<Long> list = new ArrayList<>();
        if(Tools.string.isEmpty(originString))
            throw new WakaException(RavvExceptionEnum.INVALID_PARAMETER_ERROR);
        String replaceString = originString.replace("(", "").replace(")", ",");
        String[] s = replaceString.split(",");
        for (int i = 0;i < s.length;i++){
            list.add(Long.parseLong(s[i]));
        }
        return list;
    }

    /**
     * @description: 将goodsCartList拆分（goodsCartId,quantity）
     * @param: string
     * @return map<long,int>
     * @author Mr.Simple
     * @date 2018/12/11 10:57
     */
    public Map<String,String> convertStringToMap(String goodsCartList){
        Map<String,String> map = new HashMap<>();
        String[] first = goodsCartList.split(";");
        String temp = null;
        String[] last = null;
        String key = null;
        String value = null;
        for(int i = 0;i < first.length;i++){
            temp = first[i];
            last = temp.split(",");
            key = last[0];
            value = last[1];
            map.put(key,value);
        }
        return map;
    }

    /**
     * @description: 将以逗号拼装的字符串拆解
     * @param: originString
     * @return list
     * @author Mr.Simple
     * @date 2018/11/19 14:04
     */
    public List<Long> convertStringToLong(String originString){
        List<Long> list = new ArrayList<>();
        if(Tools.string.isEmpty(originString))
            return list;
        String[] s = originString.split(",");
        for (int i = 0;i < s.length;i++){
            list.add(Long.parseLong(s[i]));
        }
        return list;
    }

    public String getAnonymous(String originalName){
        char[] chars = originalName.toCharArray();
        StringBuffer realName = new StringBuffer("");
        for(int i = 0;i < chars.length;i++){
            if(i == 0 || i == (originalName.length() - 1)){
                realName.append(originalName.charAt(i));
            }
            else {
                realName.append("*");
            }
        }
        return realName.toString();
    }

    public String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z" };

    /**
     * @description: 生成8位的推荐码
     * @param:
     * @return string
     * @author Mr.Simple
     * @date 2019/1/7 16:54
     */
    public String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    /**
     * @description: 拼装图片路径
     * @param: string
     * @return string
     * @author Mr.Simple
     * @date 2018/12/28 15:13
     */
    public String assembleImg(String img){
        List<String> list = new ArrayList<>();
        String[] s = img.split(",");
        for (String stringImg: s) {
            list.add(stringImg);
        }
        //拼装图片全路径
        List<String> targetList = new ArrayList<>();
        list.forEach(item->{
            targetList.add(QiniuUtil.getFullPath(item));
        });
        StringBuffer imgBuffer = new StringBuffer("");
        targetList.forEach(item->{
            imgBuffer.append(item).append(",");
        });
        String imgUrl = imgBuffer.substring(0,imgBuffer.length() - 1);
        return imgUrl;
    }

    /**
     * @description: 拆解图片路径
     * @param: string
     * @return string
     * @author Mr.Simple
     * @date 2018/12/28 15:09
     */
    public String splitImg(String img){
        List<String> list = new ArrayList<>();
        String[] s = img.split(",");
        for (String stringImg: s) {
            list.add(stringImg);
        }
        //拆解图片
        List<String> targetList = new ArrayList<>();
        list.forEach(item->{
            targetList.add(QiniuUtil.getRelativePath(item));
        });
        StringBuffer imgBuffer = new StringBuffer("");
        targetList.forEach(item->{
            imgBuffer.append(item).append(",");
        });
        String imgUrl = imgBuffer.substring(0,imgBuffer.length() - 1);
        return imgUrl;
    }

    /**
     * 除去后缀字符串
     * @param str
     * @param suffix
     * @return
     */
    public  String removeSuffix(String str, String suffix) {
        return !isEmpty(str) && !isEmpty(suffix)?(str.endsWith(suffix)?str.substring(0, str.length() - suffix.length()):str):str;
    }




}
