package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// GroundsReviewsDTO는 구장 리뷰 정보를 담는 데이터 전송 객체입니다.
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroundsReviewsDTO {
  private Long gno; // 구장 ID
  private Long grno; // 리뷰 ID
  private Long mid; // 사용자 ID
  private String email; // 사용자 이메일
  private String name; // 사용자 이름
  private LocalDateTime regDate; // 등록일
  private LocalDateTime modDate; // 수정일
}
