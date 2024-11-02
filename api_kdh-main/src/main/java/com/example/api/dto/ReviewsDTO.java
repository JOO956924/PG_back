package com.example.api.dto;

import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ReviewsDTO {
  @Id
  private Long reviewsnum;
  private Long bno; // Boards
  private Long mid; // Member

  private String email;
  private int likes;
  private String text;
  private LocalDateTime regDate, modDate;
}