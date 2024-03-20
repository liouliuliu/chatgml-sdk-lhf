package cn.liuhf.chatglm.interceptor;

import cn.liuhf.chatglm.session.Configuration;
import cn.liuhf.chatglm.utils.BearerTokenUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author LiuHF
 * @date 2024/3/19 22:20
 * @description 接口拦截器
 */
public class OpenAiHTTPInterceptor implements Interceptor {

    /**
     * 智普AI Jwt加密Token
     */
    private final Configuration configuration;

    public OpenAiHTTPInterceptor(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        // 1.获取原始 Request
        Request original = chain.request();
        // 2.构建请求
        Request request = original.newBuilder()
                .url(original.url())
                .header("Authorization", "Bearer " + BearerTokenUtils.getToken(configuration.getApiKey(), configuration.getApiSecret()))
                .header("Content-Type", Configuration.JSON_CONTENT_TYPE)
                .header("User-Agent", Configuration.DEFAULT_USER_AGENT)
                .header("Accept", null != original.header("Accept") ? original.header("Accept") : Configuration.SSE_CONTENT_TYPE)
                .method(original.method(),original.body())
                .build();
        // 3.返回执行结果
        return chain.proceed(request);
    }
}
