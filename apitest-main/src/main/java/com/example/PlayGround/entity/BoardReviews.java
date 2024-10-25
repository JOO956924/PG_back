//자유게시판 댓글 정보
package com.example.PlayGround.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "boards")
public class BoardReviews extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long rno;
  private String text;
  private String reviewer;

  @ManyToOne(fetch = FetchType.LAZY)
  private Boards boards;

  public void changeText(String text) {
    this.text = text;
  }
}
