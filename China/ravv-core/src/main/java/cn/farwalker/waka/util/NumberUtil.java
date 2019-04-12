package cn.farwalker.waka.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字型的工具类
 *
 * @author Administrator
 */
public class NumberUtil {

    public static final int INTEGER = 1, FLOAT = 2, DOUBLE = 3;
    public final DoubleArith doubledUtil = new DoubleArith();

    /** 因为类必须为public，所以只能把构造函数给这样控制 */
    NumberUtil() {

    }

    /** 格式数字 */
    private NumberFormat nf;

    public boolean isFloat(Object val) {
        return isNumeric(val) == FLOAT;
    }

    public boolean isInteger(Object val) {
        return isNumeric(val) == INTEGER;
    }

    /**
     * 判断是否数字
     *
     * @param val
     * @return 不是数值:-1,整型:1,浮点:2
     */
    public int isNumeric(Object val) {
        if (val == null) {
            return -1;
        }
        String s = val.toString().trim();
        int size = s.length();
        if (size == 0) {
            return -1;
        }

        int result = 0;
        boolean doc = false;// 小数点
        for (int i = 0; i < s.length(); i++) {
            char x = s.charAt(i);

            // 负号
            if (i > 0 && (x == '-' || x == '+')) {
                result = -1;
                break;
            }

            // 不能出现两个小数点
            if (x == '.') {
                if (doc) {
                    result = -1;
                    break;
                } else
                    doc = true;
            }

            if (x != '.' && x != '+' & x != '-' && (x < '0' || x > '9')) {
                result = -1;
                break;
            }
        }
        if (doc && result == 0) {
            return FLOAT;// 浮点
        } else if (result == 0) {
            return INTEGER;// 整型
        } else {
            return result;
        }
    }

    /**
     * 把浮点数格式化为两位小数的字符串
     *
     * @param f
     * @return 返回两位小数的字符串
     */
    public String numberFormat(double f) {
        if (nf == null) {
            nf = new DecimalFormat("#######0.00");// .getInstance();
            // java.text.DecimalFormat format =
            // (java.text.DecimalFormat)java.text.DecimalFormat.getInstance();

            // nf.applyPattern("###########.##");
        }
        return nf.format(f);
    }

    /** 是空或者==0 */
    public boolean isEmpty(Number n) {
        return n == null || n.intValue() == 0;
    }

    /**
     * 简单的补零
     *
     * @param num
     * @param length
     * @return
     */
    public String formatNumber(long num, int length) {
        String s = String.valueOf(num);
        if (s.length() >= length) {
            return s;
        }
        final String ZREO = "00000000000000000000";
        final int ZL = ZREO.length();
        String rs = ZREO.substring(ZL - length + s.length()) + s;
        return rs;
    }

    /**
     * 格式化金额:元(用于分转为元的处理)
     *
     * @param num
     * @param divisor 除数 分别为10,100,1000...
     * @return
     */
    public String formatAmt(Number num, int divisor) {
        String rs;
        if (num == null) {
            rs = formatAmt(0, divisor);
        } else {
            rs = formatAmt(num.longValue(), divisor);
        }
        return rs;
    }

    private static final String D_ZREO = "00000000000000000000";

    /**
     * 格式化金额:元(用于分转为元的处理)
     *
     * @param num
     * @param divisor 除数 分别为1,10,100,1000...
     * @return
     */
    public String formatAmt(long num, int divisor) {
        if (divisor < 10) {
            return String.valueOf(num);
        }
        long n = Math.abs(num);
        String rs = String.valueOf(n);
        int length = 0;
        switch (divisor) {
            case 100://常用
                length = 2;
                break;
            case 1000://常用
                length = 3;
                break;
            default:
                int d2 = divisor;
                while (d2 >= 10) {
                    d2 /= 10;
                    length++;
                }
                break;
        }

        if (rs.length() <= length) {
            int l = (D_ZREO.length() + rs.length()) - (length + 1);
            rs = (D_ZREO + rs).substring(l);
        }

        int sp = rs.length() - length;
        String s1 = rs.substring(0, sp), s2 = rs.substring(sp);
        String result = s1 + "." + s2;
        if (num < 0) {
            result = "-" + result;
        }
        return result;
    }

