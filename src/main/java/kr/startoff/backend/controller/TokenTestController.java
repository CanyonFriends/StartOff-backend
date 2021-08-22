package kr.startoff.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.startoff.backend.util.CookieUtil;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/v1/test")
public class TokenTestController {
	@GetMapping("/token/user")
	@PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<String> getUser() {
		return ResponseEntity.ok("user");
	}

	@GetMapping("/token/admin")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<String> getAdmin() {
		return ResponseEntity.ok("admin");
	}

	@GetMapping("/cookie")
	public ResponseEntity<String> getCookie(HttpServletRequest request) {
		Optional<Cookie> cookie = CookieUtil.getCookie(request, "react");
		if (cookie.isEmpty()) {
			return ResponseEntity.badRequest().body("쿠키가 없어요.");
		}
		return ResponseEntity.ok(cookie.get().toString());
	}

	@PostMapping("/list")
	public ResponseEntity<Map<String, String>> getList(@RequestBody TestList listRequest) {
		Map<String, String> result = new HashMap<>();
		List<String> stringList = listRequest.getStringList();
		for (String s : stringList) {
			result.put(s, "value");
		}
		return ResponseEntity.ok(result);
	}

	@NoArgsConstructor
	static class TestList {
		List<String> stringList;

		public TestList(List<String> stringList) {
			this.stringList = stringList;
		}

		public List<String> getStringList() {
			return stringList;
		}
	}
}
