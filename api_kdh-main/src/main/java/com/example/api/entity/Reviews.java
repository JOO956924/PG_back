package com.example.api.entity;

import com.example.api.entity.Boards;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"boards"}) // boards를 제외하여 순환 참조 방지
public class Reviews extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewsnum;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bno", referencedColumnName = "bno") // bno로 연결
  private Boards boards; // 댓글이 속하는 게시글

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="mid")
  private Members members;

  private int likes; //
  private String text; //한줄평



  public void changeGrade(int likes) { this.likes = likes; }
  public void changeText(String text) { this.text = text; }
}