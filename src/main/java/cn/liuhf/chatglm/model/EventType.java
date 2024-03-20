package cn.liuhf.chatglm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiuHF
 * @date 2024/3/14 22:56
 * @description
 */
@Getter
@AllArgsConstructor
public enum EventType {

    add("add", "增量"),
    finish("finish", "结束"),
    error("error", "错误"),
    interrupted("interrupted", "中断"),

            ;
    private final String code;
    private final String info;
    
}
