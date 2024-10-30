package com.example.api.repository;

import com.example.api.entity.Grounds;
import com.example.api.entity.GroundsReviews;
import com.example.api.entity.Members;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroundsReviewsRepositoryTests {
  @Autowired
  GroundsReviewsRepository groundsReviewsRepository;
  @Autowired
  MembersRepository membersRepository;

  @Test
  public void insertReviews() {
    IntStream.rangeClosed(1, 200).forEach(i -> {
      Long mid = (long) (Math.random() * 100) + 1;
      Long gno = (long) (Math.random() * 100) + 1;
      GroundsReviews groundsReviews = GroundsReviews.builder()
          .members(Members.builder().mid(mid).build())
          .grounds(Grounds.builder().gno(gno).build())
          .build();
      groundsReviewsRepository.save(groundsReviews);
    });
  }
}