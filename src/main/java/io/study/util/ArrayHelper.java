package io.study.util;



import io.entropy.lang.Variant;
import io.entropy.lang.annotation.Nonnull;
import io.entropy.reflect.ClassHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ArrayHelper {
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final Object[] EMPTY_ARGS = new Object[0];
    public ArrayHelper() {
    }

    public static boolean isEmpty(Object[] arr) {
        return arr != null && arr.length > 0;
    }

    public static <T> T[] assign(@Nonnull T[] arr, int index, T elm) {
        if (arr.length <= index) {
            T[] newArr = (T[])((Object[])Array.newInstance(arr.getClass().getComponentType(), index + 1));
            System.arraycopy(arr, 0, newArr, 0, arr.length);
            arr = newArr;
        }

        arr[index] = elm;
        return arr;
    }

    public static <T> T[] assign(@Nonnull T[] arr, int index, T elm, Class<?> componentType) {
        if (arr == null) {
            Class elmType = componentType == null ? elm.getClass() : componentType;
            arr = (T[])((Object[])Array.newInstance(elmType, index + 1));
        }

        return assign(arr, index, elm);
    }

    public static <T> T[] append(T[] arr, T elm, Class<?> componentType) {
        if (arr == null) {
            Class elmType = componentType == null ? elm.getClass() : componentType;
            arr = (T[])((Object[])Array.newInstance(elmType, 1));
            arr[0] = elm;
            return arr;
        } else {
            int length = arr.length;
            if (componentType != null) {
                T[] newArr = (T[])((Object[])Array.newInstance(componentType, length + 1));
                System.arraycopy(arr, 0, newArr, 0, arr.length);
                newArr[length] = elm;
                return newArr;
            } else {
                return assign(arr, length, elm);
            }
        }
    }

    public static <T> T[] slice(T[] arr, int startIndex, int endIndex) {
        int length = endIndex - startIndex;
        T[] newArr = (T[])((Object[])Array.newInstance(arr.getClass().getComponentType(), length));
        System.arraycopy(arr, startIndex, newArr, 0, length);
        return newArr;
    }

    public static <T> T[] slice(T[] arr, int pos) {
        return slice(arr, pos, arr.length);
    }

    public static <T> T[] concat(T[] arr1, T[] arr2) {
        if (arr1 == null) {
            return arr2;
        } else if (arr2 == null) {
            return arr1;
        } else {
            Class componentType = arr1.getClass().getComponentType();
            if (arr1.getClass().getComponentType() == arr2.getClass().getComponentType()) {
                if (arr1.length == 0) {
                    return arr2;
                }

                if (arr2.length == 0) {
                    return arr1;
                }
            } else {
                componentType = ClassHelper.determineCommonAncestor(componentType, arr2.getClass().getComponentType());
            }

            T[] newArr = (T[])((Object[])Array.newInstance(componentType, arr1.length + arr2.length));
            System.arraycopy(arr1, 0, newArr, 0, arr1.length);
            System.arraycopy(arr2, 0, newArr, arr1.length, arr2.length);
            return newArr;
        }
    }

    public static <T> int indexOf(T[] arr, Object elm) {
        if (arr == null) {
            return -1;
        } else {
            int index = 0;

            for(int length = arr.length; index < length; ++index) {
                if (Variant.eqEx(arr[index], elm)) {
                    return index;
                }
            }

            return -1;
        }
    }

    public static <T> T get(T[] arr, int index) {
        return arr != null && arr.length > index ? arr[index] : null;
    }

    public static <T> T[] toArray(Collection<?> collection, Class<T> type) {
        if (collection == null) {
            return null;
        } else {
            T[] arr = (T[])((Object[])Array.newInstance(type, collection.size()));
            return collection.toArray(arr);
        }
    }

    public static <T> T[] iteratorToArray(Iterator<?> iter, Class<T> componentType) {
        if (iter == null) {
            return null;
        } else {
            ArrayList list = new ArrayList();

            while(iter.hasNext()) {
                list.add(iter.next());
            }

            return toArray(list, componentType);
        }
    }

    public static <T> List<T> toList(Object obj) {
        if (obj == null) {
            return null;
        } else {
            int length = Array.getLength(obj);
            ArrayList list = new ArrayList(length);

            for(int i = 0; i < length; ++i) {
                Object elm = Array.get(obj, i);
                list.add(elm);
            }

            return list;
        }
    }



    public static String join(Object arr, String str) {
        if (arr == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            int len = Array.getLength(arr);

            for(int index = 0; index < len; ++index) {
                Object elm = Array.get(arr, index);
                sb.append(elm);
                if (index != len - 1) {
                    sb.append(str);
                }
            }

            return sb.toString();
        }
    }

    public static <T> int indexOf_char(char[] arr, char elm) {
        if (arr == null) {
            return -1;
        } else {
            int index = 0;

            for(int length = arr.length; index < length; ++index) {
                if (arr[index] == elm) {
                    return index;
                }
            }

            return -1;
        }
    }

    public static <T> int indexOf_int(int[] arr, int elm) {
        if (arr == null) {
            return -1;
        } else {
            int index = 0;

            for(int length = arr.length; index < length; ++index) {
                if (arr[index] == elm) {
                    return index;
                }
            }

            return -1;
        }
    }

    public static int[] toIntArray(Collection<Integer> collection) {
        if (collection == null) {
            return null;
        } else if (collection.isEmpty()) {
            return EMPTY_INT_ARRAY;
        } else {
            int[] arr = new int[collection.size()];
            int index = 0;

            for(Iterator iter = collection.iterator(); iter.hasNext(); ++index) {
                Integer elm = (Integer)iter.next();
                int value = elm == null ? 0 : elm;
                arr[index] = value;
            }

            return arr;
        }
    }

    public static long[] toLongArray(Collection<Long> collection) {
        if (collection == null) {
            return null;
        } else if (collection.isEmpty()) {
            return EMPTY_LONG_ARRAY;
        } else {
            long[] arr = new long[collection.size()];
            int index = 0;

            for(Iterator iter = collection.iterator(); iter.hasNext(); ++index) {
                Long elm = (Long)iter.next();
                long value = elm == null ? 0L : (long)elm.intValue();
                arr[index] = value;
            }

            return arr;
        }
    }
    public static final String[] EMPTY_STRINGS = new String[0];
    public static String[] toStringArray(Collection<String> collection) {
        if (collection == null) {
            return null;
        } else {
            return collection.isEmpty() ? EMPTY_STRINGS : (String[])collection.toArray(new String[collection.size()]);
        }
    }
}

