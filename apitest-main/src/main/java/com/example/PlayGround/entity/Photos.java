// 구장 사진 정보
package com.example.PlayGround.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "grounds")
public class Photos extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pno;

  private String uuid;
  private String photosName;
  private String path;

  @ManyToOne(fetch = FetchType.LAZY)
  private Grounds grounds;

}
