package com.example.api.service;

import com.example.api.dto.ReviewsDTO;
import com.example.api.entity.Boards;
import com.example.api.entity.Reviews;
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

  @Override
  public List<ReviewsDTO> getListOfBoards (Long bno) {
    List<Reviews> result = reviewsRepository.findByBoards(
        Boards.builder().bno(bno).build());
    return result.stream().map(reviews -> entityToDto(reviews)).collect(Collectors.toList());
  }

  @Override
  public Long register(ReviewsDTO reviewsDTO) {
    log.info("reviewsDTO >> ", reviewsDTO);
    Reviews reviews = dtoToEntity(reviewsDTO);
    reviewsRepository.save(reviews);
    return reviews.getReviewsnum();
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
