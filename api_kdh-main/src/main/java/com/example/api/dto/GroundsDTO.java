package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// GroundsDTO는 구장 정보를 담는 데이터 전송 객체입니다.
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroundsDTO {
  @Builder.Default
  private List<GphotosDTO> gphotosDTOList = new ArrayList<>(); // 구장 사진 목록

  private Long gno; // 구장 ID
  private Long reviewsCnt; // 예약자 수
  private String gtitle; // 구장 이름
  private String location; // 위치
  private String sports; // 스포츠 종류
  private String reservation = "OPEN"; // 예약 마감 여부
  private int greviewsCnt; // 예약자 수
  private String groundstime; // 경기 시간
  //  private String email; // 등록자 이메일
//  private String name; // 등록자 이름
  private String info; // 구장 정보
  private int day;
  private Long mid;
  private String email;

  private int price; // 가격
  private int maxpeople; // 최대 인원
  private int nowpeople; // 현재 인원

  private LocalDateTime regDate; // 등록일
  private LocalDateTime modDate; // 수정일

}
