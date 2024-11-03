package com.example.api.dto;

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
  private Long mid;
  private String email;
  private String pw;
  private String name;
  private String birth;
  private int nowcash;
  private String phone;
  private int addcash;
  private String level;
  private String prefer;
  private String likes;

  private boolean fromSocial;
  @Builder.Default
  private Set<String> roleSet = new HashSet<>();
  private LocalDateTime regDate;
  private LocalDateTime modDate;
}