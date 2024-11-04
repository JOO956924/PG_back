package com.example.api.service;

import com.example.api.dto.BoardsDTO;
import com.example.api.dto.BphotosDTO;
import com.example.api.dto.PageRequestDTO;
import com.example.api.dto.PageResultDTO;
import com.example.api.entity.Boards;
import com.example.api.entity.Bphotos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface BoardsService {
  Long register(BoardsDTO boardsDTO);

  PageResultDTO<BoardsDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

  BoardsDTO getBoards(Long bno);

  void modify(BoardsDTO boardsDTO);

  List<String> removeWithReviewsAndBphotos(Long bno);

  void removeUuid(String uuid);

  List<String> getTitlesByEmail(String email);

  default Map<String, Object> dtoToEntity(BoardsDTO boardsDTO) {
    Map<String, Object> entityMap = new HashMap<>();
    Boards boards = Boards.builder()
        .bno(boardsDTO.getBno())
        .email(boardsDTO.getEmail())
        .body(boardsDTO.getBody())
        .title(boardsDTO.getTitle()).build();
    entityMap.put("boards", boards);
    List<BphotosDTO> bphotosDTOList = boardsDTO.getBphotosDTOList();
    if (bphotosDTOList != null && bphotosDTOList.size() > 0) {
      List<Bphotos> bphotosList = bphotosDTOList.stream().map(
          new Function<BphotosDTO, Bphotos>() {
            @Override
            public Bphotos apply(BphotosDTO bphotosDTO) {
              Bphotos bphotos = Bphotos.builder()
                  .path(bphotosDTO.getPath())
                  .bphotosName(bphotosDTO.getBphotosName())
                  .uuid(bphotosDTO.getUuid())
                  .boards(boards)
                  .build();
              return bphotos;
            }
          }
      ).collect(Collectors.toList());
      entityMap.put("bphotosList", bphotosList);
    }
    return entityMap;
  }

  default BoardsDTO entityToDto(Boards boards, List<Bphotos> bphotosList
      , Long likes, Long reviewsCnt) {
    BoardsDTO boardsDTO = BoardsDTO.builder()
        .bno(boards.getBno())
        .email(boards.getEmail())
        .title(boards.getTitle())
        .body((boards.getBody()))
        .regDate(boards.getRegDate())
        .modDate(boards.getModDate())
        .build();
    List<BphotosDTO> bphotosDTOList = new ArrayList<>();
    if(bphotosList.toArray().length > 0 && bphotosList.toArray()[0] != null) {
      bphotosDTOList = bphotosList.stream().map(
          bphotos -> {
            BphotosDTO bphotosDTO = BphotosDTO.builder()
                .bphotosName(bphotos.getBphotosName())
                .path(bphotos.getPath())
                .uuid(bphotos.getUuid())
                .build();
            return bphotosDTO;
          }
      ).collect(Collectors.toList());
    }
    boardsDTO.setBphotosDTOList(bphotosDTOList);
    boardsDTO.setLikes(likes);
    boardsDTO.setReviewsCnt(reviewsCnt);
    return boardsDTO;
  }
}