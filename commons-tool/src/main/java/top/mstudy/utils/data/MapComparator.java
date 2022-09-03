package top.mstudy.utils.data;

import top.mstudy.utils.data.Lists.KeyType;
import top.mstudy.utils.data.Lists.OrderType;

import java.util.Comparator;
import java.util.Map;
/**
 * @author machao
 * @description:
 * @date 2022-09-02
 */

public class MapComparator implements Comparator<Object> {
    String[] keys = null;

    KeyType[] keyTypes = null;

    OrderType[] orders = null;

    public MapComparator(String[] keys, KeyType[] keyTypes, OrderType[] orders) {
        if (keys.length != keyTypes.length || (orders != null && keys.length != orders.length)) {
            throw new RuntimeException("DataComparator init param not match");
        }
        this.keys = keys;
        this.keyTypes = keyTypes;
        this.orders = orders;
    }

    @Override
    public int compare(Object o1, Object o2) {
        return compare((Map<?, ?>) o1, (Map<?, ?>) o2);
    }

    public int compare(Map<?, ?> o1, Map<?, ?> o2) {

        for (int i = 0; i < keys.length; i++) {
            KeyType keyType = keyTypes[i];
            String key = keys[i];
            OrderType order = orders == null ? OrderType.ASCEND : orders[i];
            int v = 0;

            switch (keyType) {
            case INTEGER:
                int i1 = Maps.getInt(o1, key);
                int i2 = Maps.getInt(o2, key);

                v = OrderType.ASCEND == order ? i1 - i2 : i2 - i1;
                break;
            case DOUBLE:
                String d1 = Maps.getString(o1, key);
                String d2 = Maps.getString(o2, key);
                String s1 = null, s2 = null;

                int dot1Idx = d1.indexOf('.');
                int dot2Idx = d2.indexOf('.');

                if (dot1Idx != dot2Idx) {
                    v = dot2Idx - dot1Idx;
                } else {
                    s1 = d1.substring(0, dot1Idx);
                    s2 = d2.substring(0, dot2Idx);

                    v = s2.compareTo(s1);
                    if (v == 0) {
                        s1 = d1.substring(dot1Idx + 1);
                        s2 = d2.substring(dot2Idx + 1);

                        v = s2.compareTo(s1);
                    }
                }
                v = (OrderType.ASCEND == order ? v : -v);
                break;
            case STRING:
                String v1 = Maps.getString(o1, key);
                String v2 = Maps.getString(o2, key);

                v = (OrderType.ASCEND == order ? 1 : -1) * v1.compareTo(v2);
                break;
            }
            if (v != 0) {
                return v;
            }
        }
        return 0;
    }
}

