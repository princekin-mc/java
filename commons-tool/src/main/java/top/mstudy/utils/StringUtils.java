package top.mstudy.utils;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author machao
 * @description: 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @date 2022-09-02
 */
@Slf4j public class StringUtils {
    private final static String[] EMPTY_ARRAY = new String[0];

    public static final String DEFAULT_SEPARATOR = ",";

    public static final String EMPTY = "";

    private static final int PAD_LIMIT = 8192;

    // IP归属地查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

    private static final char SEPARATOR = '_';

    private static final String UNKNOWN = "unknown";

    /**
     * 判断字符串是否为空白，空格不属于空白 StringUtil.isEmpty("  ") = false;
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * 判断value是否为空
     *
     * @param value
     * @return boolean
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * 判断字符串是否非空
     *
     * @param value
     * @return boolean
     */
    public static boolean isNonBlank(String value) {
        return !isBlank(value);
    }

    /**
     * 判断字符串是否全为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 拼接字符串
     *
     * @param arr       字符串数组
     * @param separator 分隔符
     * @return
     */
    public static String join(Object[] arr, String separator) {
        return arr == null ? null : join(arr, separator, 0, arr.length);
    }

    /**
     * 根据索引下标拼接指定部分的字符串
     *
     * @param arr        字符串数组
     * @param separator  分隔符
     * @param startIndex 开始下标
     * @param endIndex   结束下标
     * @return
     */
    public static String join(Object[] arr, String separator, int startIndex, int endIndex) {
        if (arr == null) {
            return null;
        }

        if (separator == null) {
            separator = "";
        }

        if (startIndex >= endIndex) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                sb.append(separator);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * 根据分隔符拆分字符串
     *
     * @param str 待拆分的字符串
     * @param sep 分隔符
     * @return
     */
    public static String[] split(String str, String sep) {
        return split(str, sep, false);
    }

    /**
     * 根据分隔符拆分字符串
     *
     * @param str       待拆分的字符串
     * @param sep       分隔符
     * @param needBlank 空白是否需要输出
     * @return
     */
    public static String[] split(String str, String sep, boolean needBlank) {
        int len = isBlank(str) ? 0 : str.length();
        if (len == 0) {
            return EMPTY_ARRAY;
        }

        if (sep.length() == 1) {
            return split(str, sep.charAt(0), needBlank);
        }
        List<String> list = new ArrayList<String>();

        int idx = -1, lastIdx = 0, sepLen = sep.length();
        while ((idx = str.indexOf(sep, lastIdx)) >= 0) {
            if (needBlank || lastIdx != idx) {
                list.add(str.substring(lastIdx, idx));
            }
            lastIdx = idx + sepLen;
        }
        if (lastIdx != str.length()) {
            list.add(str.substring(lastIdx));
        }
        return list.toArray(EMPTY_ARRAY);
    }

    /**
     * 根据分隔符拆分字符串
     *
     * @param str 待拆分的字符串
     * @param sep 分隔符
     * @return
     */
    public static String[] split(String str, char sep) {
        return split(str, sep, false);
    }

    /**
     * 根据分隔符拆分字符串
     *
     * @param str       待拆分的字符串
     * @param sep       分隔符
     * @param needBlank 空白是否需要输出
     * @return
     */
    public static String[] split(String str, char sep, boolean needBlank) {
        int len = isBlank(str) ? 0 : str.length();
        if (len == 0) {
            return EMPTY_ARRAY;
        }

        List<String> list = new ArrayList<String>();

        int lastIdx = 0;
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (c == sep) {
                if (needBlank || lastIdx != i) {
                    list.add(str.substring(lastIdx, i));
                }
                lastIdx = i + 1;
            }
        }
        if (lastIdx != len) {
            list.add(str.substring(lastIdx));
        }

        return list.toArray(EMPTY_ARRAY);
    }

    /**
     * 重复拼接字符串
     *
     * @param ch     待拼接的字符
     * @param repeat 重复多少遍
     * @return
     */
    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    /**
     * 重复拼接字符串
     *
     * @param str    待拼接的字符串
     * @param repeat 重复多少遍
     * @return
     */
    public static String repeat(String str, int repeat) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        if (repeat <= 0) {
            return EMPTY;
        }
        int inputLength = str.length();
        if (repeat == 1 || inputLength == 0) {
            return str;
        }
        if (inputLength == 1 && repeat <= PAD_LIMIT) {
            return repeat(str.charAt(0), repeat);
        }

