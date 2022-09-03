package top.mstudy.utils.data;

import top.mstudy.utils.StringUtils;
import top.mstudy.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author machao
 * @description: Map的工具类，提供从Map获取数据方法
 * @date 2022-09-02
 */
public class Maps {

    /**
     * 判断Map中是否包含某个key，且其值不为空
     *
     * @param map map对象
     * @param key 需要检查的Key
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map, Object key) {
        if (map == null) {
            return true;
        }
        if (!map.containsKey(key)) {
            return true;
        }
        Object obj = map.get(key);
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return StringUtils.isBlank((String) obj);
        }
        return false;
    }

    /**
     * 从Map中获取某个Key对应的对象，若为空则返回默认值
     *
     * @param map      Map对象
     * @param key      查询的Key
     * @param defValue 为空时的默认值
     * @return
     */
    public static Object get(Map<?, ?> map, Object key, Object defValue) {
        Object obj = map.get(key);

        if (obj == null) {
            return defValue;
        }

        return obj;
    }

    /**
     * 从Map中获取Key对应的字符串，若Map中没有该键值或值为null或为"",则抛出异常
     *
     * @param map Map对象
     * @param key 获取值的Key
     * @return
     */
    public static String getStringEx(Map<?, ?> map, Object key) {
        String v = getString(map, key);
        if (v == null) {
            throw new NullPointerException(v);
        }
        return v;
    }

    /**
     * 从Map中获取Key对应的字符串，若Map中没有该键值则返回null
     *
     * @param map Map对象
     * @param key 获取值的Key
     * @return
     */
    public static String getString(Map<?, ?> map, Object key) {
        return getString(map, key, null);
    }

    /**
     * 从Map中获取Key对应的字符串，若Map中没有该键值则返回传入的默认值
     *
     * @param map      Map对象
     * @param key      获取值的Key
     * @param defValue 没有值时返回的默认值
     * @return
     */
    public static String getString(Map<?, ?> map, Object key, String defValue) {
        Object obj = get(map, key, defValue);
        if (obj == null) {
            return null;
        }
        return toString(obj);
    }

    private static String toString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Date) {
            return TimeUtils.format((Date) obj);
        }
        return obj.toString();
    }

    /**
     * 从Map中获取Key对应的boolean，若Map中没有该键值则返回false
     *
     * @param map Map对象
     * @param key 获取值的Key
     * @return
     */
    public static boolean getBoolean(Map<?, ?> map, Object key) {
        return getBoolean(map, key, false);
    }

    /**
     * 从Map中获取Key对应的Boolean，若Map中没有该键值则返回传入的默认值
     *
     * @param map      Map对象
     * @param key      获取值的Key
     * @param defValue 没有值时返回的默认值
     * @return
     */
    public static boolean getBoolean(Map<?, ?> map, Object key, boolean defValue) {
        Object obj = map.get(key);
        if (obj == null) {
            return defValue;
        }

        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }

        return Boolean.parseBoolean(toString(obj));
    }

    /**
     * 从Map中获取Key对应的Int，若Map中没有该键值则返回传入的默认值
     *
     * @param map      Map对象
     * @param key      获取值的Key
     * @param defValue 没有值时返回的默认值
     * @return
     */
    public static int getInt(Map<?, ?> map, Object key, int defValue) {
        Object obj = map.get(key);
        if (obj == null) {
            return defValue;
        }

        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        String v = toString(obj);
        if (StringUtils.isBlank(v)) {
            return defValue;
        }

        return Integer.parseInt(v);
    }

    /**
     * 从Map中获取Key对应的字符串，若Map中没有该键值则抛出异常
     *
     * @param map Map对象
     * @param key 获取值的Key
     * @return
     */
    public static int getInt(Map<?, ?> map, Object key) {
        Object obj = map.get(key);
        if (obj == null) {
            throw new NullPointerException(key.toString());
        }

        if (obj instanceof Integer) {
            return (Integer) obj;
        }

        String v = toString(obj);
        if (StringUtils.isBlank(v)) {
            throw new NullPointerException(key.toString());
        }
        return Integer.parseInt(v);
    }

    /**
     * 从Map中获取Key对应的Long，若Map中没有该键值则返回传入的默认值
     *
     * @param map      Map对象
     * @param key      获取值的Key
     * @param defValue 没有值时返回的默认值
     * @return
     */
    public static long getLong(Map<?, ?> map, Object key, long defValue) {
        Object obj = map.get(key);
        if (obj == null) {
            return defValue;
        }

        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        String v = toString(obj);
        if (StringUtils.isBlank(v)) {
            return defValue;
        }
        return Long.parseLong(v);
    }

    /**
     * 从Map中获取Key对应的字符串，若Map中没有该键值则抛出异常
     *
     * @param map Map对象
     * @param key 获取值的Key
     * @return
     */
    public static long getLong(Map<?, ?> map, Object key) {
        Object obj = map.get(key);
        if (obj == null) {
            throw new NullPointerException(key.toString());
        }

        if (obj instanceof Long) {
            return (Long) obj;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        String v = toString(obj);
        if (StringUtils.isBlank(v)) {
            throw new NullPointerException(key.toString());
        }
        return Long.parseLong(v);
    }

    /**
     * 从Map中获取Key对应的List对象，若Map中没有该键值则返回空指针
     *
     * @param map Map对象
     * @param key 获取值的Key
     * @return
     */
    public static <T> List<T> getList(Map<?, ?> map, Object key) {
        return getList(map, key, null);
    }

    /**
     * 从Map中获取Key对应的List对象，若Map中没有该键值则返回传入的默认值
     *
     * @param map      Map对象
     * @param key      获取值的Key
     * @param defValue 没有值时返回的默认值
     * @return
     */
    @SuppressWarnings("unchecked") public static <T> List<T> getList(Map<?, ?> map, Object key, List<T> defValue) {
        Object obj = map.get(key);
        if (obj == null) {
            return defValue;
        }

        if (obj instanceof List) {
            return ((List<T>) obj);
        }
        return null;
    }

    /**
     * 从Map中获取Key对应的Map对象，若Map中没有该键值则返回空指针
     *
     * @param map Map对象
     * @param key 获取值的Key
     * @return
     */
    public static <K, V> Map<K, V> getMap(Map<?, ?> map, Object key) {
        return getMap(map, key, null);
    }

    /**
     * 从Map中获取Key对应的Map对象，若Map中没有该键值则返回传入的默认值
     *
     * @param map      Map对象
     * @param key      获取值的Key
     * @param defValue 没有值时返回的默认值
     * @return
     */
    @SuppressWarnings("unchecked") public static <K, V> Map<K, V> getMap(Map<?, ?> map, Object key,
            Map<K, V> defValue) {
        Object obj = map.get(key);
        if (obj == null) {
            return defValue;
        }

        if (obj instanceof Map) {
            return ((Map<K, V>) obj);
        }
        return null;
    }

    public static void addAllToItem(Map<String, List<Object>> map, String key, List<?> value) {
        List<Object> list = map.get(key);
        if (list == null) {
            list = new ArrayList<Object>();
            map.put(key, list);
        }
        list.addAll(value);
    }

    public static void addToItem(Map<String, List<Object>> map, String key, Object value) {
        List<Object> list = map.get(key);
        if (list == null) {
            list = new ArrayList<Object>();
            map.put(key, list);
        }
        list.add(value);
    }

}
