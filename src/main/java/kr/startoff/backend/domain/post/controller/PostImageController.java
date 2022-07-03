package kr.startoff.backend.domain.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.startoff.backend.common.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostImageController {
	private final S3UploadUtil s3UploadUtil;

	@PostMapping("/images/{user_id}")
	public ResponseEntity<String> upload(@PathVariable(value = "user_id") Long userId,
		@RequestPart("image") MultipartFile multipartFile) {
		return ResponseEntity.ok(s3UploadUtil.uploadPostImage(multipartFile, userId));
	}
}