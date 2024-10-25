package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"boards", "members"})
public class Reviews extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewsnum;

  @ManyToOne(fetch = FetchType.LAZY)
  private Boards boards;

  @ManyToOne(fetch = FetchType.LAZY)
  private Members members;

  private int likes; //별점
  private String text; //한줄평
  public void changeGrade(int likes) {this.likes = likes;}
  public void changeText(String text) {this.text = text;}
}
