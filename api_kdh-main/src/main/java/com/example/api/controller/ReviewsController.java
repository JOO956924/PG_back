package com.example.api.controller;

import com.example.api.dto.BoardsDTO;
import com.example.api.dto.ReviewsDTO;
import com.example.api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {
  private final ReviewService reviewService;

  @GetMapping(value = "/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ReviewsDTO>> getList(@PathVariable("bno") Long bno) {
    log.info("GET 요청의 bno: " + bno);
    List<ReviewsDTO> reviewsDTOList = reviewService.getListOfBoards(bno);
    return new ResponseEntity<>(reviewsDTOList, HttpStatus.OK);
  }

  @PostMapping("/{bno}")
  public ResponseEntity<Long> register(@RequestBody ReviewsDTO reviewsDTO, @PathVariable("bno") Long bno) {
    log.info("POST 요청이 도달했습니다. 요청 데이터: " + reviewsDTO);
    // Members 엔티티의 email을 설정
    reviewsDTO.setEmail(reviewsDTO.getEmail());
    Long reviewsnum;
    try {
      reviewsnum = reviewService.register(reviewsDTO);
      log.info("댓글이 성공적으로 등록되었습니다. 댓글 번호: " + reviewsnum);
      return new ResponseEntity<>(reviewsnum, HttpStatus.OK);
    } catch (Exception e) {
      log.error("댓글 등록 중 오류 발생: ", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{bno}/{reviewsnum}")
  public ResponseEntity<Long> modify(@RequestBody ReviewsDTO reviewsDTO) {
    log.info("PUT 요청이 도달했습니다. 요청 데이터: " + reviewsDTO);
    reviewService.modify(reviewsDTO);
    return new ResponseEntity<>(reviewsDTO.getReviewsnum(), HttpStatus.OK);
  }

  @DeleteMapping("/{bno}/{reviewsnum}")
  public ResponseEntity<Long> delete(@PathVariable Long reviewsnum) {
    log.info("DELETE 요청이 도달했습니다. 댓글 번호: " + reviewsnum);
    reviewService.remove(reviewsnum);
    return new ResponseEntity<>(reviewsnum, HttpStatus.OK);
  }
}