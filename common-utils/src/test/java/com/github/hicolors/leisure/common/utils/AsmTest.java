package com.github.hicolors.leisure.common.utils;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.util.UUID;

public class AsmTest implements Opcodes {

    public static byte[] test(String name) {
        ClassWriter cw = new ClassWriter(0);
        AnnotationVisitor av0;
        cw.visit(V1_8, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, name, null, "java/lang/Object", null);
        av0 = cw.visitAnnotation("com.fasterxml.jackson.annotation;", true);
        AnnotationVisitor av1 = av0.visitArray("value");
        av1.visit("value", UUID.randomUUID().toString().replace("-", ""));
        av1.visitEnd();
        av0.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }

    public static void main(String[] args) {
        byte[] clazzData = test(Person.class.getName());
    }
}
