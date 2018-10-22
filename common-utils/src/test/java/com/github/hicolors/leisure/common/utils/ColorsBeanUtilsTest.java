package com.github.hicolors.leisure.common.utils;

public class ColorsBeanUtilsTest {

    public static void main(String[] args) {
        A a = new A();
        AModel aModel = new AModel();


        aModel.setAge(18);

        a.setAge(17);
        a.setName("Li ming");

        ColorsBeanUtils.copyPropertiesNonNull(aModel, a);
        System.out.println(JsonUtils.serialize(a));

    }
}
