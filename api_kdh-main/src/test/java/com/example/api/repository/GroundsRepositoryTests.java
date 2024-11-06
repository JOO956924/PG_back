package com.example.api.repository;

import com.example.api.entity.Gphotos;
import com.example.api.entity.Grounds;
import com.example.api.entity.Members;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GroundsRepositoryTests {
  @Autowired
  GroundsRepository groundsRepository;

  @Autowired
  GphotosRepository gphotosRepository;

  @Transactional
  @Commit
  @Test
  public void insertGrounds() {
    IntStream.rangeClosed(1, 9).forEach(i -> {
      Grounds grounds = Grounds.builder().gtitle("Grounds...5" + i)
          .groundstime("0"+i+":"+"00")
          .email("m" + i + "@a.a")
          .price(10000)
          .location("부산")
          .day(20241110+i)
          .maxpeople(5)
          .build();
      groundsRepository.save(grounds);
      System.out.println("----------------------------");
      int cnt = (int) (Math.random() * 5) + 1;
      for (int j = 0; j < cnt; j++) {
        Gphotos gphotos = Gphotos.builder()
            .uuid(UUID.randomUUID().toString())
            .grounds(grounds)
            .gphotosName("test" + j + ".jpg")
            .build();
        gphotosRepository.save(gphotos);
      }
    });
  }

}