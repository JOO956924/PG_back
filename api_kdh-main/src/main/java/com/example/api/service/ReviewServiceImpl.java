package com.example.api.service;

import com.example.api.dto.ReviewsDTO;
import com.example.api.entity.Boards;
import com.example.api.entity.Members;
import com.example.api.entity.Reviews;
import com.example.api.repository.MembersRepository;
import com.example.api.repository.ReviewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
  private final ReviewsRepository reviewsRepository;
  private final MembersRepository membersRepository;

  @Override
  public List<ReviewsDTO> getListOfBoards (Long bno) {
    List<Reviews> result = reviewsRepository.findByBoards(
        Boards.builder().bno(bno).build());
    return result.stream().map(reviews -> entityToDto(reviews)).collect(Collectors.toList());
  }

  @Override
  public Long register(ReviewsDTO reviewsDTO) {
    Members members = membersRepository.findById(reviewsDTO.getMid()).orElseThrow(()->new RuntimeException("회원 정보를 찾을 수 없습니다."));
    try {
      log.info("reviewsDTO >> {}", reviewsDTO); // Log properly
      Reviews reviews = dtoToEntity(reviewsDTO);
      reviewsRepository.save(reviews);
      return reviews.getReviewsnum();
    } catch (Exception e) {
      log.error("Error during register: {}", e.getMessage());
      throw e; // Re-throw the exception if needed
    }
  }
  @Override
  public void modify(ReviewsDTO reviewsDTO) {
    Optional<Reviews> result = reviewsRepository.findById(reviewsDTO.getReviewsnum());
    if (result.isPresent()) {
      Reviews reviews = result.get();
      reviews.changeGrade(reviewsDTO.getLikes());
      reviews.changeText(reviewsDTO.getText());
      reviewsRepository.save(reviews);
    }
  }

  @Override
  public void remove(Long reviewsnum) {
    reviewsRepository.deleteById(reviewsnum);
  }
}