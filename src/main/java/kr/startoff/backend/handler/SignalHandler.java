package kr.startoff.backend.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SignalHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
		log.info("SignalHandler.afterConnectionClosed\n" + session.getId());
	}

	// socket이 열리면 반응함
	@Override
	public void afterConnectionEstablished(final WebSocketSession session) {
		log.info("SignalHandler.afterConnectionEstablished\n" + session.getId());
	}

	// client에서 send를 하면 반응함
	@Override
	protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
		log.info("SignalHandler.handleTextMessage\n"+session.getId()+"\n"+textMessage.toString());
		sendMessage(session,new TextMessage("hello output"));
	}

	private void sendMessage(WebSocketSession session, WebSocketMessage message) {
		try {
			String json = objectMapper.writeValueAsString(message);
			session.sendMessage(new TextMessage(json));
		} catch (IOException e) {
			log.debug("An error occured: {}", e.getMessage());
		}
	}
}