        int outputLength = inputLength * repeat;
        switch (inputLength) {
        case 1:
            return repeat(str.charAt(0), repeat);
        case 2:
            char ch0 = str.charAt(0);
            char ch1 = str.charAt(1);
            char[] output2 = new char[outputLength];
            for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
                output2[i] = ch0;
                output2[i + 1] = ch1;
            }
            return new String(output2);
        default:
            StringBuilder buf = new StringBuilder(outputLength);
            for (int i = 0; i < repeat; i++) {
                buf.append(str);
            }
            return buf.toString();
        }
    }

    /**
     * 重复拼接字符串
     *
     * @param str       待拼接的字符
     * @param separator 拼接使用的间隔符
     * @param repeat    重复多少遍
     * @return
     */
    public static String repeat(String str, String separator, int repeat) {
        if (str == null || separator == null) {
            return repeat(str, repeat);
        } else {
            String result = repeat(str + separator, repeat);
            return removeEnd(result, separator);
        }
    }

    public static String lpad(String str, char ch, int len) {
        if (isBlank(str)) {
            return repeat(ch, len);
        }
        if (len <= str.length()) {
            return str;
        }
        return repeat(ch, len - str.length()) + str;
    }

    public static String rpad(String str, char ch, int len) {
        if (isBlank(str)) {
            return repeat(ch, len);
        }
        if (len <= str.length()) {
            return str;
        }
        return str + repeat(ch, len - str.length());
    }

    /**
     * 移除字符串结尾部分字符串
     *
     * @param str
     * @param remove
     * @return
     */
    public static String removeEnd(String str, String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    /**
     * 将主字符串中某个子字符串替换为新字符串一次
     *
     * @param text         待替换的主字符串
     * @param searchString 查找的子字符串
     * @param replacement  替换的新字符串
     * @return
     */
    public static String replaceOnce(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    /**
     * 将主字符串中某个子字符串替换为新字符串
     *
     * @param text         待替换的主字符串
     * @param searchString 查找的子字符串
     * @param replacement  替换的新字符串
     * @return
     */
    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    /**
     * 将主字符串中某个子字符串替换为新字符串
     *
     * @param text         待替换的主字符串
     * @param searchString 查找的子字符串
     * @param replacement  替换的新字符串
     * @param max          最大替换多少次
     * @return
     */
    public static String replace(String text, String searchString, String replacement, int max) {
        if (isBlank(text) || (isBlank(searchString)) || (replacement == null) || (max == 0)) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0) ? 0 : increase;
        increase *= ((max > 64) ? 64 : (max < 0) ? 16 : max);
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * 从字符串中截取出部分字符串
     *
     * @param str   主字符串
     * @param start 截取开始位置
     * @return
     */
    public static String substring(String str, int start) {
        if (str == null) {
            return null;
        }

        // handle negatives, which means last n characters
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return EMPTY;
        }

        return str.substring(start);
    }

    /**
     * 从字符串中截取出部分字符串
     *
     * @param str   主字符串
     * @param start 截取开始位置
     * @param end   截取结束位置
     * @return
     */
    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 检查字符串数组中是否含有某个字符串
     *
     * @param arr    字符串数组
     * @param subStr 子字符串
     * @return
     */

    public static boolean contains(String[] arr, String subStr) {
        return indexOf(arr, subStr) >= 0;
    }

    public static int indexOf(String[] arr, String subStr) {
        if (arr == null || arr.length == 0) {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(subStr)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 检查字符串中是否含有子字符串
     *
     * @param searchStr 源字符串
     * @param subStr    子字符串
     * @return
     */

    public static boolean contains(String searchStr, String subStr) {
        return contains(searchStr, subStr, DEFAULT_SEPARATOR);
    }

    /**
     * 检查字符串中是否含有对应的子字符串，源字符串中以分隔符分隔<br/>
     * StringUtil.contains("ab|dc|ac", "dc", "|") == true
     *
     * @param searchStr 源字符串
     * @param subStr    子字符串
     * @param separator 分隔符
     * @return
     */

    public static boolean contains(String searchStr, String subStr, String separator) {
        searchStr = separator + searchStr + separator;
        subStr = separator + subStr + separator;
        return searchStr.indexOf(subStr) >= 0;
    }

    /**
     * 根据字节长度获取子字符串<br/>
     * StringUtil.getByteSubString("中国", 2) == "中"
     *
     * @param srcStr 源字符串
     * @param count  截取字符串的字节长度
     * @return 截取后的字符串
     */
    public static String getByteSubString(String srcStr, int count) {
        if (srcStr == null) {
            return "";
        }
        if (count < 0) {
            return "";
        }

        if (count > srcStr.length() * 3) {
            return srcStr;
        }

        char[] cs = srcStr.toCharArray();

        int c = 0, endPos = -1;
        for (int i = 0; i < cs.length; i++) {
            ++c;
            if (cs[i] > 255) {
                c += 2;
            }
            if (c == count) {
                endPos = i + 1;
                break;
            } else if (c > count) {
                endPos = i;
                break;
            }
        }

        if (endPos == -1) {
            return srcStr;
        }

        return new String(cs, 0, endPos);
    }

    /**
     * 将标识采用Camel标记法. 首字母小写,后面每个单词大写字母开头 CHARGE_ID ==> chargeId
     *
     * @param source 字符串
     * @return String
     * @author
     */
    public static String camelize(String source) {

        String the = source.toLowerCase();

        StringBuilder result = new StringBuilder();

        String[] theArray = the.split("_");
        result.append(theArray[0]);

        for (int i = 1; i < theArray.length; i++) {
            result.append(Character.toUpperCase(theArray[i].charAt(0)) + theArray[i].substring(1));
        }

        return result.toString();
    }

    /**
     * 根据指定的长度，将原字符串src拷贝到目标字符串dest中
     *
     * @param dest 目标字符串
     * @param src  原字符串
     * @param len
     * @return String
     */
    public static String strncpy(String dest, String src, int len) {

        String tmp = null;
        if (src.length() <= len) {
            dest = src;
        } else {
            tmp = src.substring(0, len);
            if (dest.length() <= len) {
                dest = tmp;
            } else {
                dest = tmp + dest.substring(len);
            }
        }
        return dest;
    }

    /**
     * 比较指定长度的2个字符串
     *
     * @param str1
     * @param str2
     * @param len
     * @return int
     */
    public static int strncmp(String str1, String str2, int len) {
        int cmpLen = len;
        if (str1.length() < cmpLen) {
            cmpLen = str1.length();
        }
        if (str2.length() < cmpLen) {
            cmpLen = str2.length();
        }
        int res = str1.substring(0, cmpLen).compareTo(str2.substring(0, cmpLen));
        if (res != 0) {
            return res;
        }
        if (len == cmpLen) {
            return 0;
        }
        if (str1.length() == str2.length()) {
            return 0;
        }
        if (str1.length() == cmpLen) {
            return -1;
        }
        return 1;
    }

    /**
     * 根据正则到字符串中查找符合正则的字符串内容
     *
     * @param str   查找字符串
     * @param regex 正则表达式
     * @return
     */
    public static String getMatchStr(CharSequence str, String regex) {
        List<String> result = getMatchList(str, regex);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    /**
     * 根据正则到字符串中查找符合正则的字符串内容的集合
     *
     * @param str   查找字符串
     * @param regex 正则表达式
     * @return
     */
    public static List<String> getMatchList(CharSequence str, String regex) {
        List<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

    /**
     * 根据正则到字符串中查找符合正则的字符串内容的集合
     *
     * @param str   查找字符串
     * @param start 开始标识
     * @param end   结束标识
     * @return
     */
    public static List<String> getMatchList(String str, String start, String end) {
        List<String> result = new ArrayList<String>();
        int startLen = start.length();
        int idx = 0;
        while ((idx = str.indexOf(start, idx)) > 0) {
            int startIdx = idx + startLen;
            int endIdx = str.indexOf(end, startIdx);
            if (startIdx < endIdx) {
                result.add(str.substring(startIdx, endIdx));
                idx = endIdx + 1;
            }
        }

        return result;
    }

    public static String replaceParamString(String source, String oldStr, String newStr) {
        int index = source.indexOf(oldStr);
        if (index == 0) {
            return newStr + source.substring(oldStr.length());
        } else {
            return index > 0 ? source.substring(0, index) + newStr + source.substring(index + oldStr.length()) : source;
        }
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 获取ip地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
            catch (UnknownHostException e) {
                log.error(e.getMessage());
            }
        }
        return ip;
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        String api = String.format(IP_URL, ip);
        JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        return object.get("addr", String.class);
    }

    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 获得当天是周几
     */
    public static String getWeekDay() {
        String[] weekDays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 通过spel表达式获取理想的key值
     *
     * @param key            参数
     * @param parameterNames 参数列表名
     * @param values         参数列表值
     * @return key对应的值
     */
    public static String getValueBySpelKey(String key, String[] parameterNames, Object[] values) {
        //不存在表达式返回
        if (!key.contains("#")) {
            return key;
        }
        //使用下划线拆分表达式
        String[] spelKeys = key.split("_");
        //要返回的key
        StringBuilder sb = new StringBuilder();
        //遍历拆分结果用解析器解析
        for (int i = 0; i <= spelKeys.length - 1; i++) {
            if (!spelKeys[i].startsWith("#")) {
                sb.append(spelKeys[i]);
                continue;
            }
            String tempKey = spelKeys[i];
            //spel解析器
            ExpressionParser parser = new SpelExpressionParser();
            //spel上下文
            EvaluationContext context = new StandardEvaluationContext();
            for (int j = 0; j < parameterNames.length; j++) {
                context.setVariable(parameterNames[j], values[j]);
            }
            Expression expression = parser.parseExpression(tempKey);
            Object value = expression.getValue(context);
            if (value != null) {
                sb.append(value);
            }
        }
        //返回
        log.debug("分布式锁key：{}", sb);
        return sb.toString();
    }

    /**
     * <p>
     * 拼接redis带前缀的地址
     * </p>
     *
     * @param address 地址
     * @return redis的地址
     */
    public static String prefixAddress(String address) {
        if (!StringUtils.isEmpty(address) && !address.startsWith("redis")) {
            return "redis://" + address;
        }
        return address;
    }

}

