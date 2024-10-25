// 구장 정보
package com.example.PlayGround.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Grounds extends BasicEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long gno;


  private String gtitle;
  public void changeTitle(String gtitle) {
    this.gtitle = gtitle;
  }

  private String location;
  private String sports;
  private Long price;
}
