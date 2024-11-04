package com.example.api.service;

import com.example.api.dto.BoardsDTO;
import com.example.api.dto.PageRequestDTO;
import com.example.api.dto.PageResultDTO;
import com.example.api.entity.Boards;
import com.example.api.entity.Bphotos;
import com.example.api.repository.BoardsRepository;
import com.example.api.repository.BphotosRepository;
import com.example.api.repository.ReviewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLDecoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService {
  private final BoardsRepository boardsRepository;
  private final BphotosRepository bphotosRepository;
  private final ReviewsRepository reviewsRepository;

  @Override
  public Long register(BoardsDTO boardsDTO) {
    Map<String, Object> entityMap = dtoToEntity(boardsDTO);
    Boards boards = (Boards) entityMap.get("boards");
    List<Bphotos> bphotosList =
        (List<Bphotos>) entityMap.get("bphotosList");
    boardsRepository.save(boards);
    if (bphotosList != null) {
      bphotosList.forEach(new Consumer<Bphotos>() {
        @Override
        public void accept(Bphotos bphotos) {
          bphotosRepository.save(bphotos);
        }
      });
    }
    return boards.getBno();
  }

  @Override
  public PageResultDTO<BoardsDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("bno").descending());
    Page<Object[]> result = boardsRepository.searchPage(pageRequestDTO.getType(),
        pageRequestDTO.getKeyword(),
        pageable);
    Function<Object[], BoardsDTO> fn = objects -> entityToDto(
        (Boards) objects[0],
        (List<Bphotos>) (Arrays.asList((Bphotos) objects[1])),
        (Long) objects[2],
        (Long) objects[3]
    );
    return new PageResultDTO<>(result, fn);
  }

  @Override
  public BoardsDTO getBoards(Long bno) {
    List<Object[]> result = boardsRepository.getBoardsWithAll(bno);
    Boards boards = (Boards) result.get(0)[0];
    List<Bphotos> bphotos = new ArrayList<>();
    result.forEach(objects -> bphotos.add((Bphotos) objects[1]));
    Long likes = (Long) result.get(0)[2];
    Long reviewsCnt = (Long) result.get(0)[3];

    return entityToDto(boards, bphotos, likes, reviewsCnt);
  }

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @Transactional
  @Override
  public void modify(BoardsDTO boardsDTO) {
    Optional<Boards> result = boardsRepository.findById(boardsDTO.getBno());
    if (result.isPresent()) {
      Map<String, Object> entityMap = dtoToEntity(boardsDTO);
      Boards boards = (Boards) entityMap.get("boards");
      boards.changeTitle(boardsDTO.getTitle());
      boardsRepository.save(boards);
      // bphotosList :: 수정창에서 이미지 수정할 게 있는 경우의 목록
      List<Bphotos> newBphotosList =
          (List<Bphotos>) entityMap.get("bphotosList");

      List<Bphotos> oldBphotosList =
          bphotosRepository.findByMno(boards.getBno());
      if (newBphotosList == null) {
        // 수정창에서 이미지 모두를 지웠을 때
        bphotosRepository.deleteByBno(boards.getBno());
        for (int i = 0; i < oldBphotosList.size(); i++) {
          Bphotos oldBphotos = oldBphotosList.get(i);
          String fileName = oldBphotos.getPath() + File.separator
              + oldBphotos.getUuid() + "_" + oldBphotos.getBphotosName();
          deleteFile(fileName);
        }
      } else { // newFeedsImageList에 일부 변화 발생
        newBphotosList.forEach(bphotos -> {
          boolean result1 = false;
          for (int i = 0; i < oldBphotosList.size(); i++) {
            result1 = oldBphotosList.get(i).getUuid().equals(bphotos.getUuid());
            if (result1) break;
          }
          if (!result1) bphotosRepository.save(bphotos);
        });
        oldBphotosList.forEach(oldBphotos -> {
          boolean result1 = false;
          for (int i = 0; i < newBphotosList.size(); i++) {
            result1 = newBphotosList.get(i).getUuid().equals(oldBphotos.getUuid());
            if (result1) break;
          }
          if (!result1) {
            bphotosRepository.deleteByUuid(oldBphotos.getUuid());
            String fileName = oldBphotos.getPath() + File.separator
                + oldBphotos.getUuid() + "_" + oldBphotos.getBphotosName();
            deleteFile(fileName);
          }
        });
      }
    }
  }

  private void deleteFile(String fileName) {
    // 실제 파일도 지우기
    String searchFilename = null;
    try {
      searchFilename = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + searchFilename);
      file.delete();
      new File(file.getParent(), "s_" + file.getName()).delete();
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  @Transactional
  @Override
  public List<String> removeWithReviewsAndBphotos(Long bno) {
    List<Bphotos> list = bphotosRepository.findByMno(bno);
    List<String> result = new ArrayList<>();
    list.forEach(new Consumer<Bphotos>() {
      @Override
      public void accept(Bphotos t) {
        result.add(t.getPath() + File.separator + t.getUuid() + "_" + t.getBphotosName());
      }
    });
    bphotosRepository.deleteByBno(bno);
    reviewsRepository.deleteByBno(bno);
    boardsRepository.deleteById(bno);
    return result;
  }

  @Override
  public void removeUuid(String uuid) {
    log.info("deleteImage...... uuid: " + uuid);
    bphotosRepository.deleteByUuid(uuid);
  }

  public List<String> getTitlesByEmail(String email) {
    return boardsRepository.findTitlesByEmail(email);
  }
}