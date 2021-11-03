package kr.startoff.backend.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import kr.startoff.backend.entity.MeetingRoom;

@Repository
public class MeetingRoomRepository {
	private final ConcurrentHashMap<AtomicLong, MeetingRoom> meetingRooms = new ConcurrentHashMap<>();

	public Optional<MeetingRoom> findById(AtomicLong roomId) {
		return Optional.ofNullable(meetingRooms.get(roomId));
	}

	public MeetingRoom create(AtomicLong roomId) {
		MeetingRoom room = MeetingRoom.createRoom(roomId.get());
		meetingRooms.put(roomId, room);
		return room;
	}

	public Map<Long, MeetingRoom> findByAll() {
		final Map<Long, MeetingRoom> dependency = new HashMap<>();
		for (AtomicLong roomId : meetingRooms.keySet()) {
			dependency.put(roomId.get(), meetingRooms.get(roomId));
		}
		return dependency;
	}
}
