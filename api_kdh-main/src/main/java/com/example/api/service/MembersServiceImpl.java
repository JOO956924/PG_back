package com.example.api.service;

import com.example.api.dto.MembersDTO;
import com.example.api.entity.Members;
import com.example.api.repository.GroundsRepository;
import com.example.api.repository.GroundsReviewsRepository;
import com.example.api.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService {
  private final MembersRepository membersRepository;
  private final PasswordEncoder passwordEncoder;
  private final GroundsRepository groundsRepository;

  @Override
  public MembersDTO getMembers(Long mid) {
    Optional<Members> result = membersRepository.findById(mid);
    if (result.isPresent()) return entityToDTO(result.get());
    return null;
  }

  @Override
  public MembersDTO loginCheck(String email) {
    Optional<Members> result = membersRepository.findByEmail(email);
    if (result.isPresent()) return entityToDTO(result.get());
    return null;
  }

  @Override
  public void removeMembers(Long mid) {
    membersRepository.deleteById(mid); // 가급적 사용하지 말라.
  }

  @Override
  public Long updateMembers(MembersDTO membersDTO) {
    Optional<Members> result = membersRepository.findById(membersDTO.getMid());
    if (result.isPresent()) {
      Members members = result.get();
      /* 변경할 내용은 members에 membersDTO의 내용을 변경하시오 */
      return membersRepository.save(members).getMid();
    }
    return 0L;
  }

  @Override
  public Long registerMembers(MembersDTO membersDTO) {
    membersDTO.setPw(passwordEncoder.encode(membersDTO.getPw()));
    return membersRepository.save(dtoToEnitity(membersDTO)).getMid();
  }

  @Override
  public MembersDTO getMemberByEmail(String email) {
    Optional<Members> result = membersRepository.findByEmail(email);
    if (result.isPresent()) {
      return entityToDTO(result.get());
    }
    return null;
  }

//  @Override
//  public void updateCash(MembersDTO membersDTO) {
//    Optional<Members> result = membersRepository.findById(membersDTO.getMid());
//    if (result.isPresent()) {
//      Members members = result.get();
//      members.setNowcash(membersDTO.getNowcash());
//      membersRepository.save(members);
//    } else {
//      throw new RuntimeException("Member not found");
//    }
//  }

  @Override
  public Long chargeCash(String email, int addcash) {
    Optional<Members> result = membersRepository.findByEmail(email);
    if (result.isPresent()) {
      Members members = result.get();
      int newCash = members.getNowcash() + addcash;

      if (newCash < 0) {
        throw new RuntimeException("캐쉬가 부족합니다.");
      }

      members.setNowcash(newCash);
      membersRepository.save(members);
      return members.getMid();
    }
    throw new RuntimeException("Member not found");
  }

  public void addLikes(String email, Long gno) {
    Members members = membersRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Member not found"));

    // gno를 통해 grounds에서 gtitle 조회
    String gtitle = groundsRepository.findById(gno)
        .orElseThrow(() -> new RuntimeException("Ground not found"))
        .getGtitle();

    // 현재 likes 필드 값에 새로운 gtitle 추가
    String currentLikes = members.getLikes();
    String updatedLikes = (currentLikes == null || currentLikes.isEmpty())
        ? gtitle
        : currentLikes + "," + gtitle;

    members.setLikes(updatedLikes);
    membersRepository.save(members);
  }

}