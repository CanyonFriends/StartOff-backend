package kr.startoff.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static kr.startoff.backend.payload.PayloadFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDateTime;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.startoff.backend.common.config.SecurityConfig;
import kr.startoff.backend.common.util.S3UploadUtil;
import kr.startoff.backend.domain.post.controller.PostImageController;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PostImageController.class,
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	}
)
class PostImageControllerTest {
	MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;
	@MockBean
	S3UploadUtil s3UploadUtil;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void uploadTest() throws Exception {
		MockMultipartFile file = new MockMultipartFile("image", "test.jpeg",
			MediaType.IMAGE_JPEG_VALUE, Base64.getEncoder().encode("test".getBytes()));
		String newImageUrl = String.format("https://bucket.s3.ap-northeast-2.amazonaws.com/1/%s.jpeg",
			java.util.UUID.nameUUIDFromBytes((LocalDateTime.now() + file.getOriginalFilename()).getBytes()));
		given(s3UploadUtil.uploadPostImage(any(), eq(USER_ID)))
			.willReturn(newImageUrl);

		MvcResult result = mockMvc.perform(multipart("/api/v1/images/{user_id}", USER_ID)
			.file(file)
			.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
			.accept(MediaType.MULTIPART_FORM_DATA_VALUE)).andReturn();

		assertEquals(newImageUrl, result.getResponse().getContentAsString());
	}
}