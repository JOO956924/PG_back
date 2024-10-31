package com.example.api.service;

import com.example.api.dto.GphotosDTO;
import com.example.api.dto.GroundsDTO;
import com.example.api.dto.PageRequestDTO;
import com.example.api.dto.PageResultDTO;
import com.example.api.entity.Gphotos;
import com.example.api.entity.Grounds;
import com.example.api.entity.Members;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// GroundsService는 구장 관련 서비스 인터페이스입니다.
public interface GroundsService {
  // 구장 등록
  Long register(GroundsDTO groundsDTO);

  // 구장 목록 조회
  PageResultDTO<GroundsDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

  // 구장 상세 조회
  GroundsDTO getGrounds(Long gno);

  // 구장 수정
  void modify(GroundsDTO groundsDTO);

  // 구장 및 관련 리뷰, 사진 삭제
  List<String> removeWithReviewsAndGphotos(Long gno);

  // UUID로 사진 삭제
  void removeUuid(String uuid);

  // 구장 예약
  void makeReservation(Long groundId);

  // 구장이름으로 gno찾기
  Long getGnoFromGtitle(String gtitle);


  // GroundsDTO를 Grounds 엔티티로 변환하는 메서드
  default Map<String, Object> dtoToEntity(GroundsDTO groundsDTO) {
    Map<String, Object> entityMap = new HashMap<>();


    // Grounds 객체 생성
    Grounds grounds = Grounds.builder()
        .gno(groundsDTO.getGno())
        .gtitle(groundsDTO.getGtitle())
        .location(groundsDTO.getLocation())
        .sports(groundsDTO.getSports())
        .price(groundsDTO.getPrice())
        .day(groundsDTO.getDay())
        .groundstime(groundsDTO.getGroundstime())
        .maxpeople(groundsDTO.getMaxpeople())
        .nowpeople(groundsDTO.getNowpeople())
        .reservation(groundsDTO.getReservation())
        .info(groundsDTO.getInfo())
        .build();

    entityMap.put("grounds", grounds); // 엔티티 맵에 구장 추가

    List<GphotosDTO> gphotosDTOList = groundsDTO.getGphotosDTOList();
    if (gphotosDTOList != null && !gphotosDTOList.isEmpty()) {
      // GphotosDTO 리스트를 Gphotos 리스트로 변환
      List<Gphotos> gphotosList = gphotosDTOList.stream()
          .map(gphotosDTO -> Gphotos.builder() // 수정: 람다 표현식으로 변환
              .path(gphotosDTO.getPath())
              .gphotosName(gphotosDTO.getGphotosName())
              .uuid(gphotosDTO.getUuid())
              .grounds(grounds) // 구장과의 관계 설정
              .build())
          .collect(Collectors.toList());
      entityMap.put("gphotosList", gphotosList); // 엔티티 맵에 사진 리스트 추가
    }
    return entityMap; // 변환된 엔티티 맵 반환
  }

  // Grounds 엔티티를 GroundsDTO로 변환하는 메서드
  default GroundsDTO entityToDto(Grounds grounds, List<Gphotos> gphotosList, Long nowpeople, Long reviewsCnt) {
    // GroundsDTO 객체 생성
    GroundsDTO groundsDTO = GroundsDTO.builder()
        .gno(grounds.getGno())
        .gtitle(grounds.getGtitle())
        .location(grounds.getLocation())
        .sports(grounds.getSports())
        .price(grounds.getPrice())
        .day(grounds.getDay())
        .groundstime(grounds.getGroundstime())
        .maxpeople(grounds.getMaxpeople())
        .nowpeople(grounds.getNowpeople())
        .reservation(grounds.getReservation())
        .info(grounds.getInfo())
        .reviewsCnt(reviewsCnt)
        .regDate(grounds.getRegDate())
        .modDate(grounds.getModDate())
        .build();

    List<GphotosDTO> gphotosDTOList = new ArrayList<>();
    if (gphotosList != null && !gphotosList.isEmpty()) {
      // Gphotos 리스트를 GphotosDTO 리스트로 변환
      gphotosDTOList = gphotosList.stream()
          .filter(gphotos -> gphotos != null) // null 필터링
          .map(gphotos -> GphotosDTO.builder()
              .gphotosName(gphotos.getGphotosName())
              .path(gphotos.getPath())
              .uuid(gphotos.getUuid())
              .build())
          .collect(Collectors.toList());
    }

    groundsDTO.setGphotosDTOList(gphotosDTOList); // 변환된 사진 리스트 추가
    return groundsDTO; // 변환된 DTO 반환
  }
}
