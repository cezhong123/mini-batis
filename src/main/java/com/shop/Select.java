package com.shop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
//Select注解，用来记录查询sql语言
//为什么要这个注解呢，因为需要在运行时通过反射获取注解中的sql语句
//也就是我在代理的时候，我能通过method对象获取到注解中的sql语句
//这样的话每个方法的sql语句就是固定的，好像不那么灵活，如果我要做复杂的多表查询，也要给每个方法写注解
//mybatis里好像是把sql写在xml里，下次学习一下
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Select {
        String value();
    }
