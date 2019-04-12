package cn.farwalker.waka.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author Administrator
 */
public class DateUtil {
    public enum FORMAT {
        /**
         * yyyy-MM-dd HH:mm:ss
         */
        YYYY_MM_DD_HH_MM_SS,
        /**
         * yyyyMMdd HHmmss
         */
        YYYYMMDD_HHMMSS,
        /**
         * yyyyMMddHHmmss
         */
        YYYYMMDDHHMMSS,
        /**
         * yyyy-MM-dd
         */
        YYYY_MM_DD,
        /**
         * HH:mm:ss
         */
        HH_MM_SS,
        /**
         * yyyyMMdd
         */
        YYYYMMDD,
        /**
         * HHmmss
         */
        HHMMSS
        /*19:(yyyy-MM-dd HH:mm:ss)<br/>
    *  15:(yyyyMMdd HHmmss)<br/>
    *  14:(yyyyMMddHHmmss)<br/>
    *  10:(yyyy-MM-dd)<br/>
    *  8 :(HH:mm:ss)<br/>
    *  7 :(yyyyMMdd)<br/>
    *  6 :(HHmmss)*/
    }

    /**
     * 因为类必须为public，所以只能把构造函数给这样控制
     */
    DateUtil() {

    }

    public static void main(String[] arg) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2015);
        c.set(Calendar.MONTH, 10 - 1);
        c.set(Calendar.DATE, 7);

        c.set(Calendar.HOUR_OF_DAY, 13);
        c.set(Calendar.MINUTE, 57);
        c.set(Calendar.SECOND, 9);

        //c.setTime(new Date());
        DateUtil d = new DateUtil();
        final String date = "2015-10-07", time = "13:57:09";
        if (date.equals(d.formatsmall(c.getTime()))) {
            System.out.println("formatsmall():OK");
        } else {
            System.err.println("formatsmall()-->error:" + d.formatsmall(c.getTime()));
        }
        ///////////////////
        if ((date + " " + time).equals(d.formatDate(c).toString())) {
            System.out.println("formatDate(Calendar):OK");
        } else {
            System.err.println("formatDate(Calendar)-->error:" + d.formatDate(c));
        }

        ///////////////////////
        if ((date + " " + time).equals(d.formatDate(c.getTime()).toString())) {
            System.out.println("formatDate(Date):OK");
        } else {
            System.err.println("formatDate(Date)-->error:" + d.formatDate(c.getTime()));
        }

        ///////////////////////
        if ((date + " " + time).equals(d.formatDate(c.getTimeInMillis()).toString())) {
            System.out.println("formatDate(Long):OK");
        } else {
            System.err.println("formatDate(Long)-->error:" + d.formatDate(c.getTimeInMillis()));
        }


        ///////////////////////
        if ((date + " " + time).equals(d.formatDate(c.getTime(), 19).toString())) {
            System.out.println("formatDate(Date,19):OK");
        } else {
            System.err.println("formatDate(Date,19)-->error:" + d.formatDate(c.getTime(), 19));
        }

        ///////////////////////
        if ((date.replaceAll("-", "") + " " + time.replaceAll(":", "")).equals(d.formatDate(c.getTime(), 15).toString())) {
            System.out.println("formatDate(Date,15):OK");
        } else {
            System.err.println("formatDate(Date,15)-->error:" + d.formatDate(c.getTime(), 15));
        }

        ///////////////////////
        if (date.equals(d.formatDate(c.getTime(), 10).toString())) {
            System.out.println("formatDate(Date,10):OK");
        } else {
            System.err.println("formatDate(Date,10)-->error:" + d.formatDate(c.getTime(), 10));
        }

        ///////////////////////
        FORMAT[] formats = FORMAT.values();
        for (FORMAT f : formats) {
            System.out.println(f + "-format:" + d.formatDate(c, f));
        }

        String ymds = "20160803";
        ;
        Calendar ymd = d.parseDate(ymds, FORMAT.YYYYMMDD);
        if (d.formatDate(ymd, FORMAT.YYYY_MM_DD).toString().equalsIgnoreCase(ymds)) {
            System.out.println("parseDate:" + FORMAT.YYYYMMDD.toString() + ":" + ymds + "(OK)");
        } else {
            System.out.println("parseDate:" + FORMAT.YYYYMMDD.toString() + ":" + ymds + "(ERROR)");
        }
    }

    /** 格式化日期,19位:yyyy-MM-dd HH:mm:ss */
    // private SimpleDateFormat df19;这三个类不能在对象中持有，一个Format对象在多线程情况下执行format方法会错乱

    /** 格式化日期,10位:yyyy-MM-dd */
    // private SimpleDateFormat df10;

    /** 格式化日期,15位:yyyyMMdd HHmmss */
    // private SimpleDateFormat df15;

    /**
     * 格式化日期时间，长格式: yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public String formatDate() {
        return formatDate(Calendar.getInstance()).toString();
    }

    /**
     * 格式化日期时间，长格式: yyyy-MM-dd HH:mm:ss
     *
     * @param time 毫秒
     * @return
     */
    public String formatDate(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return formatDate(c).toString();
    }

    /**
     * 格式化日期时间，短格式: yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public String formatsmall(Date date) {
        if (date == null) {
            return "1970-01-01";
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return formatDate(c, FORMAT.YYYY_MM_DD).toString();
    }

    /**
     * 格式化日期时间，长格式: yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期对象
     * @return
     */
    public String formatDate(Date date) {
        /*if (date != null) {
            // SimpleDateFormat df = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
			// //日期(yyyy-MM-dd),字符注意大小不写
			// SimpleDateFormat gdformat = new
			// SimpleDateFormat("HHmmss");//时间(HH:mm:ss),字符注意大小写
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);// 日期(yyyy-MM-dd),字符注意大小写
		} else
			return "1970-01-01 00:00:00";*/
        if (date == null) {
            return "1970-01-01 00:00:00";
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return formatDate(c).toString();
        }
    }

    public StringBuilder formatDate(Calendar c) {
        return formatDate(c, FORMAT.YYYY_MM_DD_HH_MM_SS);
    }

    public StringBuilder formatDate(FORMAT format) {
        return formatDate(Calendar.getInstance(), format);
    }

    /**
     * @param c
     * @param format 长度格式 <br/>
     *               19:(yyyy-MM-dd HH:mm:ss)<br/>
     *               15:(yyyyMMdd HHmmss)<br/>
     *               14:(yyyyMMddHHmmss)<br/>
     *               10:(yyyy-MM-dd)<br/>
     *               8 :(HH:mm:ss)<br/>
     *               7 :(yyyyMMdd)<br/>
     *               6 :(HHmmss)
     * @return
     */
    public StringBuilder formatDate(Calendar c, FORMAT format) {

        boolean date = false, time = false, datetimeSplit = false;
        boolean split = (format == FORMAT.YYYY_MM_DD_HH_MM_SS || format == FORMAT.YYYY_MM_DD || format == FORMAT.HH_MM_SS);
        if (format == FORMAT.YYYY_MM_DD_HH_MM_SS || format == FORMAT.YYYYMMDD_HHMMSS) {
            datetimeSplit = true;
            time = true;
            date = true;
        } else if (format == FORMAT.YYYYMMDDHHMMSS) {
            date = true;
            time = true;
        } else if (format == FORMAT.YYYY_MM_DD || format == FORMAT.YYYYMMDD) {
            date = true;
        } else if (format == FORMAT.HH_MM_SS || format == FORMAT.HHMMSS) {
            time = true;
        } else {
            throw new YMException("不支持的长度格式:" + format);
        }

        StringBuilder rs = new StringBuilder();
        if (date) {
            String y = String.valueOf(c.get(Calendar.YEAR));
            String m = Tools.number.formatNumber(c.get(Calendar.MONTH) + 1, 2);
            String d = Tools.number.formatNumber(c.get(Calendar.DATE), 2);
            final char SD = '-';

            rs.append(y);
            if (split) {
                rs.append(SD);
            }
            rs.append(m);
            if (split) {
                rs.append(SD);
            }
            rs.append(d);
        }
        if (datetimeSplit) {
            rs.append(' ');
        }

        if (time) {
            String h = Tools.number.formatNumber(c.get(Calendar.HOUR_OF_DAY), 2);
            String mi = Tools.number.formatNumber(c.get(Calendar.MINUTE), 2);
            String s = Tools.number.formatNumber(c.get(Calendar.SECOND), 2);
            final char SD = ':';

            rs.append(h);
            if (split) {
                rs.append(SD);
            }
            rs.append(mi);
            if (split) {
                rs.append(SD);
            }
            rs.append(s);
        }

        return rs;
    }

    /**
     * 格式化日期时间
     *
     * @param date
     * @param format 长度格式 <br/>
     *               19:(yyyy-MM-dd HH:mm:ss)<br/>
     *               15:(yyyyMMdd HHmmss)<br/>
     *               14:(yyyyMMddHHmmss)<br/>
     *               10:(yyyy-MM-dd)<br/>
     *               8 :(HH:mm:ss)<br/>
     *               7 :(yyyyMMdd)<br/>
     *               6 :(HHmmss)
     * @return
     * @deprecated 请使用 formatDate(Calendar c,FORMAT format)
     */
    public String formatDate(Date date, int format) {

	     /* @param length
	     *            -格式长度，取值为10、15或19；默认为19；<br>
	     *            19－长格式yyyy-MM-dd HH:mm:ss，15-无格式：yyyyMMdd
	     *            HHmmss，10－短格式yyyy-MM-dd
	     */
        if (date == null) {
            return null;
        }
        FORMAT f = null;
        switch (format) {
            case 19:
                f = FORMAT.YYYY_MM_DD_HH_MM_SS;
                break;
            case 15:
                f = FORMAT.YYYYMMDD_HHMMSS;
                break;
            case 14:
                f = FORMAT.YYYYMMDDHHMMSS;
                break;
            case 10:
                f = FORMAT.YYYY_MM_DD;
                break;
            case 8:
                f = FORMAT.YYYYMMDD;
                break;
            case 7:
                f = FORMAT.HH_MM_SS;
                break;
            case 6:
                f = FORMAT.HHMMSS;
                break;
            default:
                throw new YMException("不支持的长度格式:" + format);
        }

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return formatDate(c, f).toString();
    }

    /**
     * 比较日期:d1 - d2 进行运算(经过sql Server验证，请放心使用)
     *
     * @param d1    日期1
     * @param d2    日期2
     * @param field 日期类型：与Calendar.field的值
     * @return 相差结果
     */
    public int compareDate(Date d1, Date d2, int field) {
        int result = 0;
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        c1.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);

        switch (field) {
            case Calendar.DATE:
                c1.set(Calendar.HOUR, 0);
                c1.set(Calendar.MINUTE, 0);
                c1.set(Calendar.SECOND, 0);

                c2.set(Calendar.HOUR, 0);
                c2.set(Calendar.MINUTE, 0);
                c2.set(Calendar.SECOND, 0);

                long t1 = c1.getTime().getTime();
                long t2 = c2.getTime().getTime();
                result = (int) ((t1 - t2) / (1000 * 60 * 60 * 24));
                break;
            case Calendar.HOUR:
                // c1.set(Calendar.HOUR,0);
                c1.set(Calendar.MINUTE, 0);
                c1.set(Calendar.SECOND, 0);
                // c2.set(Calendar.HOUR,0);

                c2.set(Calendar.MINUTE, 0);
                c2.set(Calendar.SECOND, 0);

                t1 = c1.getTime().getTime();
                t2 = c2.getTime().getTime();
                result = (int) ((t1 - t2) / (1000 * 60 * 60));
                break;
            case Calendar.MINUTE:
                c1.set(Calendar.SECOND, 0);
                c2.set(Calendar.SECOND, 0);

                t1 = c1.getTime().getTime();
                t2 = c2.getTime().getTime();
                result = (int) ((t1 - t2) / (1000 * 60));
                break;
            case Calendar.SECOND:
                t1 = c1.getTime().getTime();
                t2 = c2.getTime().getTime();
                result = (int) ((t1 - t2) / (1000));
                break;
            case Calendar.YEAR:
                int y1 = c1.get(Calendar.YEAR);
                int y2 = c2.get(Calendar.YEAR);
                result = y1 - y2;
                break;
            case Calendar.MONTH:
                y1 = c1.get(Calendar.YEAR);
                y2 = c2.get(Calendar.YEAR);
                int m1 = y1 * 12 + c1.get(Calendar.MONTH);
                int m2 = y2 * 12 + c2.get(Calendar.MONTH);
                result = (m1 - m2);
                break;
            default: // 默认为日期
                c1.set(Calendar.HOUR, 0);
                c1.set(Calendar.MINUTE, 0);
                c1.set(Calendar.SECOND, 0);
                c2.set(Calendar.HOUR, 0);
                c2.set(Calendar.MINUTE, 0);
                c2.set(Calendar.SECOND, 0);

                t1 = c1.getTime().getTime();
                t2 = c2.getTime().getTime();
                result = (int) ((t1 - t2) / (1000 * 60 * 60 * 24));
        }
        return result;
    }

    /**
     * 把字符串转换日期
     * 只支持yyyymmdd格式
     *
     * @param date
     * @param format 可以为null
     * @return
     */
    public Calendar parseDate(String date, FORMAT format) {
        Calendar rs = null;
        if (Tools.string.isEmpty(date)) {
            rs = null;
        } else if (format == null || format == FORMAT.YYYY_MM_DD || format == FORMAT.YYYY_MM_DD_HH_MM_SS) {
            rs = parseDate(date);
        } else if (format == FORMAT.YYYYMMDD) {
            String ds = convertYYYYMMDD(date);
            rs = parseDate(ds);
        } else if (format == FORMAT.HHMMSS) {
            String ds = convertHHMMSS(date);
            rs = parseDate(ds);
        } else if (format == FORMAT.YYYYMMDD_HHMMSS) {
            if (date.length() == 8 + 1 + 6) {
                String ds = convertYYYYMMDD(date.substring(0, 8));
                String ts = convertHHMMSS(date.substring(9));
                String dt = ds + " " + ts;
                rs = parseDate(dt);
            }
        } else if (format == FORMAT.YYYYMMDDHHMMSS) {
            if (date.length() == 8 + 6) {
                String ds = convertYYYYMMDD(date.substring(0, 8));
                String ts = convertHHMMSS(date.substring(8));
                String dt = ds + " " + ts;
                rs = parseDate(dt);
            }
        }
        return rs;
    }

    private String convertYYYYMMDD(String date) {
        String ds = null;
        if (date.length() == 8) {
            String y = date.substring(0, 4), m = date.substring(4, 6), d = date.substring(6);
            ds = y + "-" + m + "-" + d;
        }
        return ds;
    }

    private String convertHHMMSS(String time) {
        String ts = null;
        if (time.length() == 6) {
            String h = time.substring(0, 2), m = time.substring(2, 4), s = time.substring(4);
            ts = h + ":" + m + ":" + s;
        }
        return ts;
    }

    /**
     * 把字符串转换日期
     *
     * @param date 字符串,支持的格式为:yyyy/MM/dd 或 yyyy/MM/dd hh:mm:ss,支持yyyymmdd格式(请使用parseYYYYMMDD()方法)
     * @return 如果格式不符, 则返回null
     */
    public Calendar parseDate(String date) {
        if (Tools.string.isEmpty(date)) {
            return null;
        }

        // 字符格式 yyyy/MM/dd 或 yyyy/MM/dd hh:mm:ss
        boolean format = false;
        int year = 1970, month = 0, day = 1, hrs = 0, min = 0, sec = 0;

        date = date.trim().replace('.', '-').replace('/', '-');
        int i = date.indexOf(' ');
        String time = null;
        if (i > 0) {
            time = date.substring(i + 1);
            date = date.substring(0, i);
        } else if (date.indexOf(':') > 0) {
            time = date;
            date = null;
        }

        // 日期
        if (date != null) {
            String[] d = date.split("-");
            if (format = (d.length == 3)) {
                if (format = Tools.number.isInteger(d[0])) {
                    year = Integer.parseInt(d[0]);
                }
                if (format = (format && Tools.number.isInteger(d[1]))) {
                    month = Integer.parseInt(d[1]) - 1;
                }
                if (format = (format && Tools.number.isInteger(d[2]))) {
                    day = Integer.parseInt(d[2]);
                }
            }
        }

        // 时间
        if (time != null) {
            String[] t = time.split(":");
            if (t.length == 3) {
                if (format = Tools.number.isInteger(t[0])) {
                    hrs = Integer.parseInt(t[0]);
                }
                if (format = (format && Tools.number.isInteger(t[1]))) {
                    min = Integer.parseInt(t[1]);
                }
                if (format) {
                    String s = t[2];
                    int idx = s.indexOf('-');
                    if (idx > 0) {
                        s = s.substring(0, idx);
                    }
                    if (format = Tools.number.isInteger(s)) {
                        sec = Integer.parseInt(s);
                    }
                }
            }
        }

        if (format) {
            Calendar gdate = Calendar.getInstance();
            gdate.set(year, month, day, hrs, min, sec);
            gdate.set(Calendar.MILLISECOND, 0);
            return gdate;
        } else {
            return null;
        }
    }

    /**
     * 日期加一天
     *
     * @param date 支持的格式为:yyyy/MM/dd 或 yyyy/MM/dd hh:mm:ss,支持yyyymmdd格式(请使用parseYYYYMMDD()方法)
     * @return
     */
    public String addOneDay(String date) {
        Calendar c = parseDate(date);
        String rs = null;
        if (c != null) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            rs = formatDate(c, FORMAT.YYYY_MM_DD).toString();
        }
        return rs;
    }

    /**
     * 日期加一天
     *
     * @param date       日期
     * @param expression 日期格式(如果"yyyy-MM-dd"则还是调用addOneDay(String))
     *                   这个方法到目前还没有人使用(2016-10-08)
     * @return
     */
    public String addOneDay(String date, String expression) {
        try {
            if (Tools.string.isEmpty(expression) || expression.equals("yyyy-MM-dd")) {
                return addOneDay(date);
            } else {
                SimpleDateFormat format = new SimpleDateFormat(expression);
                Calendar calendar = Calendar.getInstance();
                Date date_ = format.parse(date);
                calendar.setTime(date_);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String result = format.format(calendar.getTime());
                return result;
            }
        } catch (ParseException e) {
            throw new YMException("日期处理错误[" + date + "][" + expression + "]");
        }
    }

    /**
     * 清除时间或日期
     */
    public Calendar clearDateTime(Date d, boolean clearTime) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return clearDateTime(c, clearTime);
    }

    /**
     * 清除时间或日期
     *
     * @param c
     * @param clearTime 清除时间true或清除日期false
     * @return
     */
    public Calendar clearDateTime(Calendar c, boolean clearTime) {
        if (c == null) {
            return null;
        }
        if (clearTime) {
            // 设置时分秒等字段的值为0
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        } else {
            c.set(Calendar.YEAR, 0);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DAY_OF_MONTH, 0);
        }
        return c;
    }

    public Date nowToday() {
        return new Date();
    }

    public Date nowToday(int forwardDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, forwardDays);
        return calendar.getTime();
    }
}
