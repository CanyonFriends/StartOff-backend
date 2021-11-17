package kr.startoff.backend.payload.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebRtcMessage {
	private Long roomId;
	private Long userId;
	private Long receiverId;
	private String type;
	private String socketId;
	private String data;
	private Object candidate;
	private Object sdp;

	public WebRtcMessage(Long roomId, Long userId,  Long receiverId, String type, String socketId, String data, Object candidate,
		Object sdp) {
		this.roomId = roomId;
		this.userId = userId;
		this.receiverId = receiverId;
		this.type = type;
		this.socketId = socketId;
		this.data = data;
		this.candidate = candidate;
		this.sdp = sdp;
	}
}
