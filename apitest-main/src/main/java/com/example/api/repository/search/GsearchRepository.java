package com.example.api.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GsearchRepository {
//  Feeds search1();

  Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

}
