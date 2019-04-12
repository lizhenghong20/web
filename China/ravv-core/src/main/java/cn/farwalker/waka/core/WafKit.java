package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/22.
 */
import java.util.regex.Pattern;
public class WafKit {
    public WafKit() {
    }

    public static String stripXSS(String value) {
        String rlt = null;
        if(value != null) {
            rlt = value.replaceAll("", "");
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", 2);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
            scriptPattern = Pattern.compile("</script>", 2);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
            scriptPattern = Pattern.compile("<script(.*?)>", 42);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", 42);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", 42);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
            scriptPattern = Pattern.compile("javascript:", 2);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
            scriptPattern = Pattern.compile("vbscript:", 2);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
            scriptPattern = Pattern.compile("onload(.*?)=", 42);
            rlt = scriptPattern.matcher(rlt).replaceAll("");
        }

        return rlt;
    }

    public static String stripSqlInjection(String value) {
        return value == null?null:value.replaceAll("(\'.+--)|(--)|(%7C)", "");
    }

    public static String stripSqlXSS(String value) {
        return stripXSS(stripSqlInjection(value));
    }
}
