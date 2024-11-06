package com.example.api.service;

import com.example.api.dto.BoardsDTO;
import com.example.api.dto.PageRequestDTO;
import com.example.api.dto.PageResultDTO;
import com.example.api.entity.Boards;
import com.example.api.entity.Bphotos;
import com.example.api.entity.Members;
import com.example.api.repository.BoardsRepository;
import com.example.api.repository.BphotosRepository;
import com.example.api.repository.MembersRepository;
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
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService {
  private final BoardsRepository boardsRepository;
  private final BphotosRepository bphotosRepository;
  private final ReviewsRepository reviewsRepository;
  private final MembersRepository membersRepository;

  @Override
  public Long register(BoardsDTO boardsDTO) {
    Map<String, Object> entityMap = dtoToEntity(boardsDTO);
    Boards boards = (Boards) entityMap.get("boards");
    List<Bphotos> bphotosList = (List<Bphotos>) entityMap.get("bphotosList");

    // Boards 엔티티 저장
    boardsRepository.save(boards);

    // Members 엔티티에 bno와 title을 조합하여 기존 bnotitle에 추가 저장
    Optional<Members> optionalMember = membersRepository.findByEmail(boards.getEmail());
    if (optionalMember.isPresent()) {
      Members member = optionalMember.get();
      String newBnoTitle = boards.getBno() + "-" + boards.getTitle(); // 새로 추가할 bno-title 값

      // 기존 bnotitle 값에 새 bno-title 추가
      String existingBnoTitle = member.getBnotitle();
      if (existingBnoTitle != null && !existingBnoTitle.isEmpty()) {
        member.setBnotitle(existingBnoTitle + "," + newBnoTitle); // 기존 값과 새로운 값 결합
      } else {
        member.setBnotitle(newBnoTitle); // 기존 값이 없을 경우 새 값만 저장
      }
      membersRepository.save(member);
    }

    // Bphotos 리스트 저장
    if (bphotosList != null) {
      bphotosList.forEach(bphotos -> bphotosRepository.save(bphotos));
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

      // Boards 엔티티의 title을 변경
      boards.changeTitle(boardsDTO.getTitle());
      boardsRepository.save(boards);

      // Members 엔티티의 bnotitle 필드 업데이트 로직 추가
      Optional<Members> optionalMember = membersRepository.findByEmail(boards.getEmail());
      if (optionalMember.isPresent()) {
        Members member = optionalMember.get();

        // 기존의 bnotitle 값에서 수정된 title 반영
        String oldBnoTitle = boards.getBno() + "-";
        String newBnoTitle = oldBnoTitle + boards.getTitle();  // 새로 변경된 bno-title 값

        String updatedBnoTitle = Arrays.stream(member.getBnotitle().split(","))
            .map(bnoTitle -> bnoTitle.startsWith(oldBnoTitle) ? newBnoTitle : bnoTitle)
            .collect(Collectors.joining(","));

        member.setBnotitle(updatedBnoTitle);
        membersRepository.save(member);
      }

      // bphotosList :: 수정창에서 이미지 수정할 목록
      List<Bphotos> newBphotosList = (List<Bphotos>) entityMap.get("bphotosList");

      // 기존 Bphotos 리스트
      List<Bphotos> oldBphotosList = bphotosRepository.findByMno(boards.getBno());

      if (newBphotosList == null) {
        // 이미지가 모두 삭제된 경우
        bphotosRepository.deleteByBno(boards.getBno());
        for (Bphotos oldBphotos : oldBphotosList) {
          String fileName = oldBphotos.getPath() + File.separator
              + oldBphotos.getUuid() + "_" + oldBphotos.getBphotosName();
          deleteFile(fileName);
        }
      } else {
        // 이미지 리스트에서 변경사항 처리
        newBphotosList.forEach(bphotos -> {
          boolean exists = oldBphotosList.stream()
              .anyMatch(oldBphoto -> oldBphoto.getUuid().equals(bphotos.getUuid()));
          if (!exists) {
            bphotosRepository.save(bphotos);
          }
        });
        oldBphotosList.forEach(oldBphotos -> {
          boolean exists = newBphotosList.stream()
              .anyMatch(newBphoto -> newBphoto.getUuid().equals(oldBphotos.getUuid()));
          if (!exists) {
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