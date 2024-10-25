package com.example.api.service;

import com.example.api.dto.ReviewsDTO;
import com.example.api.entity.Boards;
import com.example.api.entity.Members;
import com.example.api.entity.Reviews;

import java.util.List;

public interface ReviewService {
  List<ReviewsDTO> getListOfBoards(Long bno);
  
  Long register(ReviewsDTO reviewsDTO);

  void modify(ReviewsDTO reviewsDTO);

  void remove(Long reviewsnum);

  public default Reviews dtoToEntity(ReviewsDTO reviewsDTO) {
    Reviews reviews = Reviews.builder()
        .reviewsnum(reviewsDTO.getReviewsnum())
        .boards(Boards.builder().bno(reviewsDTO.getBno()).build())
        .members(Members.builder().mid(reviewsDTO.getMid()).build())
        .likes(reviewsDTO.getLikes())
        .text(reviewsDTO.getText())
        .build();
    return reviews;
  }

  default ReviewsDTO entityToDto(Reviews reviews) {
    ReviewsDTO reviewsDTO = ReviewsDTO.builder()
        .reviewsnum(reviews.getReviewsnum())
        .bno(reviews.getBoards().getBno())
        .mid(reviews.getMembers().getMid())
        .email(reviews.getMembers().getEmail())
        .likes(reviews.getLikes())
        .text(reviews.getText())
        .regDate(reviews.getRegDate())
        .modDate(reviews.getModDate())
        .build();
    return reviewsDTO;
  }
}
