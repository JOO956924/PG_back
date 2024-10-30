package com.example.api.controller;

import com.example.api.dto.UploadResultDTO;
import com.example.api.repository.BphotosRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UploadController {
  private final BphotosRepository bphotosRepository;

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @PostMapping("/uploadAjax")
  public ResponseEntity<List<UploadResultDTO>> upload(MultipartFile[] uploadFiles) {
    List<UploadResultDTO> resultDTOList = new ArrayList<>();

    for (MultipartFile multipartFile : uploadFiles) {
      String originalName = multipartFile.getOriginalFilename();
      String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
      log.info("fileName: " + fileName);

      String folderPath = makeFolder(); // yyyy/MM/dd
      String uuid = UUID.randomUUID().toString(); // unique identifier
      String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
      Path savePath = Paths.get(saveName);

      try {
        // 원본 파일 저장
        multipartFile.transferTo(savePath);

        // 섬네일 생성
        String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;
        File thumbnailFile = new File(thumbnailSaveName);
        Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

        resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
      } catch (IOException e) {
        log.error("File upload error: " + e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
  }

  @GetMapping("/display")
  public ResponseEntity<byte[]> getImageFile(String fileName, String size) {
    ResponseEntity<byte[]> result = null;
    try {
      String searchFilename = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + searchFilename);

      if (size != null && size.equals("1")) {
        log.info("Thumbnail view requested for: " + file.getName());
        // 미리보기 섬네일 이름에서 "s_"를 제거하여 원본 이미지 참조
        file = new File(file.getParent(), file.getName().substring(2));
      }

      log.info("Serving file: " + file);
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", Files.probeContentType(file.toPath()));

      result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
    } catch (Exception e) {
      log.error("Error retrieving image file: " + e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return result;
  }

  @Transactional
  @PostMapping("/removeFile")
  public ResponseEntity<Boolean> removeFile(String fileName, String uuid) {
    log.info("Requested file removal: " + fileName);

    if (fileName == null || fileName.isEmpty()) {
      log.error("Invalid file name: null or empty");
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    if (uuid != null) {
      bphotosRepository.deleteByUuid(uuid);
      log.info("Database record with UUID " + uuid + " deleted.");
    }

    try {
      String searchFilename = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + searchFilename);

      if (!file.exists()) {
        log.warn("File not found: " + searchFilename);
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
      }

      boolean result1 = file.delete();
      File thumbnail = new File(file.getParent(), "s_" + file.getName());
      boolean result2 = thumbnail.exists() ? thumbnail.delete() : true;

      boolean deletionResult = result1 && result2;
      log.info("File deletion result: " + deletionResult + " (File: " + result1 + ", Thumbnail: " + result2 + ")");

      return new ResponseEntity<>(deletionResult, deletionResult ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (Exception e) {
      log.error("Error while deleting file: " + e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private String makeFolder() {
    String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String folderPath = str.replace("/", File.separator);
    File uploadPathFolder = new File(uploadPath, folderPath);
    if (!uploadPathFolder.exists()) uploadPathFolder.mkdirs();
    return folderPath;
  }
}
