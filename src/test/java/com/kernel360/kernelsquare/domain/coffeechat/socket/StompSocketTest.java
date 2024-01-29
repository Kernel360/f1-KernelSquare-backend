package com.kernel360.kernelsquare.domain.coffeechat.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernel360.kernelsquare.domain.coffeechat.dto.ChatMessage;
import com.kernel360.kernelsquare.domain.coffeechat.dto.MessageType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("STOMP 소켓 통신 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompSocketTest {

    protected StompSession stompSession;

    @LocalServerPort
    private int port;

    private final String url;

    private final String endPoint;

    private final WebSocketStompClient websocketClient;

    private final BlockingQueue<ChatMessage> blockingQueue;

    private final StompSessionHandler sessionHandler;


    public StompSocketTest() {
        this.blockingQueue = new LinkedBlockingQueue<>();

        this.sessionHandler = new TestSessionHandler(blockingQueue);

        this.url = "ws://localhost:";

        this.endPoint = "/kernel-square";

        this.websocketClient = new WebSocketStompClient(new SockJsClient(createTransport()));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);

        this.websocketClient.setMessageConverter(messageConverter);
    }

    @BeforeEach
    public void connect() throws ExecutionException, InterruptedException, TimeoutException {
        this.stompSession = this.websocketClient
            .connect(url + port + endPoint, this.sessionHandler)
            .get(1, TimeUnit.SECONDS);
    }

    @AfterEach
    public void disconnect() {
        if (this.stompSession.isConnected()) {
            this.stompSession.disconnect();
        }
    }

    private List<Transport> createTransport() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class TestSessionHandler extends StompSessionHandlerAdapter {

        private final BlockingQueue<ChatMessage> blockingQueue;

        public TestSessionHandler(BlockingQueue<ChatMessage> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            blockingQueue.offer((ChatMessage) payload);
        }
    }

    @Test
    @DisplayName("메시지 전송 테스트")
    public void testSendMessage() throws InterruptedException {
        //given
        ChatMessage message = ChatMessage.builder()
            .message("hi")
            .roomKey("key")
            .type(MessageType.TALK)
            .sender("홍박사")
            .build();

        this.stompSession.subscribe("/topic/chat/room/" + message.getRoomKey(), this.sessionHandler);

        //when
        this.stompSession.send("/app/chat/message", message);

        ChatMessage receivedMessage = blockingQueue.poll(10, TimeUnit.SECONDS);

        //then
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getMessage()).isEqualTo(message.getMessage());
        assertThat(receivedMessage.getSender()).isEqualTo(message.getSender());
        assertThat(receivedMessage.getRoomKey()).isEqualTo(message.getRoomKey());
        assertThat(receivedMessage.getType()).isEqualTo(message.getType());
    }
}
