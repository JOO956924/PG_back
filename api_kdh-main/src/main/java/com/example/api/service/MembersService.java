package com.example.api.service;

import com.example.api.dto.MembersDTO;
import com.example.api.entity.Members;
import com.example.api.entity.MembersRole;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface MembersService {
  default Members dtoToEnitity(MembersDTO membersDTO) {
    Members members = Members.builder()
        .mid(membersDTO.getMid())
        .email(membersDTO.getEmail())
        .pw(membersDTO.getPw())
        .name(membersDTO.getName())
        .phone(membersDTO.getPhone())
        .birth(membersDTO.getBirth())
        .likes(membersDTO.getLikes())
        .nowcash(membersDTO.getNowcash())
        .addcash(membersDTO.getAddcash())
        .level(membersDTO.getLevel())
        .prefer(membersDTO.getPrefer())
        .bnotitle(membersDTO.getBnotitle())
        .fromSocial(membersDTO.isFromSocial())
        .roleSet(membersDTO.getRoleSet().stream().map(new Function<String, MembersRole>() {
          @Override
          public MembersRole apply(String str) {
            if (str.equals("ROLE_USER")) return MembersRole.USER;
            else if (str.equals("ROLE_MANAGER")) return MembersRole.MANAGER;
            else if (str.equals("ROLE_ADMIN")) return MembersRole.ADMIN;
            else return MembersRole.USER;
          }
        }).collect(Collectors.toSet()))
        .build();
    return members;
  }

  default MembersDTO entityToDTO(Members members) {
    MembersDTO membersDTO = MembersDTO.builder()
        .mid(members.getMid())
        .email(members.getEmail())
        .pw(members.getPw())
        .name(members.getName())
        .phone(members.getPhone())
        .likes(members.getLikes())
        .birth(members.getBirth())
        .nowcash(members.getNowcash())
        .addcash(members.getAddcash())
        .level(members.getLevel())
        .prefer(members.getPrefer())
        .bnotitle(members.getBnotitle())
        .fromSocial(members.isFromSocial())
        .regDate(members.getRegDate())
        .modDate(members.getModDate())
        .roleSet(members.getRoleSet().stream().map(new Function<MembersRole, String >() {
          @Override
          public String apply(MembersRole membersRole) {
            return new String("ROLE_" + membersRole.name());
          }
        }).collect(Collectors.toSet()))
        .build();
    return membersDTO;
  }

  Long registerMembers(MembersDTO membersDTO);
  Long updateMembers(MembersDTO membersDTO);
  void removeMembers(Long mid);
  MembersDTO getMembers(Long mid);
  MembersDTO loginCheck(String email);
  MembersDTO getMemberByEmail(String email);
  //  void updateCash(MembersDTO membersDTO);
  Long chargeCash(String email, int addcash);
  void addLikes(String email, Long gno);
  void removeLike(String email, Long gno);
  Optional<Set<String>> getMemberRolesByEmail(String email);
}