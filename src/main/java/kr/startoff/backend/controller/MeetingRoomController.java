package kr.startoff.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.startoff.backend.service.MeetingRoomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MeetingRoomController {
	private final MeetingRoomService meetingRoomService;

	@GetMapping("/rooms")
	public ResponseEntity<List<Long>> getAllMeetingRoomIds() {
		return ResponseEntity.ok(meetingRoomService.findAllRoomIds());
	}

	@PostMapping("/rooms")
	public ResponseEntity<Long> createMeetingRoom(@RequestParam(value = "room-id") Long roomId) {
		return ResponseEntity.ok(meetingRoomService.addRoom(roomId));
	}
}
