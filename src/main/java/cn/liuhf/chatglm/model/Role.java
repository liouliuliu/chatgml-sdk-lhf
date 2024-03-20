package cn.liuhf.chatglm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiuHF
 * @date 2024/3/19 22:30
 * @description 角色
 */
@Getter
@AllArgsConstructor
public enum Role {
    /**
     * user 用户输入的内容，role位user
     */
    user("user"),
    /**
     * 模型生成的内容，role位assistant
     */
    assistant("assistant"),

    /**
     * 系统
     */
    system("system"),

    ;
    private final String code;
}
