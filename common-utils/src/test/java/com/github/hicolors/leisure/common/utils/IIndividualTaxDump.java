package com.github.hicolors.leisure.common.utils;

import org.objectweb.asm.*;

public class IIndividualTaxDump implements Opcodes {

    public static byte[] dump() throws Exception {

        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(V1_7, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, "cn/com/*/*/gateway/service/IIndividualTax", null, "java/lang/Object", null);

        {
            av0 = cw.visitAnnotation("Ljavax/ws/rs/Path;", true);
            av0.visit("value", "it");
            av0.visitEnd();
        }
        {
            av0 = cw.visitAnnotation("Ljavax/ws/rs/Consumes;", true);
            {
                AnnotationVisitor av1 = av0.visitArray("value");
                av1.visit(null, "application/json");
                av1.visit(null, "text/xml");
                av1.visitEnd();
            }
            av0.visitEnd();
        }
        {
            av0 = cw.visitAnnotation("Ljavax/ws/rs/Produces;", true);
            {
                AnnotationVisitor av1 = av0.visitArray("value");
                av1.visit(null, "application/json; charset=UTF-8");
                av1.visit(null, "text/xml; charset=UTF-8");
                av1.visitEnd();
            }
            av0.visitEnd();
        }
        {
            av0 = cw.visitAnnotation("Lcn/com/*/*/gateway/service/ServiceType;", false);
            av0.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "request", "(Lcn/com/*/*/gateway/dto/BusinessRequest;)Lcn/com/*/*/gateway/dto/BusinessResponse;", null, new String[]{"cn/com/*/*/gateway/exception/GatewayException"});
            {
                av0 = mv.visitAnnotation("Ljavax/ws/rs/POST;", true);
                av0.visitEnd();
            }
            {
                av0 = mv.visitAnnotation("Ljavax/ws/rs/Path;", true);
                av0.visit("value", "request");
                av0.visitEnd();
            }
            mv.visitEnd();
        }
        cw.visitEnd();

        return cw.toByteArray();
    }
}
