package com.example.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "members")
public class Members extends BasicEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long mid;

  // 유니크
  @Column(unique = true)
  private String email;
  private String pw;
  private String name;
  private String birth;
  private String phone;
  private String likes;

  @OneToMany
  private List<Boards> boards;

  @OneToMany
  private List<Reviews> reviews;

  private int nowcash; // 현재 보유 캐쉬
  private int addcash; // 충전한 캐쉬
  private String level; // 내 경기 레벨
  private String prefer; // 최다로 즐긴 스포츠 (선호 종목)


  private boolean fromSocial;
  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  private Set<MembersRole> roleSet = new HashSet<>();

  public void addMemberRole(MembersRole membersRole) {
    roleSet.add(membersRole);
  }

  public void setNowcash(int nowcash) {
    this.nowcash = nowcash;
  }

  public void setLikes(String likes) {
    this.likes = likes;
  }

}