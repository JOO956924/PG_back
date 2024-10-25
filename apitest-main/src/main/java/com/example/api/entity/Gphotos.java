package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "grounds")
public class Gphotos extends BasicEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long gpnum;

  private String uuid; //Universally Unique IDentifier
  private String gphotosName;
  private String path;
  @ManyToOne(fetch = FetchType.LAZY)
  private Grounds grounds;
}
