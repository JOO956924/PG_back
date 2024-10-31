package com.example.api.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewsDTO {
  @Id
  private Long reviewsnum;
  private Long bno; // Boards
  private Long mid; // Member
  private String nickname;

  private String email;
  private int likes;
  private String text;
  private LocalDateTime regDate, modDate;
}