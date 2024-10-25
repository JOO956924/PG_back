// 구장 상세 정보
package com.example.PlayGround.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"grounds", "members"})
public class GroundsReviews {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long grno;

  @ManyToOne(fetch = FetchType.LAZY)
  private Grounds grounds;

  @ManyToOne(fetch = FetchType.LAZY)
  private Members members;

  private Long maxpeople; //
  private Long nowpeople; //
  private String reservation; //
  private String groundsTime; // 경기시간





}
