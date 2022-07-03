package kr.startoff.backend.domain.meetingroom.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.web.socket.WebSocketSession;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MeetingRoom {
	private Long roomId;
	private UUID roomUUID;
	private Map<Long, WebSocketSession> clients = new HashMap<>();

	public static MeetingRoom createRoom(Long roomId) {
		MeetingRoom room = new MeetingRoom();
		room.setRoomId(roomId);
		room.setRoomUUID(UUID.randomUUID());
		return room;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MeetingRoom that = (MeetingRoom)o;
		return Objects.equals(getRoomUUID(), that.getRoomUUID()) && Objects.equals(getRoomId(),
			that.getRoomId()) && Objects.equals(getClients(), that.getClients());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getRoomUUID(), getRoomId(), getClients());
	}
}
