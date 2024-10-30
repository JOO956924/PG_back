package com.example.api.repository;

import com.example.api.entity.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroundsReviewsRepository extends JpaRepository<GroundsReviews, Long> {

  @EntityGraph(attributePaths = {"members"},
      type = EntityGraph.EntityGraphType.FETCH)
  List<GroundsReviews> findByGrounds(Grounds grounds);

  // 쿼리메서드나, deleteById등은 한건씩 진행을 한다.
  // @Query를 사용해서 delete, update를 할 경우 Bulk연산을 함
  // 그래서 트랜잭션을 복수개 할 것을 한번에 처리하기 때문에
  // 복수의 트랜잭션을 한번에 처리하기 위해 @Modifying
  @Modifying
  @Query("delete from GroundsReviews r where r.members = :members")
  void deleteByMembers(Members members);

  @Modifying //update, delete 할 때 항상 표기
  @Query("delete from GroundsReviews r where r.grounds.gno=:gno")
  void deleteByGno(@Param("gno") Long gno);
}
