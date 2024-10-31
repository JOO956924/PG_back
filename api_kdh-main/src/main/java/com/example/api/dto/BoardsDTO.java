package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardsDTO {
  private Long bno;
  private String title;
  private String email;
  @Builder.Default // @AllArgsConstructor가 없으면 에러,기본값초기화
  private List<BphotosDTO> bphotosDTOList = new ArrayList<>();
  private double likes;
  private String body;
  private Long reviewsCnt;
  private LocalDateTime regDate;
  private LocalDateTime modDate;
}