package kr.startoff.backend.payload.message;

import org.springframework.web.socket.WebSocketSession;

import lombok.Getter;

@Getter
public class WebSocketUserMessage {
	Long userId;
	String socketId;

	public WebSocketUserMessage(Long userId, WebSocketSession session) {
		this.userId = userId;
		this.socketId = session.getId();
	}
}
