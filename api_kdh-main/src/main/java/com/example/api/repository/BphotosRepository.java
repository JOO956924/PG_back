package com.example.api.repository;

import com.example.api.entity.Bphotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BphotosRepository extends JpaRepository<Bphotos, Long> {
  @Modifying
  @Query("delete from Bphotos p where p.boards.bno=:bno")
  void deleteByBno(@Param("bno") long bno);

  @Modifying
  @Query("delete from Bphotos p where p.uuid=:uuid")
  void deleteByUuid(@Param("uuid")String uuid);

  @Query("select p from Bphotos p where p.boards.bno=:bno")
  List<Bphotos> findByMno(@Param("bno") Long bno);
}
