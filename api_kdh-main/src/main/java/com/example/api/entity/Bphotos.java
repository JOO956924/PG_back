package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "boards")
public class Bphotos extends BasicEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pnum;

  private String uuid; //Universally Unique IDentifier
  private String bphotosName;
  private String path;
  @ManyToOne(fetch = FetchType.LAZY)
  private Boards boards;
}