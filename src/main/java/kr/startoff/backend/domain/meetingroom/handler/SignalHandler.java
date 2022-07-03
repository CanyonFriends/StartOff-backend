package kr.startoff.backend.domain.meetingroom.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.startoff.backend.domain.meetingroom.dto.WebRtcMessage;
import kr.startoff.backend.domain.meetingroom.dto.WebSocketUserMessage;
import kr.startoff.backend.domain.meetingroom.service.MeetingRoomService;
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

	Map<String, SessionInfoToUserInfo> sessionInfoToUserInfoMap = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
		SessionInfoToUserInfo info = sessionInfoToUserInfoMap.get(session.getId());
		meetingRoomService.exitRoom(info.getRoomId(), info.getUserId());
		log.info("afterConnectionClosed, ROOM ID : {} USER COUNT : {}", info.getRoomId(),
			meetingRoomService.getClients(info.getRoomId()).size());
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
			Map<Long, WebSocketSession> clients = meetingRoomService.getClients(roomId);
			switch (message.getType()) {
				case MSG_TYPE_JOIN:
					meetingRoomService.joinRoom(roomId, userId, session);
					sessionInfoToUserInfoMap.put(session.getId(),new SessionInfoToUserInfo(userId, roomId));
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
					log.info("MSG_TYPE_JOIN, ROOM ID : {} USER COUNT : {}", roomId,
						meetingRoomService.getClients(roomId).size());
					break;
				case MSG_TYPE_OFFER:
					log.info("MSG_TYPE_OFFER FROM " + message.getUserId() + " TO " + message.getReceiverId());
					sendToClient(message, "getOffer");
					break;
				case MSG_TYPE_ANSWER:
					log.info("MSG_TYPE_ANSWER FROM " + message.getUserId() + " TO " + message.getReceiverId());
					sendToClient(message, "getAnswer");
					break;
				case MSG_TYPE_CANDIDATE:
					log.info("MSG_TYPE_CANDIDATE FROM " + message.getUserId() + " TO " + message.getReceiverId());
					sendToClient(message, "getCandidate");
					break;
				case MSG_TYPE_DISCONNECT:
					String leaveUserSocketId = clients.get(userId).getId();
					meetingRoomService.exitRoom(roomId, userId);
					List<WebSocketSession> clientsSocketSession = new ArrayList<>(clients.values());
					for (WebSocketSession webSocketSession : clientsSocketSession) {
						sendWebRtcMessage(webSocketSession, new WebRtcMessage(
							roomId,
							userId,
							message.getReceiverId(),
							"userExit",
							leaveUserSocketId,
							message.getData(),
							message.getCandidate(),
							message.getSdp()));
					}
					log.info("MSG_TYPE_DISCONNECT, ROOM ID : {} USER COUNT : {}", roomId,
						meetingRoomService.getClients(roomId).size());
					break;
				default:
					break;
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void sendToClient(WebRtcMessage message, final String setType) {
		Long userId = message.getUserId();
		Long roomId = message.getRoomId();
		Map<Long, WebSocketSession> clients = meetingRoomService.getClients(roomId);
		WebSocketSession receiver = clients.get(message.getReceiverId());
		sendWebRtcMessage(receiver,
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

	private void sendWebRtcMessage(final WebSocketSession session, final WebRtcMessage message) {
		try {
			String json = objectMapper.writeValueAsString(message);
			synchronized (session) {
				session.sendMessage(new TextMessage(json));
			}
		} catch (IOException e) {
			log.debug("An error occured: {}", e.getMessage());
		}
	}

	private static class SessionInfoToUserInfo {
		private Long userId;
		private Long roomId;

		public SessionInfoToUserInfo(Long userId, Long roomId) {
			this.userId = userId;
			this.roomId = roomId;
		}

		public Long getUserId() {
			return userId;
		}

		public Long getRoomId() {
			return roomId;
		}
	}
}