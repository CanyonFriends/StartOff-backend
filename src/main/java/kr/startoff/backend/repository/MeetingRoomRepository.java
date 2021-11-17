package kr.startoff.backend.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import kr.startoff.backend.entity.MeetingRoom;

@Repository
public class MeetingRoomRepository {
	private final ConcurrentHashMap<Long, MeetingRoom> meetingRooms = new ConcurrentHashMap<>();

	public Optional<MeetingRoom> findById(Long roomId) {
		return Optional.ofNullable(meetingRooms.get(roomId));
	}

	public MeetingRoom create(Long roomId) {
		if(meetingRooms.containsKey(roomId)) {
			return meetingRooms.get(roomId);
		}
		MeetingRoom room = MeetingRoom.createRoom(roomId);
		meetingRooms.put(roomId, room);
		return room;
	}

	public Map<Long, MeetingRoom> findByAll() {
		final Map<Long, MeetingRoom> defensive = new HashMap<>();
		for (Map.Entry<Long, MeetingRoom> entry : meetingRooms.entrySet()) {
			defensive.put(entry.getKey(), entry.getValue());
		}
		return defensive;
	}

	public Long delete(Long roomId) {
		return meetingRooms.remove(roomId).getRoomId();
	}
}