    /**
     * 比较Double
     *
     * @return
     * @deprecated 建议使用Double.compare(d1, d2);
     */
    public int compareDouble(double d1, double d2) {
        return Double.compare(d1, d2);
    }

    /**
     * 比较Float
     *
     * @return
     * @deprecated 建议使用Float.compare(d1, d2);
     */
    public int compareFloat(float d1, float d2) {
        return Float.compare(d1, d2);
    }

    public int nullIf(final Number val, final int defaultVal) {
        if (val == null) {
            return defaultVal;
        }
        return val.intValue();
    }

    public long nullIf(final Number val, final long defaultVal) {
        if (val == null) {
            return defaultVal;
        }
        return val.longValue();
    }

    public double nullIf(final Number val, final double defaultVal) {
        if (val == null) {
            return defaultVal;
        }
        return val.doubleValue();
    }

    /**
     * 检查字符串是否数字，并且返回数值
     *
     * @param val
     * @param defaultVal
     * @return
     */
    public long nullIf(final String val, final long defaultVal) {
        long rs = defaultVal;
        if (isInteger(val)) {
            rs = Long.parseLong(val);
        }
        return rs;
    }

    /**
     * 检查字符串是否数字，并且返回int数值
     *
     * @param val
     * @param defaultVal
     * @return
     */
    public int nullIf(final String val, final int defaultVal) {
        int rs = defaultVal;
        if (isInteger(val)) {
            rs = Integer.parseInt(val);
        }
        return rs;
    }

    /**
     * 检查字符串是否数字，并且返回double数值
     *
     * @param val
     * @param defaultVal
     * @return
     */
    public double nullIf(final String val, final double defaultVal) {
        double rs = defaultVal;
        int n = isNumeric(val);
        if (n == INTEGER) {
            rs = Long.parseLong(val);
        } else if (n == FLOAT) {
            rs = Double.parseDouble(val);
        }
        return rs;
    }


    /**
     * 进行BigDecimal对象的加减乘除，四舍五入等运算的工具类
     * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精  确的浮点数运算，包括加减乘除和四舍五入。
     *
     * @author ameyume
     */
    public class DoubleArith {
        private DoubleArith() {

        }

        /**
         * (+)提供精确的加法运算。
         *
         * @param v1 被加数
         * @param v2 加数
         * @return 两个参数的和
         */
        public double add(double v1, double v2) {
            return add(Double.toString(v1), Double.toString(v2));
        }

        /**
         * (+)提供精确的加法运算。
         *
         * @param double1 被加数
         * @param double2 加数
         * @return 两个参数的和
         */
        public double add(String double1, String double2) {
            BigDecimal b1 = new BigDecimal(double1);
            BigDecimal b2 = new BigDecimal(double2);
            return b1.add(b2).doubleValue();
        }

        /**
         * (-)提供精确的减法运算。
         *
         * @param v1 被减数
         * @param v2 减数
         * @return 两个参数的差
         */
        public double sub(double v1, double v2) {
            return sub(Double.toString(v1), Double.toString(v2));
        }

        /**
         * (-)提供精确的减法运算。
         *
         * @param double1 被减数
         * @param double2 减数
         * @return 两个参数的差
         */
        public double sub(String double1, String double2) {
            BigDecimal b1 = new BigDecimal(double1);
            BigDecimal b2 = new BigDecimal(double2);
            return b1.subtract(b2).doubleValue();
        }

        /**
         * (*)提供精确的乘法运算。
         *
         * @param v1 被乘数
         * @param v2 乘数
         * @return 两个参数的积
         */
        public double mul(double v1, double v2) {
            return mul(Double.toString(v1), Double.toString(v2));
        }

