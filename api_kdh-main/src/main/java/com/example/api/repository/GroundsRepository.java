package com.example.api.repository;

import com.example.api.entity.Boards;
import com.example.api.entity.Grounds;
import com.example.api.entity.Members;
import com.example.api.repository.search.GsearchRepository;
import com.example.api.repository.search.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroundsRepository extends JpaRepository<Grounds, Long>, GsearchRepository {

//  select f.fno, likes(coalesce(r.likes,0)), count(r.reviewsnum)
//  from feeds f left outer join reviews r on f.fno = r.feeds_fno
//  group by f.fno;

  // 경기 게시판에 대한 리뷰의 현재 신청한 인원 수를 출력
  @Query("select g, count(g.nowpeople), count(distinct r) " +
      "from Grounds g left outer join GroundsReviews r on r.grounds=g group by g ")
  Page<Object[]> getListPage(Pageable pageable);

  // 아래와 같은 경우 mi를 찾기 위해서 reviews 카운트 만큼 반복횟수도 늘어나는 문제점
  // mi의 pnum이 가장 낮은 이미지 번호가 출력된다.
  // 영화와 영화이미지,리뷰의 평점과 댓글 갯수 출력
//  @Query("select g, p, count(r.likes), count(distinct r) from Grounds g " +
//      "left outer join Gphotos p on p.grounds = g " +
//      "left outer join GroundsReviews     r  on r.grounds  = g group by g ")
//  Page<Object[]> getListPageImg(Pageable pageable);

  // spring 3.x에서는 실행 안됨.
  @Query("select g,max(p),count(g.nowpeople),count(distinct r) from Grounds g  " +
      "left outer join Gphotos p on p.grounds = g " +
      "left outer join GroundsReviews     r  on r.grounds  = g group by g ")
  Page<Object[]> getListPageMaxImg(Pageable pageable);

  // Native Query = SQL
  @Query(value = "select g.gno, p.gpnum, p.gphotos_name, " +
      "r.members,count(g.nowpeople),r.maxpelple,r.reservation,r.groundsTime " +
      "from db7.gphotos p left outer join db7.grounds g on g.gno=p.grounds_gno " +
      "left outer join db7.GroundsReviews r on g.gno=r.grounds_gno " +
      "where p.gpnum = " +
      "(select max(gpnum) from db7.gphotos p2 where p2.grounds_gno=g.gno) " +
      "group by g.gno ", nativeQuery = true)
  Page<Object[]> getListPageImgNative(Pageable pageable);

  // JPQL
  @Query("select g, p, count(g.nowpeople), count(distinct r) from Grounds g " +
      "left outer join Gphotos p on p.grounds = g " +
      "left outer join GroundsReviews     r  on r.grounds  = g " +
      "where gpnum = (select max(p2.gpnum) from Gphotos p2 where p2.grounds=g) " +
      "group by g ")
  Page<Object[]> getListPageImgJPQL(Pageable pageable);

  @Query("select grounds, max(p.gpnum) from Gphotos p group by grounds")
  Page<Object[]> getMaxQuery(Pageable pageable);

  @Query("select g, p, count(g.nowpeople), count(r) " +
      "from Grounds g left outer join Gphotos p on p.grounds=g " +
      "left outer join GroundsReviews r on r.grounds = g " +
      "where g.gno = :gno group by p ")
  List<Object[]> getGroundsWithAll(Long gno); //특정 게시글 조회

  @Query("select g.gno from Grounds g where g.gtitle = :gtitle and g.groundstime = :groundstime")
  Optional<Long> findGnoByGtitleAndGroundstime(@Param("gtitle") String gtitle, @Param("groundstime") String groundstime);



}

