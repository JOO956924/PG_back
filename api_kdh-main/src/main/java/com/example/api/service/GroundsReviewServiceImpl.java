package com.example.api.service;

import com.example.api.dto.GroundsReviewsDTO;
import com.example.api.entity.Grounds;
import com.example.api.entity.GroundsReviews;
import com.example.api.entity.Members;
import com.example.api.repository.GroundsReviewsRepository;
import com.example.api.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class GroundsReviewServiceImpl implements GroundsReviewService {
  private final GroundsReviewsRepository groundsReviewsRepository;
  private final MembersRepository membersRepository;

  @Override
  public List<GroundsReviewsDTO> getListOfGrounds(Long gno) {
    List<GroundsReviews> result = groundsReviewsRepository.findByGrounds(
        Grounds.builder().gno(gno).build());
    return result.stream().map(this::entityToDto).collect(Collectors.toList());
  }

  @Override
  public Long register(GroundsReviewsDTO groundsReviewsDTO) {
    Optional<Members> memberOpt = membersRepository.findById(groundsReviewsDTO.getMid());
    if (memberOpt.isPresent()) {
      Members member = memberOpt.get();
      GroundsReviews groundsReviews = GroundsReviews.builder()
          .grounds(Grounds.builder().gno(groundsReviewsDTO.getGno()).build())
          .members(member)
          .email(member.getEmail())
          .name(member.getName())
          .build();
      groundsReviewsRepository.save(groundsReviews);
      return groundsReviews.getGrno();
    } else {
      throw new IllegalArgumentException("Member not found with id: " + groundsReviewsDTO.getMid());
    }
  }

  @Override
  public void modify(GroundsReviewsDTO groundsReviewsDTO) {
    Optional<GroundsReviews> result = groundsReviewsRepository.findById(groundsReviewsDTO.getGrno());
    if (result.isPresent()) {
      GroundsReviews groundsReviews = result.get();
      groundsReviewsRepository.save(groundsReviews);
    }
  }

  @Override
  public void remove(Long grno) {
    groundsReviewsRepository.deleteById(grno);
  }
}