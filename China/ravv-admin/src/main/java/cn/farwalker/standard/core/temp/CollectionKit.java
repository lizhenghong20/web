//package cn.farwalker.standard.core.temp;
//
///**
// * Created by asus on 2019/3/28.
// */
////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//import java.lang.reflect.Array;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Deque;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.Stack;
//import java.util.TreeSet;
//import java.util.Map.Entry;
//
//public class CollectionKit {
//    private CollectionKit() {
//    }
//
//    public static <T> String join(Iterable<T> collection, String conjunction) {
//        StringBuilder sb = new StringBuilder();
//        boolean isFirst = true;
//
//        Object item;
//        for(Iterator var5 = collection.iterator(); var5.hasNext(); sb.append(item)) {
//            item = (Object)var5.next();
//            if(isFirst) {
//                isFirst = false;
//            } else {
//                sb.append(conjunction);
//            }
//        }
//
//        return sb.toString();
//    }
//
//    public static <T> String join(T[] array, String conjunction) {
//        StringBuilder sb = new StringBuilder();
//        boolean isFirst = true;
//        Object[] var7 = array;
//        int var6 = array.length;
//
//        for(int var5 = 0; var5 < var6; ++var5) {
//            Object item = var7[var5];
//            if(isFirst) {
//                isFirst = false;
//            } else {
//                sb.append(conjunction);
//            }
//
//            sb.append(item);
//        }
//
//        return sb.toString();
//    }
//
//
//
//
//
//
//    public static <T, K> HashMap<T, K> newHashMap() {
//        return new HashMap();
//    }
//
//    public static <T, K> HashMap<T, K> newHashMap(int size) {
//        return new HashMap((int)((double)size / 0.75D));
//    }
//
//    public static <T> HashSet<T> newHashSet() {
//        return new HashSet();
//    }
//
//    @SafeVarargs
//    public static <T> HashSet<T> newHashSet(T... ts) {
//        HashSet set = new HashSet();
//        Object[] var5 = ts;
//        int var4 = ts.length;
//
//        for(int var3 = 0; var3 < var4; ++var3) {
//            Object t = var5[var3];
//            set.add(t);
//        }
//
//        return set;
//    }
//
//    public static <T> ArrayList<T> newArrayList() {
//        return new ArrayList();
//    }
//
//    @SafeVarargs
//    public static <T> ArrayList<T> newArrayList(T... values) {
//        return new ArrayList(Arrays.asList(values));
//    }
//
//    public static <T> T[] append(T[] buffer, T newElement) {
//        Object[] t = resize(buffer, buffer.length + 1, newElement.getClass());
//        t[buffer.length] = newElement;
//        return t;
//    }
//
//    public static <T> T[] resize(T[] buffer, int newSize, Class<?> componentType) {
//        Object[] newArray = newArray(componentType, newSize);
//        System.arraycopy(buffer, 0, newArray, 0, buffer.length >= newSize?newSize:buffer.length);
//        return newArray;
//    }
//
//    public static <T> T[] newArray(Class<?> componentType, int newSize) {
//        return (Object[]) Array.newInstance(componentType, newSize);
//    }
//
//    public static <T> T[] resize(T[] buffer, int newSize) {
//        return resize(buffer, newSize, buffer.getClass().getComponentType());
//    }
//
//    @SafeVarargs
//    public static <T> T[] addAll(T[]... arrays) {
//        if(arrays.length == 1) {
//            return arrays[0];
//        } else {
//            int length = 0;
//            Object[][] var5 = arrays;
//            int var4 = arrays.length;
//
//            Object[] result;
//            for(int array = 0; array < var4; ++array) {
//                result = var5[array];
//                if(result != null) {
//                    length += result.length;
//                }
//            }
//
//            result = newArray(arrays.getClass().getComponentType().getComponentType(), length);
//            length = 0;
//            Object[][] var6 = arrays;
//            int var8 = arrays.length;
//
//            for(var4 = 0; var4 < var8; ++var4) {
//                Object[] var7 = var6[var4];
//                if(var7 != null) {
//                    System.arraycopy(var7, 0, result, length, var7.length);
//                    length += var7.length;
//                }
//            }
//
//            return result;
//        }
//    }
//
//    public static <T> T[] clone(T[] array) {
//        return array == null?null:(Object[])array.clone();
//    }
//
//    public static int[] range(int excludedEnd) {
//        return range(0, excludedEnd, 1);
//    }
//
//    public static int[] range(int includedStart, int excludedEnd) {
//        return range(includedStart, excludedEnd, 1);
//    }
//
//    public static int[] range(int includedStart, int excludedEnd, int step) {
//        int deviation;
//        if(includedStart > excludedEnd) {
//            deviation = includedStart;
//            includedStart = excludedEnd;
//            excludedEnd = deviation;
//        }
//
//        if(step <= 0) {
//            step = 1;
//        }
//
//        deviation = excludedEnd - includedStart;
//        int length = deviation / step;
//        if(deviation % step != 0) {
//            ++length;
//        }
//
//        int[] range = new int[length];
//
//        for(int i = 0; i < length; ++i) {
//            range[i] = includedStart;
//            includedStart += step;
//        }
//
//        return range;
//    }
//
//    public static <T> List<T> sub(List<T> list, int start, int end) {
//        if(list != null && !list.isEmpty()) {
//            if(start < 0) {
//                start = 0;
//            }
//
//            if(end < 0) {
//                end = 0;
//            }
//
//            int size;
//            if(start > end) {
//                size = start;
//                start = end;
//                end = size;
//            }
//
//            size = list.size();
//            if(end > size) {
//                if(start >= size) {
//                    return null;
//                }
//
//                end = size;
//            }
//
//            return list.subList(start, end);
//        } else {
//            return null;
//        }
//    }
//
//    public static <T> List<T> sub(Collection<T> list, int start, int end) {
//        return list != null && !list.isEmpty()?sub((List)(new ArrayList(list)), start, end):null;
//    }
//
//    public static <T> boolean isEmpty(T[] array) {
//        return array == null || array.length == 0;
//    }
//
//    public static <T> boolean isNotEmpty(T[] array) {
//        return !isEmpty(array);
//    }
//
//    public static boolean isEmpty(Collection<?> collection) {
//        return collection == null || collection.isEmpty();
//    }
//
//    public static boolean isNotEmpty(Collection<?> collection) {
//        return !isEmpty(collection);
//    }
//
//    public static boolean isEmpty(Map<?, ?> map) {
//        return map == null || map.isEmpty();
//    }
//
//    public static <T> boolean isNotEmpty(Map<?, ?> map) {
//        return !isEmpty(map);
//    }
//
//    public static <T, K> Map<T, K> zip(T[] keys, K[] values) {
//        if(!isEmpty(keys) && !isEmpty(values)) {
//            int size = Math.min(keys.length, values.length);
//            HashMap map = new HashMap((int)((double)size / 0.75D));
//
//            for(int i = 0; i < size; ++i) {
//                map.put(keys[i], values[i]);
//            }
//
//            return map;
//        } else {
//            return null;
//        }
//    }
//
//    public static Map<String, String> zip(String keys, String values, String delimiter) {
//        return zip((Object[])StrKit.split(keys, delimiter), (Object[])StrKit.split(values, delimiter));
//    }
//
//    public static <T, K> Map<T, K> zip(Collection<T> keys, Collection<K> values) {
//        if(!isEmpty(keys) && !isEmpty(values)) {
//            ArrayList keyList = new ArrayList(keys);
//            ArrayList valueList = new ArrayList(values);
//            int size = Math.min(keys.size(), values.size());
//            HashMap map = new HashMap((int)((double)size / 0.75D));
//
//            for(int i = 0; i < size; ++i) {
//                map.put(keyList.get(i), valueList.get(i));
//            }
//
//            return map;
//        } else {
//            return null;
//        }
//    }
//
//    public static <T> boolean contains(T[] array, T value) {
//        Class componetType = array.getClass().getComponentType();
//        boolean isPrimitive = false;
//        if(componetType != null) {
//            isPrimitive = componetType.isPrimitive();
//        }
//
//        Object[] var7 = array;
//        int var6 = array.length;
//
//        for(int var5 = 0; var5 < var6; ++var5) {
//            Object t = var7[var5];
//            if(t == value) {
//                return true;
//            }
//
//            if(!isPrimitive && value != null && value.equals(t)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public static <T, K> HashMap<T, K> toMap(Collection<Map.Entry<T, K>> entryCollection) {
//        HashMap map = new HashMap();
//        Iterator var3 = entryCollection.iterator();
//
//        while(var3.hasNext()) {
//            Map.Entry entry = (Map.Entry)var3.next();
//            map.put(entry.getKey(), entry.getValue());
//        }
//
//        return map;
//    }
//
//    public static <T> TreeSet<T> toTreeSet(Collection<T> collection, Comparator<T> comparator) {
//        TreeSet treeSet = new TreeSet(comparator);
//        Iterator var4 = collection.iterator();
//
//        while(var4.hasNext()) {
//            Object t = (Object)var4.next();
//            treeSet.add(t);
//        }
//
//        return treeSet;
//    }
//
//    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
//        ArrayList list = new ArrayList(collection);
//        Collections.sort(list, comparator);
//        return list;
//    }
//
//    public static Integer[] wrap(int... values) {
//        int length = values.length;
//        Integer[] array = new Integer[length];
//
//        for(int i = 0; i < length; ++i) {
//            array[i] = Integer.valueOf(values[i]);
//        }
//
//        return array;
//    }
//
//    public static Long[] wrap(long... values) {
//        int length = values.length;
//        Long[] array = new Long[length];
//
//        for(int i = 0; i < length; ++i) {
//            array[i] = Long.valueOf(values[i]);
//        }
//
//        return array;
//    }
//
//    public static Character[] wrap(char... values) {
//        int length = values.length;
//        Character[] array = new Character[length];
//
//        for(int i = 0; i < length; ++i) {
//            array[i] = Character.valueOf(values[i]);
//        }
//
//        return array;
//    }
//
//    public static Byte[] wrap(byte... values) {
//        int length = values.length;
//        Byte[] array = new Byte[length];
//
//        for(int i = 0; i < length; ++i) {
//            array[i] = Byte.valueOf(values[i]);
//        }
//
//        return array;
//    }
//
//    public static Short[] wrap(short... values) {
//        int length = values.length;
//        Short[] array = new Short[length];
//
//        for(int i = 0; i < length; ++i) {
//            array[i] = Short.valueOf(values[i]);
//        }
//
//        return array;
//    }
//
//    public static Float[] wrap(float... values) {
//        int length = values.length;
//        Float[] array = new Float[length];
//
//        for(int i = 0; i < length; ++i) {
//            array[i] = Float.valueOf(values[i]);
//        }
//
//        return array;
//    }
//
//    public static Double[] wrap(double... values) {
//        int length = values.length;
//        Double[] array = new Double[length];
//
//        for(int i = 0; i < length; ++i) {
//            array[i] = Double.valueOf(values[i]);
//        }
//
//        return array;
//    }
//
//    public static Boolean[] wrap(boolean... values) {
//        int length = values.length;
//        Boolean[] array = new Boolean[length];
//
//        for(int i = 0; i < length; ++i) {
//            array[i] = Boolean.valueOf(values[i]);
//        }
//
//        return array;
//    }
//
//    public static boolean isArray(Object obj) {
//        return obj.getClass().isArray();
//    }
//
//    public static String toString(Object obj) {
//        if(obj == null) {
//            return null;
//        } else if(isArray(obj)) {
//            try {
//                return Arrays.deepToString((Object[])obj);
//            } catch (Exception var4) {
//                String className = obj.getClass().getComponentType().getName();
//                switch(className.hashCode()) {
//                    case -1325958191:
//                        if(className.equals("double")) {
//                            return Arrays.toString((double[])obj);
//                        }
//                        break;
//                    case 104431:
//                        if(className.equals("int")) {
//                            return Arrays.toString((int[])obj);
//                        }
//                        break;
//                    case 3039496:
//                        if(className.equals("byte")) {
//                            return Arrays.toString((byte[])obj);
//                        }
//                        break;
//                    case 3052374:
//                        if(className.equals("char")) {
//                            return Arrays.toString((char[])obj);
//                        }
//                        break;
//                    case 3327612:
//                        if(className.equals("long")) {
//                            return Arrays.toString((long[])obj);
//                        }
//                        break;
//                    case 64711720:
//                        if(className.equals("boolean")) {
//                            return Arrays.toString((boolean[])obj);
//                        }
//                        break;
//                    case 97526364:
//                        if(className.equals("float")) {
//                            return Arrays.toString((float[])obj);
//                        }
//                        break;
//                    case 109413500:
//                        if(className.equals("short")) {
//                            return Arrays.toString((short[])obj);
//                        }
//                }
//
//            }
//        } else {
//            return obj.toString();
//        }
//    }
//}
//
