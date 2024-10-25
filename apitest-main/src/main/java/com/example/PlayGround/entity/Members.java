//회원정보
package com.example.PlayGround.entity;

import com.example.PlayGround.entity.BasicEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "pg_members")
public class Members extends BasicEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  private Long mno;
  private String id;
  private String pw;
  private String name;
  private String email;
  private String birth;
  private String phone;
  private String favorite;
  private Long cash;



  private boolean fromSocial;
  @ElementCollection(fetch = FetchType.LAZY)
  @Builder.Default
  private Set<MembersRole> roleSet = new HashSet<>();

  public void addMemberRole(MembersRole membersRole) {
    roleSet.add(membersRole);
  }
}
