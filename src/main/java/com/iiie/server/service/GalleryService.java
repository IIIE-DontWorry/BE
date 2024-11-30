package com.iiie.server.service;
import com.iiie.server.domain.*;
import com.iiie.server.dto.GalleryDTO;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.GalleryRepository;
import com.iiie.server.repository.CaregiverRepository;
import com.iiie.server.repository.GuardianRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final CaregiverRepository caregiverRepository;
    private final GuardianRepository guardianRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.directory-path}")
    private String directoryPath;

    @Value("${aws.s3.bucket-url}")
    private String bucketUrl;

    public GalleryService(GalleryRepository galleryRepository, CaregiverRepository caregiverRepository, GuardianRepository guardianRepository, S3Client s3Client) {
        this.caregiverRepository = caregiverRepository;
        this.guardianRepository = guardianRepository;
        this.galleryRepository = galleryRepository;
        this.s3Client = s3Client;
    }

    @Transactional
    public void uploadImages(GalleryDTO.UploadImages uploadImages) {
        Patient patient = null;
        Gallery gallery = null;
        
        // 간병인일 경우
        if (uploadImages.getCaregiverId() != null) {
            Caregiver caregiver = caregiverRepository.findById(uploadImages.getCaregiverId())
                    .orElseThrow(() -> new NotFoundException("caregiver", uploadImages.getCaregiverId(), "존재하지 않는 간병인입니다."));
            patient = caregiver.getPatient();

            // 갤러리 엔티티 생성
            gallery = Gallery.builder()
                    .createdBy(uploadImages.getCaregiverId())
                    .title(uploadImages.getTitle())
                    .patient(patient)
                    .build();
        }
        // 보호자일 경우
        else if (uploadImages.getGuardianId() != null) {
            Guardian guardian = guardianRepository.findById(uploadImages.getGuardianId())
                    .orElseThrow(() -> new NotFoundException("guardian", uploadImages.getGuardianId(), "존재하지 않는 보호자입니다."));
            patient = guardian.getPatient();

            // 갤러리 엔티티 생성
            gallery = Gallery.builder()
                    .createdBy(uploadImages.getGuardianId())
                    .title(uploadImages.getTitle())
                    .patient(patient)
                    .build();
        } else {
            throw new IllegalArgumentException("Caregiver ID나 Guardian ID 중 하나를 제공해야 합니다.");
        }

        // S3에 이미지 업로드 및 이미지 엔티티 생성
        List<Image> imageEntities = new ArrayList<>();
        for (MultipartFile file : uploadImages.getImages()) {
            try (InputStream inputStream = file.getInputStream()) {
                String fileName = file.getOriginalFilename();
                long contentLength = file.getSize();

                // S3에 파일 업로드
                String imageUrl = uploadFileToS3(fileName, inputStream, contentLength);

                // 이미지 엔티티 생성
                Image image = Image.builder()
                        .imageUrl(imageUrl)
                        .gallery(gallery)
                        .build();

                imageEntities.add(image);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
            }
        }
        gallery.setImages(imageEntities);

        // 데이터베이스 저장
        galleryRepository.save(gallery);
    }

    public String uploadFileToS3(String fileName, InputStream inputStream, long contentLength) {
        // S3 Key 생성
        String key = directoryPath + UUID.randomUUID().toString() + fileName;
        
        // S3에 파일 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentLength(contentLength)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));

        // S3에 업로드된 파일의 URL 반환
        return bucketUrl + key;
    }
}
