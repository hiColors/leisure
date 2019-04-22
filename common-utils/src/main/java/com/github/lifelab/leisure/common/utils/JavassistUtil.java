package com.github.lifelab.leisure.common.utils;

import com.google.common.collect.Maps;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * JavassistUtil
 *
 * @author weichao.li (liweichao0102@gmail.com)
 * @date 2018/9/13
 */
public class JavassistUtil {

    private static final Map<String, CtClass> CT_CLASS_CACHE = Maps.newConcurrentMap();

    private JavassistUtil() {
    }

    /**
     * 默认的类搜索路径
     *
     * @return
     */
    public static ClassPool getDefault() {
        return ClassPool.getDefault();
    }

    /**
     * @param classname
     * @return
     * @throws NotFoundException
     */
    public static CtClass getCtClass(String classname) {
        try {
            if (!CT_CLASS_CACHE.containsKey(classname)) {
                try {
                    getDefault().insertClassPath(new ClassClassPath(Class.forName(classname)));
                } catch (ClassNotFoundException e) {
                    throw new NotFoundException(e.getMessage(), e);
                }
                CT_CLASS_CACHE.putIfAbsent(classname, getDefault().get(classname));
            }
        } catch (NotFoundException ignore) {

        }
        return CT_CLASS_CACHE.get(classname);
    }

    /**
     * 获取类方法参数名称
     *
     * @param classname
     * @param method
     * @return
     * @throws NotFoundException
     * @throws MissingLvException
     */
    public static String[] getParamNames(String classname, Method method) throws NotFoundException, MissingLvException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] paramTypeNames = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            paramTypeNames[i] = parameterTypes[i].getName();
        }
        CtMethod cm = getCtClass(classname).getDeclaredMethod(method.getName(), getDefault().get(paramTypeNames));
        return getParamNames(cm);
    }

    /**
     * 获取方法参数名称
     *
     * @param classname
     * @param methodname
     * @param parameterTypes
     * @return
     * @throws NotFoundException
     * @throws MissingLvException
     */
    public static String[] getParamNames(String classname, String methodname, Class<?>... parameterTypes) throws NotFoundException, MissingLvException {
        String[] paramTypeNames = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            paramTypeNames[i] = parameterTypes[i].getName();
        }
        CtMethod cm = getCtClass(classname).getDeclaredMethod(methodname, getDefault().get(paramTypeNames));
        return getParamNames(cm);
    }

    /**
     * 获取方法参数名称
     *
     * @param cm
     * @return
     * @throws NotFoundException
     * @throws MissingLvException 如果最终编译的class文件不包含局部变量表信息
     */
    protected static String[] getParamNames(CtMethod cm) throws NotFoundException, MissingLvException {
        CtClass cc = cm.getDeclaringClass();
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new MissingLvException(cc.getName());
        }
        String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }

    /**
     * 在class 中未找到局部变量表信息
     * 使用编译器选项 javac -g:{vars}来编译源文件
     *
     * @author weichao.li (liweichao0102@gmail.com)
     * @date 2018/9/13
     */
    public static class MissingLvException extends RuntimeException {
        static final String MSG = "class:%s 不包含局部变量表信息，请使用编译器选项 javac -g:{vars}来编译源文件。";

        MissingLvException(String clazzName) {
            super(String.format(MSG, clazzName));
        }
    }


}