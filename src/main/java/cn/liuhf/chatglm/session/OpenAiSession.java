package cn.liuhf.chatglm.session;

import cn.liuhf.chatglm.model.ChatCompletionRequest;
import cn.liuhf.chatglm.model.ChatCompletionSyncResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author LiuHF
 * @date 2024/3/19 21:45
 * @description 会话服务接口
 */
public interface OpenAiSession {
    
    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;
    
    CompletableFuture<String> completions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException;

    ChatCompletionSyncResponse completionsSync(ChatCompletionRequest chatCompletionRequest) throws IOException;
}
