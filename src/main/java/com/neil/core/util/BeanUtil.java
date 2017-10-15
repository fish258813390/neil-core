package com.neil.core.util;

import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public final class BeanUtil {

    /**
     * 利用反射实现对象之间相同属性复制
     *
     * @param source 要复制的
     * @param target 复制给
     */
    public static void copyProperties(Object source, Object target) throws Exception {
        copyPropertiesExclude(source, target, null);
    }

    /**
     * 利用反射实现对象之间相同属性复制(深度复制)
     *
     * @param source 要复制的
     * @param target 复制给
     */
    public static void deepCopyProperties(Object source, Object target) throws Exception {
        deepCopyPropertiesExclude(source, target, null);
    }

    /**
     * 复制对象属性
     *
     * @param from
     * @param to
     * @param excludsArray 排除属性列表
     * @throws Exception
     */
    public static void copyPropertiesExclude(Object from, Object to, String[] excludsArray) throws Exception {
        List<String> excludesList = null;
        if (excludsArray != null && excludsArray.length > 0) {
            excludesList = Arrays.asList(excludsArray); // 构造列表对象
        }
        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;
        for (int i = 0; i < fromMethods.length; i++) {
            fromMethod = fromMethods[i];
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("get")) {
                continue;
            }
            // 排除列表检测
            if (excludesList != null
                    && excludesList.contains(fromMethodName.substring(3)
                    .toLowerCase())) {
                continue;
            }
            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);
            if (toMethod == null) {
                continue;
            }
            Object value = fromMethod.invoke(from, new Object[0]);
            if (value == null) {
                continue;
            }
            // 集合类判空处理
            if (value instanceof Collection) {
                Collection<?> newValue = (Collection<?>) value;
                if (newValue.size() <= 0) {
                    continue;
                }
            }

            toMethod.invoke(to, new Object[]{value});
        }
    }


    /**
     * 深度复制对象属性
     *
     * @param from
     * @param to
     * @param excludsArray 排除属性列表
     * @throws Exception
     */
    public static void deepCopyPropertiesExclude(Object from, Object to, String[] excludsArray) throws Exception {
        List<String> excludesList = null;
        if (excludsArray != null && excludsArray.length > 0) {
            excludesList = Arrays.asList(excludsArray); // 构造列表对象
        }
        Method[] fromMethods = from.getClass().getMethods();
        Method[] toMethods = to.getClass().getMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;
        for (int i = 0; i < fromMethods.length; i++) {
            fromMethod = fromMethods[i];
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("get")) {
                continue;
            }
            // 排除列表检测
            if (excludesList != null
                    && excludesList.contains(fromMethodName.substring(3)
                    .toLowerCase())) {
                continue;
            }
            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);
            if (toMethod == null) {
                continue;
            }
            Object value = fromMethod.invoke(from, new Object[0]);
            if (value == null) {
                continue;
            }
            // 集合类判空处理
            if (value instanceof Collection) {
                Collection<?> newValue = (Collection<?>) value;
                if (newValue.size() <= 0) {
                    continue;
                }
            }

            toMethod.invoke(to, new Object[]{value});
        }
    }


    /**
     * 对象属性值复制，仅复制指定名称的属性值
     *
     * @param from
     * @param to
     * @param includsArray
     * @throws Exception
     */
    public static void copyPropertiesInclude(Object from, Object to,
                                             String[] includsArray) throws Exception {

        List<String> includesList = null;
        if (includsArray != null && includsArray.length > 0) {
            includesList = Arrays.asList(includsArray);
        } else {
            return;
        }
        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;
        for (int i = 0; i < fromMethods.length; i++) {
            fromMethod = fromMethods[i];
            fromMethodName = fromMethod.getName();

            if (!fromMethodName.contains("get")) {
                continue;
            }

            // 排除列表检测
            String str = fromMethodName.substring(3);

            if (!includesList.contains(str.substring(0, 1).toLowerCase()
                    + str.substring(1))) {
                continue;
            }

            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);

            if (toMethod == null) {
                continue;
            }

            Object value = fromMethod.invoke(from, new Object[0]);

            if (value == null) {
                continue;
            }

            // 集合类判空处理
            if (value instanceof Collection) {

                Collection<?> newValue = (Collection<?>) value;

                if (newValue.size() <= 0) {
                    continue;
                }
            }

            toMethod.invoke(to, new Object[]{value});
        }
    }


    /**
     * 对象属性值复制，仅复制指定名称的属性值(深度复制)
     *
     * @param from
     * @param to
     * @param includsArray
     * @throws Exception
     */
    public static void deepCopyPropertiesInclude(Object from, Object to,
                                                 String[] includsArray) throws Exception {

        List<String> includesList = null;
        if (includsArray != null && includsArray.length > 0) {
            includesList = Arrays.asList(includsArray);
        } else {
            return;
        }
        Method[] fromMethods = from.getClass().getMethods();
        Method[] toMethods = to.getClass().getMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;
        for (int i = 0; i < fromMethods.length; i++) {
            fromMethod = fromMethods[i];
            fromMethodName = fromMethod.getName();

            if (!fromMethodName.contains("get")) {
                continue;
            }

            // 排除列表检测
            String str = fromMethodName.substring(3);

            if (!includesList.contains(str.substring(0, 1).toLowerCase()
                    + str.substring(1))) {
                continue;
            }

            toMethodName = "set" + fromMethodName.substring(3);
            toMethod = findMethodByName(toMethods, toMethodName);

            if (toMethod == null) {
                continue;
            }

            Object value = fromMethod.invoke(from, new Object[0]);

            if (value == null) {
                continue;
            }

            // 集合类判空处理
            if (value instanceof Collection) {

                Collection<?> newValue = (Collection<?>) value;

                if (newValue.size() <= 0) {
                    continue;
                }
            }

            toMethod.invoke(to, new Object[]{value});
        }
    }


    /**
     * 从方法数组中获取指定名称的方法
     *
     * @param methods
     * @param name
     * @return
     */
    public static Method findMethodByName(Method[] methods, String name) {

        for (int j = 0; j < methods.length; j++) {
            if (methods[j].getName().equals(name)) {
                return methods[j];
            }
        }
        return null;
    }


    public static Object covertToTarget(Object sourceObject, Class targetClass) throws Exception {
        Object targetObject = targetClass.newInstance();
        try {
            BeanUtils.copyProperties(targetObject, sourceObject);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("转换对象异常");
        }
        return targetObject;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean
     *            要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map convertBeanIngoreBlank(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null && !"".equals(result)) {
                    returnMap.put(propertyName, result);
                }
            }
        }
        return returnMap;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean
     *            要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map convertBean(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param type
     *            要转化的类型
     * @param map
     *            包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    @SuppressWarnings("rawtypes")
    public static Object convertMap(Class type, Map map) throws IntrospectionException, IllegalAccessException, InstantiationException,
            InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }
}
