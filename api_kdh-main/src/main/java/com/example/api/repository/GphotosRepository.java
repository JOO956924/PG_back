package com.example.api.repository;

import com.example.api.entity.Gphotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GphotosRepository extends JpaRepository<Gphotos, Long> {
  @Modifying
  @Query("delete from Gphotos g where g.grounds.gno=:gno")
  void deleteByGno(@Param("gno") long gno);

  @Modifying
  @Query("delete from Gphotos g where g.uuid=:uuid")
  void deleteByUuid(@Param("uuid")String uuid);

  @Query("select p from Gphotos p where p.grounds.gno=:gno")
  List<Gphotos> findByGno(@Param("gno") Long gno);
}
