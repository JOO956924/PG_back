package com.example.PlayGround.dto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembersDTO {
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
  private Set<String> roleSet = new HashSet<>();
  private LocalDateTime regDate;
  private LocalDateTime modDate;
}
