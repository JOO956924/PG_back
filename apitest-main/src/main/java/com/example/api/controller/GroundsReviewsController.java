package com.example.api.controller;

import com.example.api.dto.GroundsReviewsDTO;
import com.example.api.service.GroundsReviewService;
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
@RequestMapping("/greviews")
public class GroundsReviewsController {
  private final GroundsReviewService groundsReviewService;

  @GetMapping(value = "/all/{gno}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<GroundsReviewsDTO>> getList(@PathVariable("gno") Long gno) {
    log.info("gno: " + gno);
    List<GroundsReviewsDTO> groundsReviewsDTOList = groundsReviewService.getListOfGrounds(gno);
    return new ResponseEntity<>(groundsReviewsDTOList, HttpStatus.OK);
  }

  @PostMapping("/{gno}")
  public ResponseEntity<Long> register(@RequestBody GroundsReviewsDTO groundsReviewsDTO) {
    log.info(">>" + groundsReviewsDTO);
    Long grno = groundsReviewService.register(groundsReviewsDTO);
    return new ResponseEntity<>(grno, HttpStatus.OK);
  }

  @PutMapping("/{gno}/{grno}")
  public ResponseEntity<Long> modify(@RequestBody GroundsReviewsDTO groundsReviewsDTO) {
    log.info(">>" + groundsReviewsDTO);
    groundsReviewService.modify(groundsReviewsDTO);
    return new ResponseEntity<>(groundsReviewsDTO.getGrno(), HttpStatus.OK);
  }

  @DeleteMapping("/{gno}/{grno}")
  public ResponseEntity<Long> delete(@PathVariable Long grno) {
    log.info(">>" + grno);
    groundsReviewService.remove(grno);
    return new ResponseEntity<>(grno, HttpStatus.OK);
  }
}