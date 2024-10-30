package com.example.api.repository;

import com.example.api.entity.Boards;
import com.example.api.repository.search.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardsRepository extends JpaRepository<Boards, Long>, SearchRepository {

//  select f.fno, likes(coalesce(r.likes,0)), count(r.reviewsnum)
//  from feeds f left outer join reviews r on f.fno = r.feeds_fno
//  group by f.fno;

  // 자유 게시판에 대한 댓글의 평점과 댓글 갯수를 출력
  @Query("select b, count(r.likes), count(distinct r) " +
      "from Boards b left outer join Reviews r on r.boards=b group by b ")
  Page<Object[]> getListPage(Pageable pageable);

  // 아래와 같은 경우 mi를 찾기 위해서 reviews 카운트 만큼 반복횟수도 늘어나는 문제점
  // mi의 pnum이 가장 낮은 이미지 번호가 출력된다.
  // 영화와 영화이미지,리뷰의 평점과 댓글 갯수 출력
  @Query("select b, p, count(r.likes), count(distinct r) from Boards b " +
      "left outer join Bphotos p on p.boards = b " +
      "left outer join Reviews     r  on r.boards  = b group by b ")
  Page<Object[]> getListPageImg(Pageable pageable);

  // spring 3.x에서는 실행 안됨.
  @Query("select b,max(p),count(r.likes),count(distinct r) from Boards b " +
      "left outer join Bphotos p on p.boards = b " +
      "left outer join Reviews     r  on r.boards  = b group by b ")
  Page<Object[]> getListPageMaxImg(Pageable pageable);

  // Native Query = SQL
  @Query(value = "select b.bno, p.pnum, p.bphotos_name, " +
      "count(r.likes), count(r.reviewsnum) " +
      "from db7.bphotos p left outer join db7.boards b on b.bno=p.boards_bno " +
      "left outer join db7.reviews r on b.bno=r.boards_bno " +
      "where p.pnum = " +
      "(select max(pnum) from db7.bphotos p2 where p2.boards_bno=b.bno) " +
      "group by b.bno ", nativeQuery = true)
  Page<Object[]> getListPageImgNative(Pageable pageable);

  // JPQL
  @Query("select b, p, count(r.likes), count(distinct r) from Boards b " +
      "left outer join Bphotos p on p.boards = b " +
      "left outer join Reviews     r  on r.boards  = b " +
      "where pnum = (select max(p2.pnum) from Bphotos p2 where p2.boards=b) " +
      "group by b ")
  Page<Object[]> getListPageImgJPQL(Pageable pageable);

  @Query("select boards, max(p.pnum) from Bphotos p group by boards")
  Page<Object[]> getMaxQuery(Pageable pageable);

  @Query("select b, p, count(r.likes), count(r) " +
      "from Boards b left outer join Bphotos p on p.boards=b " +
      "left outer join Reviews r on r.boards = b " +
      "where b.bno = :bno group by p ")
  List<Object[]> getBoardsWithAll(Long bno); //특정 게시글 조회

}

/*
select p.feeds_fno,pnum from db7.feeds_image p
where p.pnum =
	(select max(mi2.pnum) from db7.feeds_image mi2 where mi2.feeds_fno=p.feeds_fno);
-- 1) 2개의 테이블을 단순 조인, pnum은 먼저 등록된 값으로 나옴
select fno, pnum from db7.feeds_image p, db7.feeds m
where m.fno=p.feeds_fno
group by m.fno order by m.fno desc;

-- 2) pnum은 최근값 출력, img_name은 개별속성이라서 정확히 출력 안됨.
select fno, max(pnum), img_name from db7.feeds_image p, db7.feeds m
where m.fno=p.feeds_fno
group by m.fno order by m.fno desc;

-- 3) 조건절에서 처리하여 pnum, p.img_name들도 불러 올 수 있음
select fno, pnum, p.img_name from db7.feeds_image p, db7.feeds m
where m.fno=p.feeds_fno
and p.pnum = (select max(pnum) from db7.feeds_image mi2 where mi2.feeds_fno=m.fno)
group by m.fno order by m.fno desc;

-- 4) 평점, 댓글 까지 불러옴 하지만, reviews 카운트가 0이면 출력 안됨
select fno, pnum, p.img_name, likes(coalesce(r.likes, 0)), count(coalesce(r.reviewsnum))
from db7.feeds_image p,db7.feeds m,db7.reviews r
where m.fno=p.feeds_fno and m.fno = r.feeds_fno
and p.pnum = (select max(pnum) from db7.feeds_image mi2 where mi2.feeds_fno=m.fno)
group by m.fno order by m.fno desc;

-- 5) 그래서, left outer join을 사용
select m.fno, p.pnum, p.img_name, likes(coalesce(r.likes, 0)), count(r.reviewsnum)
from db7.feeds_image p left outer join db7.feeds m on m.fno=p.feeds_fno
left outer join db7.reviews r on m.fno=r.feeds_fno
where p.pnum = (select max(pnum) from db7.feeds_image mi2 where mi2.feeds_fno=m.fno)
group by m.fno order by m.fno desc;

-- 6) 조건절을 테이블에서도 처리할 수 있음.
select m.fno,m.title, p.pnum, likes(coalesce(r.likes, 0)), count(r.reviewsnum)
from db7.feeds m left outer join
    (select mi2.feeds_fno,mi2.pnum from db7.feeds_image mi2
        where mi2.pnum = (select max(pnum) from db7.feeds_image mi3 where mi2.feeds_fno=mi3.feeds_fno)) as p
on m.fno = p.feeds_fno
left outer join db7.reviews r on r.feeds_fno = m.fno
group by m.fno order by m.fno desc;

*/