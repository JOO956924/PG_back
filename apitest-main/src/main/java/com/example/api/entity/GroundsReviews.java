package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// GroundsReviews는 구장 리뷰 정보를 저장하는 엔티티입니다.
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"grounds", "members"})
public class GroundsReviews extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long grno; // 리뷰 ID

  @ManyToOne(fetch = FetchType.LAZY)
  private Grounds grounds; // 구장

  @ManyToOne(fetch = FetchType.LAZY)
  private Members members; // 사용자

  private String email; // 사용자 이메일
  private String name; // 사용자 이름
}
