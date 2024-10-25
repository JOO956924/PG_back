package com.example.api.service;

import com.example.api.dto.GroundsReviewsDTO;
import com.example.api.entity.Grounds;
import com.example.api.entity.GroundsReviews;
import com.example.api.entity.Members;

import java.util.List;

public interface GroundsReviewService {
  List<GroundsReviewsDTO> getListOfGrounds(Long gno); // 구장 리뷰 목록 조회

  Long register(GroundsReviewsDTO groundsReviewsDTO); // 구장 리뷰 등록

  void modify(GroundsReviewsDTO groundsReviewsDTO); // 구장 리뷰 수정

  void remove(Long grno); // 구장 리뷰 삭제

  // DTO를 엔티티로 변환
  default GroundsReviews dtoToEntity(GroundsReviewsDTO groundsReviewsDTO) {
    return GroundsReviews.builder()
        .grno(groundsReviewsDTO.getGrno())
        .grounds(Grounds.builder().gno(groundsReviewsDTO.getGno()).build())
        .members(Members.builder().mid(groundsReviewsDTO.getMid()).build())
        .email(groundsReviewsDTO.getEmail())
        .name(groundsReviewsDTO.getName())
        .build();
  }

  // 엔티티를 DTO로 변환
  default GroundsReviewsDTO entityToDto(GroundsReviews groundsReviews) {
    return GroundsReviewsDTO.builder()
        .grno(groundsReviews.getGrno())
        .gno(groundsReviews.getGrounds().getGno())
        .mid(groundsReviews.getMembers().getMid())
        .email(groundsReviews.getMembers().getEmail())
        .name(groundsReviews.getMembers().getName())
        .regDate(groundsReviews.getRegDate())
        .modDate(groundsReviews.getModDate())
        .build();
  }
}
