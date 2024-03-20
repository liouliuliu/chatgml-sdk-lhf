package cn.liuhf.chatglm;

import cn.liuhf.chatglm.model.ChatCompletionRequest;
import cn.liuhf.chatglm.model.ChatCompletionResponse;
import cn.liuhf.chatglm.model.ChatCompletionSyncResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author LiuHF
 * @date 2024/3/13 22:09
 * @description OpenAi 接口，用于扩展通用类服务
 */
public interface IOpenAiApi {

    String v3_completions = "api/paas/v3/model-api/{model}/sse-invoke";
    
    String v3_completions_sync = "api/paas/v3/model-api/{model}/invoke";
    
    Single<ChatCompletionResponse> completions(@Path("model") String model, @Body ChatCompletionRequest chatCompletionRequest);


    @POST(v3_completions_sync)
    Single<ChatCompletionSyncResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);
}
