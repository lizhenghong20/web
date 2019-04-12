package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/22.
 */

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WafRequestWrapper extends HttpServletRequestWrapper {
    private boolean filterXSS;
    private boolean filterSQL;

    public WafRequestWrapper(HttpServletRequest request, boolean filterXSS, boolean filterSQL) {
        super(request);
        this.filterXSS = true;
        this.filterSQL = true;
        this.filterXSS = filterXSS;
        this.filterSQL = filterSQL;
    }

    public WafRequestWrapper(HttpServletRequest request) {
        this(request, true, true);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if(values == null) {
            return null;
        } else {
            int count = values.length;
            String[] encodedValues = new String[count];

            for(int i = 0; i < count; ++i) {
                encodedValues[i] = this.filterParamString(values[i]);
            }

            return encodedValues;
        }
    }

    public Map getParameterMap() {
        Map primary = super.getParameterMap();
        HashMap result = new HashMap(primary.size());
        Iterator var4 = primary.entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry entry = (Map.Entry)var4.next();
            result.put((String)entry.getKey(), this.filterEntryString((String[])entry.getValue()));
        }

        return result;
    }

    protected String[] filterEntryString(String[] rawValue) {
        for(int i = 0; i < rawValue.length; ++i) {
            rawValue[i] = this.filterParamString(rawValue[i]);
        }

        return rawValue;
    }

    public String getParameter(String parameter) {
        return this.filterParamString(super.getParameter(parameter));
    }

    public String getHeader(String name) {
        return this.filterParamString(super.getHeader(name));
    }

    public Cookie[] getCookies() {
        Cookie[] existingCookies = super.getCookies();
        if(existingCookies != null) {
            for(int i = 0; i < existingCookies.length; ++i) {
                Cookie cookie = existingCookies[i];
                cookie.setValue(this.filterParamString(cookie.getValue()));
            }
        }

        return existingCookies;
    }

    protected String filterParamString(String rawValue) {
        if(rawValue == null) {
            return null;
        } else {
            String tmpStr = rawValue;
            if(this.filterXSS) {
                tmpStr = WafKit.stripXSS(rawValue);
            }

            if(this.filterSQL) {
                tmpStr = WafKit.stripSqlInjection(tmpStr);
            }

            return tmpStr;
        }
    }
}

