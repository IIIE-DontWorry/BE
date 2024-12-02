package com.iiie.server.service;
import com.iiie.server.domain.*;
import com.iiie.server.dto.GalleryDTO;
import com.iiie.server.exception.NotFoundException;
import com.iiie.server.repository.GalleryRepository;
import com.iiie.server.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.util.Map;
import java.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Collections;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class GalleryService {
    private final GalleryRepository galleryRepository;
    private final PatientRepository patientRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.directory-path}")
    private String directoryPath;

    @Value("${aws.s3.bucket-url}")
    private String bucketUrl;

    public GalleryService(GalleryRepository galleryRepository, PatientRepository patientRepository, S3Client s3Client) {
        this.galleryRepository = galleryRepository;
        this.patientRepository = patientRepository;
        this.s3Client = s3Client;
    }

        // 데이터 URL에서 content-type 추출
        String contentType = dataUrl.split(":")[1].split(";")[0]; // 예: "image/png"

    public List<GalleryDTO.GetGalleryResponse> getRecentGalleries(Long caregiverId, Long guardianId, Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("patient", patientId, "존재하지 않는 환자입니다."));

        // 최근 이미지 3개 조회
        List<Image> recentImages = galleryRepository.findTop3ImagesByPatientIdOrderByIdDesc(patient.getId());

        if (recentImages.isEmpty()) {
            return Collections.emptyList(); // 이미지가 없을 경우 빈 리스트 반환
        }

        List<GalleryDTO.GetGalleryResponse> responses = new ArrayList<>();
        int totalImageCount = 0; // 반환된 총 이미지 개수

        // 이미지별로 갤러리 데이터를 생성
        for (Image image : recentImages) {
            if (totalImageCount >= 3) break; // 총 이미지 개수가 3개가 되면 종료

            Gallery gallery = image.getGallery();
            responses.add(GalleryDTO.GetGalleryResponse.builder()
                    .galleryId(gallery.getGallery_id())
                    .createdBy(gallery.getCreatedBy())
                    .createdAt(gallery.getCreatedAt())
                    .title(gallery.getTitle())
                    .images(Collections.singletonList(
                            GalleryDTO.ImageInfo.builder()
                                    .imageId(image.getId())
                                    .imageUrl(image.getImageUrl())
                                    .build()
                    ))
                    .build());

            totalImageCount++; // 이미지 개수 증가
        }

        return responses;
    }

    @Transactional
    public void uploadImages(Long caregiverId, Long guardianId, Long patientId, GalleryDTO.UploadGallery uploadGallery) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("patient", patientId, "존재하지 않는 환자입니다."));

        // 갤러리 엔티티 생성
        Gallery gallery = Gallery.builder()
                .createdBy(uploadGallery.getCreatedBy())
                .title(uploadGallery.getTitle())
                .patient(patient)
                .build();

        // S3에 이미지 업로드 및 이미지 엔티티 생성
        List<Image> imageEntities = new ArrayList<>();
        for (String base64Image : uploadGallery.getImages()) {
            try {
                // Base64 문자열에서 데이터 분리
                String[] parts = base64Image.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("잘못된 Base64 이미지 데이터");
                }
                String dataUrl = parts[0]; // 데이터 URL(이미지 형식 포함)
                String base64Data = parts[1];  //실제 이미지 데이터

                // 데이터 URL에서 content-type 추출
                String contentType = dataUrl.split(":")[1].split(";")[0]; // 예: "image/png"

                // content-type에서 이미지 확장자 추출
                String extension = contentType.substring(contentType.indexOf("/") + 1); // 예: "png"

                // Base64 데이터 디코딩
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                InputStream inputStream = new ByteArrayInputStream(imageBytes);
                long contentLength = imageBytes.length;

                // 고유 파일 이름 생성
                String fileName = UUID.randomUUID().toString() + "." + extension;

                // S3에 업로드
                String imageUrl = uploadFileToS3(fileName, inputStream, contentLength);

                // 이미지 엔티티 생성
                Image image = Image.builder()
                        .imageUrl(imageUrl)
                        .gallery(gallery)
                        .build();

                imageEntities.add(image);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("잘못된 이미지 데이터: " + e.getMessage(), e);
            }
        }
        gallery.setImages(imageEntities);

        // 이미지 엔티티 생성
        Image image = Image.builder().imageUrl(imageUrl).gallery(gallery).build();

        imageEntities.add(image);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("잘못된 이미지 데이터: " + e.getMessage(), e);
      }
    }
    gallery.setImages(imageEntities);

    // 데이터베이스 저장
    galleryRepository.save(gallery);
  }

  @Transactional
  public void deleteGallery(Long galleryId) {
    Gallery gallery =
        galleryRepository
            .findById(galleryId)
            .orElseThrow(() -> new NotFoundException("gallery", galleryId, "존재하지 않는 갤러리입니다."));

    // S3에서 이미지 삭제
    for (Image image : gallery.getImages()) {
      deleteFileFromS3(image.getImageUrl());
    }

    galleryRepository.delete(gallery);
  }

  @Transactional
  public void updateGallery(GalleryDTO.UpdateGalleryRequest request) {
    Gallery gallery =
        galleryRepository
            .findById(request.getGalleryId())
            .orElseThrow(
                () -> new NotFoundException("gallery", request.getGalleryId(), "존재하지 않는 갤러리입니다."));

    // 제목 수정
    if (request.getTitle() != null) {
      gallery.setTitle(request.getTitle());
    }

    @Transactional
    public void updateGallery(GalleryDTO.UpdateGalleryRequest updateGalleryRequest) {
        Gallery gallery = galleryRepository.findById(updateGalleryRequest.getGalleryId())
                .orElseThrow(() -> new NotFoundException("gallery", updateGalleryRequest.getGalleryId(), "존재하지 않는 갤러리입니다."));

        // 제목 수정
        if (updateGalleryRequest.getTitle() != null) {
            gallery.setTitle(updateGalleryRequest.getTitle());
        }

        // 이미지 추가
        if (updateGalleryRequest.getAddImages() != null && !updateGalleryRequest.getAddImages().isEmpty()) {
            List<Image> imageEntities = new ArrayList<>();
            for (String base64Image : updateGalleryRequest.getAddImages()) {
                try {
                    // Base64 문자열에서 데이터 분리
                    String[] parts = base64Image.split(",");
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("잘못된 Base64 이미지 데이터");
                    }
                    String dataUrl = parts[0]; // 데이터 URL(이미지 형식 포함)
                    String base64Data = parts[1]; // 실제 이미지 데이터

                    // 데이터 URL에서 content-type 추출
                    String contentType = dataUrl.split(":")[1].split(";")[0]; // 예: "image/png"

                    // content-type에서 이미지 확장자 추출
                    String extension = contentType.substring(contentType.indexOf("/") + 1); // 예: "png"

                    // Base64 데이터 디코딩
                    byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                    InputStream inputStream = new ByteArrayInputStream(imageBytes);
                    long contentLength = imageBytes.length;

                    // 고유 파일 이름 생성
                    String fileName = UUID.randomUUID().toString() + "." + extension;

                    // S3에 업로드
                    String imageUrl = uploadFileToS3(fileName, inputStream, contentLength);

                    // 이미지 엔티티 생성
                    Image image = Image.builder()
                            .imageUrl(imageUrl)
                            .gallery(gallery)
                            .build();

                    imageEntities.add(image);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("잘못된 이미지 데이터: " + e.getMessage(), e);
                }
            }
            gallery.getImages().addAll(imageEntities);
        }

        // 이미지 삭제
        if (updateGalleryRequest.getDeleteImageIds() != null && !updateGalleryRequest.getDeleteImageIds().isEmpty()) {
            List<Image> imagesToRemove = gallery.getImages().stream()
                    .filter(image -> updateGalleryRequest.getDeleteImageIds().contains(image.getId()))
                    .collect(Collectors.toList());
          
          imageEntities.add(image);
        } catch (IOException e) {
          throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
        }
      }
      gallery.getImages().addAll(imageEntities);
    }

    // 이미지 삭제
    if (request.getDeleteImageIds() != null && !request.getDeleteImageIds().isEmpty()) {
      List<Image> imagesToRemove =
          gallery.getImages().stream()
              .filter(image -> request.getDeleteImageIds().contains(image.getId()))
              .collect(Collectors.toList());

      for (Image image : imagesToRemove) {
        gallery.getImages().remove(image);
        deleteFileFromS3(image.getImageUrl());
      }
    }
  }

  public String uploadFileToS3(String fileName, InputStream inputStream, long contentLength) {
    // S3 Key 생성
    String key = directoryPath + fileName;

    // S3에 파일 업로드
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder().bucket(bucketName).key(key).contentLength(contentLength).build();

    s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));

    // S3에 업로드된 파일의 URL 반환
    return bucketUrl + key;
  }

  public void deleteFileFromS3(String imageUrl) {
    // S3 키 추출
    String key = imageUrl.replace(bucketUrl, "");

    // S3 객체 삭제 요청
    DeleteObjectRequest deleteObjectRequest =
        DeleteObjectRequest.builder().bucket(bucketName).key(key).build();

    s3Client.deleteObject(deleteObjectRequest);
  }
}
