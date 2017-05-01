package com.wangxin.chinesepoker.Tools;

import java.lang.reflect.Field;

/**
 * Created by wangxin on 2017/4/16.
 */

public class CopyProperty {

    /**
     *
     * @author 2014-11-6 上午11:26:36
     * @param oldObj
     * @param newObj
     * @throws NoSuchFieldException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @modificationHistory=========================逻辑或功能性重大变更记录
     */
    @SuppressWarnings("rawtypes")
    public static void copyProperty(Object oldObj,Object newObj) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        //新的class
        Class newClass = newObj.getClass();
        //老的class
        Class oldClass = oldObj.getClass();
        //该类所有的属性
        Field[] newFields = newClass.getDeclaredFields();
        //新的属性
        Field newField = null;
        //老的属性
        Field oldField = null;
        for(Field f : newFields){
            //类中的属性名称
            String fieldName = f.getName();
            //通过属性名称获取属性
            newField = newClass.getDeclaredField(fieldName);
            //获取属性的值时需要设置为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。
            //值为 false 则指示反射的对象应该实施 Java 语言访问检查。
            newField.setAccessible(true);
            //根据属性获取对象上的值
            Object newObject = newField.get(newObj);
            //过滤空的属性或者一些默认值
            if (isContinue(newObject)) {
                continue;
            }
            oldField = oldClass.getDeclaredField(fieldName);
            oldField.setAccessible(true);
            oldField.set(oldObj, newObject);
        }
    }

    /**
     *  是否跳出本次循环
     * @author 2014-11-6 上午11:37:22
     * @param object
     * @return true 是 有null或者默认值
     *         false 否 有默认值
     */
    private static boolean isContinue(Object object){
        if (object == null || "".equals(object)) {
            return true;
        }
        String valueStr = object.toString();
        if ("0".equals(valueStr) || "0.0".equals(valueStr)) {
            return true;
        }
        return false;
    }
}
