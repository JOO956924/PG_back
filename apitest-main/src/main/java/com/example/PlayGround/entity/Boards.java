//자유게시판
package com.example.PlayGround.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "members")
public class Boards extends BasicEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bno;

  private String btitle;
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  private Members members;

  public void changeTitle(String btitle) {this.btitle = btitle;}
  public void changeContent(String content) {this.content = content;}

}



