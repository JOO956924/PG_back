package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"boards"})
public class Reviews extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewsnum;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bno")
  private Boards boards;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="mid")
  private Members members;

  private int likes; //
  private String text; //한줄평

  public void changeGrade(int likes) {this.likes = likes;}
  public void changeText(String text) {this.text = text;}
}