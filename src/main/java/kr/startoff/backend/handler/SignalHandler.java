package kr.startoff.backend.handler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.payload.message.WebRtcMessage;
import kr.startoff.backend.payload.message.WebSocketUserMessage;
import kr.startoff.backend.service.MeetingRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignalHandler extends TextWebSocketHandler {
	private final MeetingRoomService meetingRoomService;
	private final ObjectMapper objectMapper = new ObjectMapper();

	private static final String MSG_TYPE_TEXT = "text";
	private static final String MSG_TYPE_JOIN = "join";
	private static final String MSG_TYPE_OFFER = "offer";
	private static final String MSG_TYPE_ANSWER = "answer";
	private static final String MSG_TYPE_ICE = "ice";
	private static final String MSG_TYPE_CANDIDATE = "candidate";
	private static final String MSG_TYPE_DISCONNECT = "disconnect";
	private static final String MSG_TYPE_USERS = "users";

	@Override
	public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
		log.info("SignalHandler.afterConnectionClosed\n" + session.getId() + "\n" + status);
	}

	@Override
	public void afterConnectionEstablished(final WebSocketSession session) {
		log.info("SignalHandler.afterConnectionEstablished\n" + session.getId());
	}

	@Override
	protected void handleTextMessage(final WebSocketSession session, final TextMessage textMessage) {
		log.info("SignalHandler.handleTextMessage\n" + session.getId());
		try {
			WebRtcMessage message = objectMapper.readValue(textMessage.getPayload(), WebRtcMessage.class);
			Long roomId = message.getRoomId();
			Long userId = message.getUserId();

			switch (message.getType()) {
				case MSG_TYPE_JOIN:
					meetingRoomService.joinRoom(roomId, userId, session);
					Map<Long, WebSocketSession> clients = meetingRoomService.getClients(roomId);
					String socketId = clients.get(userId).getId();
					List<WebSocketUserMessage> users = clients.entrySet().stream()
						.filter(m -> !Objects.equals(m.getKey(), userId))
						.map(m -> new WebSocketUserMessage(m.getKey(), m.getValue()))
						.collect(Collectors.toList());
					String data = objectMapper.writeValueAsString(users);
					sendWebRtcMessage(session,
						new WebRtcMessage(
							roomId,
							userId,
							message.getReceiverId(),
							MSG_TYPE_USERS,
							socketId,
							data,
							message.getCandidate(),
							message.getSdp()));
					break;
				case MSG_TYPE_OFFER:
					log.info("MSG_TYPE_OFFER FROM " + message.getUserId() + " TO " + message.getReceiverId());
					sendToClients(message, "getOffer");
					break;
				case MSG_TYPE_ANSWER:
					log.info("MSG_TYPE_ANSWER FROM " + message.getUserId() + " TO " + message.getReceiverId());
					sendToClients(message, "getAnswer");
					break;
				case MSG_TYPE_CANDIDATE:
					log.info("MSG_TYPE_CANDIDATE FROM " + message.getUserId() + " TO " + message.getReceiverId());
					sendToClients(message, "getCandidate");
					break;
				case MSG_TYPE_DISCONNECT:
					meetingRoomService.exitRoom(roomId, userId);
					break;
				default:
					break;
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void sendToClients(WebRtcMessage message, final String setType) {
		Long userId = message.getUserId();
		Long roomId = message.getRoomId();
		Map<Long, WebSocketSession> clients = meetingRoomService.getClients(roomId);
		WebSocketSession receive = clients.get(message.getReceiverId());
		sendWebRtcMessage(receive,
			new WebRtcMessage(
				roomId,
				userId,
				message.getReceiverId(),
				setType,
				clients.get(userId).getId(),
				message.getData(),
				message.getCandidate(),
				message.getSdp()));
	}

	private void sendWebRtcMessage(WebSocketSession session, WebRtcMessage message) {
		try {
			String json = objectMapper.writeValueAsString(message);
			session.sendMessage(new TextMessage(json));
		} catch (IOException e) {
			log.debug("An error occured: {}", e.getMessage());
		}
	}
}