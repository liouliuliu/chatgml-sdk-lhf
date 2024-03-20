package cn.liuhf.chatglm.session;

/**
 * @author LiuHF
 * @date 2024/3/19 21:50
 * @description 工厂接口
 */
public interface OpenAiSessionFactory {
    
    OpenAiSession openSession();
}
