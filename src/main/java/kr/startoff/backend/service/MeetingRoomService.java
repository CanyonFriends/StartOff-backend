package kr.startoff.backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import kr.startoff.backend.entity.MeetingRoom;
import kr.startoff.backend.repository.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {
	private final MeetingRoomRepository meetingRoomRepository;

	public List<Long> findAllRoomIds() {
		return new ArrayList<>(meetingRoomRepository.findByAll().keySet());
	}

	public WebSocketSession joinRoom(Long roomId, Long userId, WebSocketSession session) {
		MeetingRoom room = meetingRoomRepository.findById(roomId).orElseThrow();
		return room.getClients().put(userId, session);
	}

	public Long addRoom(Long roomId) {
		MeetingRoom room = meetingRoomRepository.create(roomId);
		return room.getRoomId();
	}

	public void exitRoom(Long roomId, Long userId) {
		MeetingRoom room = meetingRoomRepository.findById(roomId).orElseThrow();
		room.getClients().remove(userId);
		if (room.getClients().size() == 0) {
			meetingRoomRepository.delete(roomId);
		}
	}

	public Map<Long, WebSocketSession> getClients(Long roomId) {
		Optional<MeetingRoom> room = meetingRoomRepository.findById(roomId);
		return room.map(r -> Collections.unmodifiableMap(r.getClients()))
			.orElse(Collections.emptyMap());
	}
}
