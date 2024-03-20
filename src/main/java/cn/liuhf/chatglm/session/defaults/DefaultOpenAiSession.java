package cn.liuhf.chatglm.session.defaults;

import cn.liuhf.chatglm.IOpenAiApi;
import cn.liuhf.chatglm.model.ChatCompletionRequest;
import cn.liuhf.chatglm.model.ChatCompletionResponse;
import cn.liuhf.chatglm.model.ChatCompletionSyncResponse;
import cn.liuhf.chatglm.model.EventType;
import cn.liuhf.chatglm.session.Configuration;
import cn.liuhf.chatglm.session.OpenAiSession;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author LiuHF
 * @date 2024/3/19 21:53
 * @description 会话服务
 */
public class DefaultOpenAiSession implements OpenAiSession {

    /**
     * OpenAi 接口
     */
    private final Configuration configuration;

    /**
     * 工厂事件
     */
    private final EventSource.Factory factory;

    public DefaultOpenAiSession(Configuration configuration) {
        this.configuration = configuration;
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions).replace("{model}",chatCompletionRequest.getModel().getCode()))
                .post(RequestBody.create(MediaType.parse("application/json"),chatCompletionRequest.toString()))
                .build();
        // 返回事件结果
        return factory.newEventSource(request,eventSourceListener);
    }

    @Override
    public CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException {
        // 用于执行异步任务并获取结果
        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuffer dataBuffer = new StringBuffer();
        
        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions).replace("{model}",chatCompletionRequest.getModel().getCode()))
                .post(RequestBody.create(MediaType.parse("application/json"), chatCompletionRequest.toString()))
                .build();
        
        // 异步响应请求
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
                // type 消息类型，add 增量，finish 结束，error 错误,interrupted 中断
                if (EventType.add.getCode().equals(type)){
                    dataBuffer.append(response.getData());
                }else if (EventType.finish.getCode().equals(type)){
                    future.complete(dataBuffer.toString());
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                future.completeExceptionally(new RuntimeException("Request close before completion"));
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });
        return future;
    }

    @Override
    public ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws IOException {
        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v3_completions_sync).replace("{model}",chatCompletionRequest.getModel().getCode()))
                .header("Accept",Configuration.APPLICATION_JSON)
                .post(RequestBody.create(MediaType.parse("application/json"),chatCompletionRequest.toString()))
                .build();
        OkHttpClient okHttpClient = configuration.getOkHttpClient();
        Response response = okHttpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            new RuntimeException("Request failed");
        }
        return com.alibaba.fastjson.JSON.parseObject(response.body().string(),ChatCompletionSyncResponse.class);
    }
}
