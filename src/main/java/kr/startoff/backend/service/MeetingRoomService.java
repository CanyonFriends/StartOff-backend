package kr.startoff.backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import kr.startoff.backend.entity.MeetingRoom;
import kr.startoff.backend.entity.User;
import kr.startoff.backend.exception.custom.UserNotFoundException;
import kr.startoff.backend.repository.MeetingRoomRepository;
import kr.startoff.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {
	private final MeetingRoomRepository meetingRoomRepository;
	private final UserRepository userRepository;

	public Long addRoom(Long roomId) {
		MeetingRoom room = meetingRoomRepository.create(new AtomicLong(roomId));
		return room.getRoomId();
	}

	public MeetingRoom findRoom(Long roomId) {
		return meetingRoomRepository.findById(new AtomicLong(roomId)).orElseThrow();
	}

	public List<Long> findAllRoomIds() {
		return new ArrayList<>(meetingRoomRepository.findByAll().keySet());
	}

	public WebSocketSession joinRoom(Long roomId, Long userId, WebSocketSession session) {
		MeetingRoom room = meetingRoomRepository.findById(new AtomicLong(roomId)).orElseThrow();
		return room.getClients().put(userId, session);
	}

	public WebSocketSession exitRoom(Long roomId, Long userId) {
		MeetingRoom room = meetingRoomRepository.findById(new AtomicLong(roomId)).orElseThrow();
		return room.getClients().remove(userId);
	}

	public Map<Long, WebSocketSession> getClients(AtomicLong roomId) {
		MeetingRoom room = meetingRoomRepository.findById(roomId).orElse(null);
		return Optional.ofNullable(room)
			.map(r -> Collections.unmodifiableMap(r.getClients()))
			.orElse(Collections.emptyMap());
	}
}
