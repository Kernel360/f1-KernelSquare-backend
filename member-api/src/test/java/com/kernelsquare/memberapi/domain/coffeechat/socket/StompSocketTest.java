//package com.kernelsquare.memberapi.domain.coffeechat.socket;
//
//import static org.assertj.core.api.Assertions.*;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//
//import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.simp.stomp.StompFrameHandler;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.messaging.simp.stomp.StompSession;
//import org.springframework.messaging.simp.stomp.StompSessionHandler;
//import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
//import org.springframework.web.socket.WebSocketHttpHeaders;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//import org.springframework.web.socket.sockjs.client.SockJsClient;
//import org.springframework.web.socket.sockjs.client.Transport;
//import org.springframework.web.socket.sockjs.client.WebSocketTransport;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.PropertyNamingStrategy;
//import com.kernelsquare.core.type.MessageType;
//import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageResponse;
//
//@DisplayName("STOMP 소켓 통신 테스트")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class StompSocketTest {
//	@LocalServerPort
//	private int port;
//
//	private SockJsClient sockJsClient;
//
//	private WebSocketStompClient stompClient;
//
//	private final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
//
//	@BeforeEach
//	public void setup() {
//		List<Transport> transports = new ArrayList<>();
//		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
//
//		this.sockJsClient = new SockJsClient(transports);
//		this.stompClient = new WebSocketStompClient(sockJsClient);
//
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
//
//		MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
//		messageConverter.setObjectMapper(objectMapper);
//
//		this.stompClient.setMessageConverter(messageConverter);
//	}
//
//	@Test
//	@DisplayName("메시지 수신 테스트")
//	public void getMessage() throws Exception {
//
//		final CountDownLatch latch = new CountDownLatch(1);
//		StompSessionHandler handler = new TestSessionHandler(latch);
//		this.stompClient.connect("ws://localhost:" + this.port + "/ws", this.headers, handler);
//
//		latch.await(30, TimeUnit.SECONDS);
//
//		assertThat(latch.getCount()).isEqualTo(0);
//	}
//
//	class TestSessionHandler extends StompSessionHandlerAdapter {
//
//		private final CountDownLatch latch;
//
//		public TestSessionHandler(CountDownLatch latch) {
//			this.latch = latch;
//		}
//
//		@Override
//		public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//			ChatMessageRequest sendMessage = ChatMessageRequest.builder()
//				.message("hi")
//				.roomKey("key")
//				.type(MessageType.TALK)
//				.sender("홍박사")
//				.build();
//
//			session.subscribe("/topic/test/room/" + sendMessage.getRoomKey(), new StompFrameHandler() {
//				@Override
//				public Type getPayloadType(StompHeaders headers) {
//					return ChatMessageResponse.class;
//				}
//
//				@Override
//				public void handleFrame(StompHeaders headers, Object payload) {
//					ChatMessageResponse receiveMessage = (ChatMessageResponse)payload;
//
//					System.out.println(receiveMessage.getRoomKey());
//
//					try {
//						assertThat(receiveMessage.getMessage()).startsWith("hi");
//						latch.countDown();
//					} catch (Throwable e) {
//						latch.countDown();
//					}
//				}
//			});
//			try {
//				session.send("/app/test/message", sendMessage);
//			} catch (Throwable t) {
//				latch.countDown();
//			}
//		}
//	}
//}