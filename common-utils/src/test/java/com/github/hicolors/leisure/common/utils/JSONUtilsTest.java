package com.github.hicolors.leisure.common.utils;

import lombok.Builder;
import lombok.Data;
import org.junit.Test;

import java.util.Date;

public class JSONUtilsTest {

    @Test
    public void serialize() {
        String ss = JsonUtils.serialize(Person.builder().id(1L).name("leisure").birthday(new Date()).build());
        System.out.println(ss);
        ss = JsonUtils.serializeExcludes(Person.builder().id(1L).name("leisure").birthday(new Date()).build(), "id");
        System.out.println(ss);
        ss = JsonUtils.serializeExcludes(Person.builder().id(1L).name("leisure").birthday(new Date()).build(), "name");
        System.out.println(ss);
        ss = JsonUtils.serializeIncludes(Person.builder().id(1L).name("leisure").birthday(new Date()).build(), "id");
        System.out.println(ss);
        ss = JsonUtils.serializeIncludes(Person.builder().id(1L).name("leisure").birthday(new Date()).build(), "name");
        System.out.println(ss);
        ss = JsonUtils.serializeIncludes(Person.builder().id(1L).name("leisure").birthday(new Date()).build(), "birthday");
        System.out.println(ss);
    }


}

@Builder
@Data
class Person {
    private Long id;
    private String name;
    private Date birthday;
}