        /**
         * (*)提供精确的乘法运算。
         *
         * @param double1 被乘数
         * @param double2 乘数
         * @return 两个参数的积
         */
        public double mul(String double1, String double2) {
            BigDecimal b1 = new BigDecimal(double1);
            BigDecimal b2 = new BigDecimal(double2);
            return b1.multiply(b2).doubleValue();
        }

        /**
         * (/)提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
         * 保留2小数点，以后的数字四舍五入。
         *
         * @param v1 被除数
         * @param v2 除数
         * @return 两个参数的商
         */
        public double div(double v1, double v2) {
            return div(v1, v2, 2);
        }

        /**
         * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
         * 定精度，以后的数字四舍五入。
         *
         * @param v1    被除数
         * @param v2    除数
         * @param scale 保留位数,表示需要精确到小数点以后几位。
         * @return 两个参数的商
         */
        public double div(double v1, double v2, int scale) {
            return div(Double.toString(v1), Double.toString(v2), scale);
        }

        /**
         * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
         * 定精度，以后的数字四舍五入。
         *
         * @param v1    被除数
         * @param v2    除数
         * @param scale 保留位数,表示需要精确到小数点以后几位。
         * @return 两个参数的商
         */
        public double div(String double1, String double2, int scale) {
            if (scale < 0) {
                throw new IllegalArgumentException("小数位不能为负数");
            }
            BigDecimal b1 = new BigDecimal(double1);
            BigDecimal b2 = new BigDecimal(double2);
            BigDecimal c = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
            return c.doubleValue();
        }

        /**
         * 提供精确的小数位四舍五入处理。
         *
         * @param v     需要四舍五入的数字
         * @param scale 小数点后保留几位
         * @return 四舍五入后的结果
         */
        public double round(double v, int scale) {
            if (scale < 0) {
                throw new IllegalArgumentException("小数位不能为负数");
            }
            BigDecimal b = BigDecimal.valueOf(v);
            BigDecimal one = new BigDecimal(1);
            BigDecimal c = b.divide(one, scale, BigDecimal.ROUND_HALF_UP);
            return c.doubleValue();
        }

        /**
         * 提供精确的类型转换(Float)
         *
         * @param v 需要被转换的数字
         * @return 返回转换结果
         */
        public float convertsToFloat(double v) {
            BigDecimal b = BigDecimal.valueOf(v);
            return b.floatValue();
        }

        /**
         * 提供精确的类型转换(Int)不进行四舍五入
         *
         * @param v 需要被转换的数字
         * @return 返回转换结果
         */
        public int convertsToInt(double v) {
            BigDecimal b = BigDecimal.valueOf(v);
            return b.intValue();
        }

        /**
         * 提供精确的类型转换(Long)
         *
         * @param v 需要被转换的数字
         * @return 返回转换结果
         */
        public long convertsToLong(double v) {
            BigDecimal b = BigDecimal.valueOf(v);
            return b.longValue();
        }

        /**
         * 返回两个数中大的一个值
         *
         * @param v1 需要被对比的第一个数
         * @param v2 需要被对比的第二个数
         * @return 返回两个数中大的一个值
         */
        public double returnMax(double v1, double v2) {
            BigDecimal b1 = BigDecimal.valueOf(v1);
            BigDecimal b2 = BigDecimal.valueOf(v2);
            BigDecimal c = b1.max(b2);
            return c.doubleValue();
        }

        /**
         * 返回两个数中小的一个值
         *
         * @param v1 需要被对比的第一个数
         * @param v2 需要被对比的第二个数
         * @return 返回两个数中小的一个值
         */
        public double returnMin(double v1, double v2) {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            BigDecimal c = b1.min(b2);
            return c.doubleValue();
        }

        /**
         * 精确对比两个数字
         *
         * @param v1 需要被对比的第一个数
         * @param v2 需要被对比的第二个数
         * @return 如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
         * @deprecated 不知道与 Double.compare(d1, d2);有没有区别
         */
        public int compareTo(double v1, double v2) {
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);
            return b1.compareTo(b2);
        }
    }
}