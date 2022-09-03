package top.mstudy.utils.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author machao
 * @description: List类的工具类，提供获取数据的方法
 * @date 2022-09-02
 */
public class Lists {
    public enum OrderType {
        ASCEND, DESCEND
    }

    public enum KeyType {
        STRING, INTEGER, DOUBLE
    }

    /**
     * 从List中取出第一个元素Map，没有则返回null
     *
     * @param list List对象
     * @return
     */
    public static <K, V> Map<K, V> firstMap(List<?> list) {
        return getMap(list, 0);
    }

    /**
     * 从List中取出某个位置的元素Map，没有则返回null
     *
     * @param list  List对象
     * @param index 元素的索引位置
     * @return
     */
    @SuppressWarnings("unchecked") public static <K, V> Map<K, V> getMap(List<?> list, int index) {
        if (list != null && list.size() > index) {
            Object obj = list.get(index);
            if (obj instanceof Map) {
                return (Map<K, V>) obj;
            }
        }
        return null;
    }

    /**
     * 从List中取出某个位置的元素List，没有则返回null
     *
     * @param list  List对象
     * @param index 元素的索引位置
     * @return
     */
    @SuppressWarnings("unchecked") public static <T> List<T> getList(List<?> list, int index) {
        if (list != null && list.size() > index) {
            Object obj = list.get(index);
            if (obj instanceof List) {
                return (List<T>) obj;
            }
        }
        return null;
    }

    /**
     * 将任意List对象强转成某个泛型的List对象
     *
     * @param list List对象
     * @return
     */
    @SuppressWarnings("unchecked") public static <T> List<T> caseIgnore(List<?> list) {
        return (List<T>) list;
    }

    /**
     * 从List中取出某个位置的元素Map中键对应的值，没有则返回null
     *
     * @param list  List对象
     * @param index 元素的索引位置
     * @param key   Map中的键
     * @return
     */
    public static String getString(List<?> list, int index, Object key) {
        return getString(list, index, key, null);
    }

    /**
     * 从List中取出某个位置的元素Map中键对应的值，没有则返回默认值
     *
     * @param list     List对象
     * @param index    元素的索引位置
     * @param key      Map中的键
     * @param defValue 默认值
     * @return
     */
    public static String getString(List<?> list, int index, Object key, String defValue) {
        Map<?, ?> map = getMap(list, index);
        return map != null ? Maps.getString(map, key, defValue) : defValue;
    }

    /**
     * 从List中取出某个位置的元素Map中键对应的Boolean值，没有则返回false
     *
     * @param list  List对象
     * @param index 元素的索引位置
     * @param key   Map中的键
     * @return
     */
    public static boolean getBoolean(List<?> list, int index, Object key) {
        return getBoolean(list, index, key, false);
    }

    /**
     * 从List中取出某个位置的元素Map中键对应的Boolean值，没有则返回默认值
     *
     * @param list     List对象
     * @param index    元素的索引位置
     * @param key      Map中的键
     * @param defValue 默认值
     * @return
     */
    public static boolean getBoolean(List<?> list, int index, Object key, boolean defValue) {
        Map<?, ?> map = getMap(list, index);
        return map != null ? Maps.getBoolean(map, key, defValue) : defValue;
    }

    /**
     * 从List中取出某个位置的元素Map中键对应的Int值，没有则抛出异常
     *
     * @param list  List对象
     * @param index 元素的索引位置
     * @param key   Map中的键
     * @return
     */
    public static int getInt(List<?> list, int index, Object key) {
        return Maps.getInt(getMap(list, index), key);
    }

    /**
     * 从List中取出某个位置的元素Map中键对应的Int值，没有则返回默认值
     *
     * @param list     List对象
     * @param index    元素的索引位置
     * @param key      Map中的键
     * @param defValue 默认值
     * @return
     */
    public static int getInt(List<?> list, int index, Object key, int defValue) {
        Map<?, ?> map = getMap(list, index);
        return map != null ? Maps.getInt(map, key, defValue) : defValue;
    }

    /**
     * 从List中取出某个位置的元素Map中键对应的Long值，没有则抛出异常
     *
     * @param list  List对象
     * @param index 元素的索引位置
     * @param key   Map中的键
     * @return
     */
    public static long getLong(List<?> list, int index, Object key) {
        return Maps.getLong(Lists.getMap(list, index), key);
    }

    /**
     * 从List中取出某个位置的元素Map中键对应的Long值，没有则返回默认值
     *
     * @param list     List对象
     * @param index    元素的索引位置
     * @param key      Map中的键
     * @param defValue 默认值
     * @return
     */
    public static long getLong(List<?> list, int index, Object key, long defValue) {
        Map<?, ?> map = getMap(list, index);
        return map != null ? Maps.getLong(map, key, defValue) : defValue;
    }

    public static Map<String, List<Object>> toMap(List<Map<String, Object>> list) {
        return toMap(list, null);
    }

    /**
     * List转Map，一级boss接口场景用到
     *
     * @param list
     * @param keys :只取Map中某些key值,不传默认转换全部key值
     * @return
     */
    public static Map<String, List<Object>> toMap(List<Map<String, Object>> list, String[] keys) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Map<String, List<Object>> retMap = new HashMap<String, List<Object>>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            if (keys != null) {
                for (String key : keys) {
                    Maps.addToItem(retMap, key, map.get(key));
                }
            } else {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Maps.addToItem(retMap, entry.getKey(), entry.getValue());
                }
            }
        }
        return retMap;
    }

    public static final void sort(List<?> data, String key, KeyType keyType) {
        sort(data, key, keyType, OrderType.ASCEND);
    }

    public static final void sort(List<?> data, String key1, KeyType keyType1, String key2, KeyType keyType2) {
        sort(data, key1, keyType1, OrderType.ASCEND, key2, keyType2, OrderType.ASCEND);
    }

    public static final void sort(List<?> data, String key, KeyType keyType, OrderType order) {
        String[] keys = { key };
        KeyType[] types = { keyType };
        OrderType[] orders = { order };
        sort(data, keys, types, orders);
    }

    public static final void sort(List<?> data, String key1, KeyType keyType1, OrderType order1, String key2,
            KeyType keyType2, OrderType order2) {
        String[] keys = { key1, key2 };
        KeyType[] types = { keyType1, keyType2 };
        OrderType[] orders = { order1, order2 };
        sort(data, keys, types, orders);
    }

    public static final void sort(List<?> datas, String[] keys, KeyType[] keyTypes) {
        sort(datas, keys, keyTypes, (OrderType[]) null);
    }

    public static final void sort(List<?> datas, String[] keys, KeyType[] keyTypes, OrderType order) {
        OrderType[] orders = new OrderType[keyTypes.length];
        for (int i = 0; i < keyTypes.length; i++) {
            orders[i] = order;
        }
        sort(datas, keys, keyTypes, orders);
    }

    public static final void sort(List<?> datas, String[] keys, KeyType[] keyTypes, OrderType[] orders) {
        Collections.sort(datas, new MapComparator(keys, keyTypes, orders));
    }

}

