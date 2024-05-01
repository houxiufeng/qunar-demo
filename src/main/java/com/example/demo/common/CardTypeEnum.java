package com.example.demo.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yunhe.wei
 */
@Getter
@AllArgsConstructor
public enum CardTypeEnum {

    /**
     * 礼包
     */
    GIFT("gift", "礼包"),

    /**
     * 资源位
     */
    RESOURCE("resource", "资源位"),

    /**
     * 登录
     */
    LOGIN("login", "登录");

    /**
     * 代码
     */
    private final String code;

    /**
     * 描述
     */
    private final String des;
}
