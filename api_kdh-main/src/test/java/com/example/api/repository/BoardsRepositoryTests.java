package com.example.api.repository;

import com.example.api.entity.Boards;
import com.example.api.entity.Bphotos;
import com.example.api.entity.Gphotos;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BoardsRepositoryTests {

  @Autowired
  BoardsRepository boardsRepository;

  @Autowired
  BphotosRepository bphotosRepository;

  @Transactional
  @Commit
  @Test
  public void insertBoards() {
    IntStream.rangeClosed(1, 9).forEach(i -> {
      Boards boards = Boards.builder().title("Borads...3" + i)
          .body("body"+i)
          .email("m" + i + "@a.a")
          .build();
      boardsRepository.save(boards);
      System.out.println("----------------------------");


    });
  }

}