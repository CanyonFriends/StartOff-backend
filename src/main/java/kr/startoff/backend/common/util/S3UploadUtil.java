package kr.startoff.backend.common.util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import kr.startoff.backend.common.exception.custom.ImageUploadFailureException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor
public class S3UploadUtil {
	private AmazonS3 s3Client;

	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.region.static}")
	private String region;

	@PostConstruct
	public void setS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

		s3Client = AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withRegion(this.region)
			.build();
	}

	public String uploadProfileImage(MultipartFile file, Long userId) {
		String fileExtension = getExtension(file.getOriginalFilename()).orElseThrow(ImageUploadFailureException::new);
		String fileName = String.format("%s/profile.%s", userId, fileExtension);
		try {
			requestToUpload(file, fileName);
		} catch (IOException e) {
			throw new ImageUploadFailureException();
		}
		return s3Client.getUrl(bucket, fileName).toString();
	}

	public String uploadPostImage(MultipartFile file, Long userId) {
		String fileExtension = getExtension(file.getOriginalFilename()).orElseThrow(ImageUploadFailureException::new);
		String fileName = String.format("%s/%s.%s", userId,
			UUID.nameUUIDFromBytes((LocalDateTime.now() + file.getOriginalFilename()).getBytes()),
			fileExtension);
		try {
			requestToUpload(file, fileName);
		} catch (IOException e) {
			throw new ImageUploadFailureException();
		}
		return s3Client.getUrl(bucket, fileName).toString();
	}

	private void requestToUpload(MultipartFile file, String fileName) throws IOException {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata)
			.withCannedAcl(CannedAccessControlList.PublicRead));
	}

	private Optional<String> getExtension(String filename) {
		return Optional.ofNullable(filename)
			.filter(f -> f.contains("."))
			.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}
}
