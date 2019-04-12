package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/22.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpKit {
    public HttpKit() {
    }

    public static String getIp() {
        HttpServletRequest request = getRequest();
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            int index = XFor.indexOf(",");
            return index != -1?XFor.substring(0, index):XFor;
        } else {
            XFor = Xip;
            if(StringUtils.isNotEmpty(Xip) && !"unKnown".equalsIgnoreCase(Xip)) {
                return Xip;
            } else {
                if(StringUtils.isBlank(Xip) || "unknown".equalsIgnoreCase(Xip)) {
                    XFor = request.getHeader("Proxy-Client-IP");
                }

                if(StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = request.getHeader("WL-Proxy-Client-IP");
                }

                if(StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = request.getHeader("HTTP_CLIENT_IP");
                }

                if(StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
                }

                if(StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = request.getRemoteAddr();
                }

                if(StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
                    XFor = request.getRemoteHost();
                }

                return XFor;
            }
        }
    }

    public static Map<String, String> getRequestParameters() {
        HashMap values = new HashMap();
        HttpServletRequest request = getRequest();
        Enumeration enums = request.getParameterNames();

        while(enums.hasMoreElements()) {
            String paramName = (String)enums.nextElement();
            String paramValue = request.getParameter(paramName);
            values.put(paramName, paramValue);
        }

        return values;
    }

    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return new WafRequestWrapper(request);
    }

    public static String sendGet(String url, Map<String, String> param) {
        String result = "";
        BufferedReader in = null;

        try {
            StringBuffer e = new StringBuffer();
            Iterator realUrl = param.entrySet().iterator();

            while(realUrl.hasNext()) {
                Map.Entry urlNameString = (Map.Entry)realUrl.next();
                e.append(URLEncoder.encode((String)urlNameString.getKey(), "UTF-8") + "=");
                e.append(URLEncoder.encode((String)urlNameString.getValue(), "UTF-8") + "&");
            }

            if(e.lastIndexOf("&") > 0) {
                e.deleteCharAt(e.length() - 1);
            }

            String urlNameString1 = url + "?" + e.toString();
            URL realUrl1 = new URL(urlNameString1);
            URLConnection connection = realUrl1.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map map = connection.getHeaderFields();
            Iterator var10 = map.keySet().iterator();

            String line;
            while(var10.hasNext()) {
                line = (String)var10.next();
                System.out.println(line + "--->" + map.get(line));
            }

            for(in = new BufferedReader(new InputStreamReader(connection.getInputStream())); (line = in.readLine()) != null; result = result + line) {
                ;
            }
        } catch (Exception var19) {
            System.out.println("发送GET请求出现异常！" + var19);
            var19.printStackTrace();
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
            } catch (Exception var18) {
                var18.printStackTrace();
            }

        }

        return result;
    }

    public static String sendPost(String url, Map<String, String> param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        try {
            String e = "";

            String urlNameString;
            for(Iterator realUrl = param.keySet().iterator(); realUrl.hasNext(); e = e + urlNameString + "=" + (String)param.get(urlNameString) + "&") {
                urlNameString = (String)realUrl.next();
            }

            if(e.lastIndexOf("&") > 0) {
                e = e.substring(0, e.length() - 1);
            }

            urlNameString = url + "?" + e;
            URL realUrl1 = new URL(urlNameString);
            URLConnection conn = realUrl1.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();

            String line;
            for(in = new BufferedReader(new InputStreamReader(conn.getInputStream())); (line = in.readLine()) != null; result = result + line) {
                ;
            }
        } catch (Exception var18) {
            System.out.println("发送 POST 请求出现异常！" + var18);
            var18.printStackTrace();
        } finally {
            try {
                if(out != null) {
                    out.close();
                }

                if(in != null) {
                    in.close();
                }
            } catch (IOException var17) {
                var17.printStackTrace();
            }

        }

        return result;
    }
}
