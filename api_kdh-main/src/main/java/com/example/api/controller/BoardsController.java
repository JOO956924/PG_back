package com.example.api.controller;

import com.example.api.dto.BoardsDTO;
import com.example.api.dto.PageRequestDTO;
import com.example.api.service.BoardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardsController {
  private final BoardsService boardsService;

  @Value("${com.example.upload.path}")
  private String uploadPath;

  private void typeKeywordInit(PageRequestDTO pageRequestDTO) {
    if (pageRequestDTO.getType() == null || pageRequestDTO.getType().equals("null")) {
      pageRequestDTO.setType("");
    }
    if (pageRequestDTO.getKeyword() == null || pageRequestDTO.getKeyword().equals("null")) {
      pageRequestDTO.setKeyword("");
    }
  }

  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> list(PageRequestDTO pageRequestDTO) {
    System.out.println("pageRequestDTO: " + pageRequestDTO);
    Map<String, Object> result = new HashMap<>();
    result.put("pageResultDTO", boardsService.getList(pageRequestDTO));
    result.put("pageRequestDTO", pageRequestDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
  public ResponseEntity<Long> registerBoards(@RequestBody BoardsDTO boardsDTO) {
    System.out.println(" 컨트롤러 수신 데이터:"+boardsDTO);
    Long bno = boardsService.register(boardsDTO);
    if (boardsDTO.getBody() == null){
      System.out.println("컨트롤러 : body 필드가 null입니다.");
    }
    return new ResponseEntity<>(bno, HttpStatus.OK);
  }

  @GetMapping(value = {"/boards/{bno}","/read/{bno}", "/modify/{bno}"}, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, BoardsDTO>> getBoards(
      @PathVariable("bno") Long bno) {
    BoardsDTO boardsDTO = boardsService.getBoards(bno);
    //typeKeywordInit(pageRequestDTO);
    Map<String, BoardsDTO> result = new HashMap<>();
    result.put("boardsDTO", boardsDTO);
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping(value = "/modify", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> modify(@RequestBody BoardsDTO dto)
  {
    log.info("modify post... dto: " + dto);
    boardsService.modify(dto);
    //typeKeywordInit(pageRequestDTO);
    Map<String, String> result = new HashMap<>();
    result.put("msg", dto.getBno() + " 수정");
    result.put("bno", dto.getBno() + "");
    //result.put("page", pageRequestDTO.getPage() + "");
    //result.put("type", pageRequestDTO.getType());
    //result.put("keyword", pageRequestDTO.getKeyword());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping(value = "/remove/{bno}", produces = MediaType.APPLICATION_JSON_VALUE )
  public ResponseEntity<Map<String, String>> remove(
      @PathVariable Long bno, @RequestBody PageRequestDTO pageRequestDTO) {

    Map<String, String> result = new HashMap<>();
    List<String> bphotoList = boardsService.removeWithReviewsAndBphotos(bno);
    bphotoList.forEach(fileName -> {
      try {
        log.info("removeFile............" + fileName);
        String srcFileName = URLDecoder.decode(fileName, "UTF-8");
        File file = new File(uploadPath + File.separator + srcFileName);
        file.delete();
        File thumb = new File(file.getParent(), "s_" + file.getName());
        thumb.delete();
      } catch (Exception e) {
        log.info("remove file : " + e.getMessage());
      }
    });
    if (boardsService.getList(pageRequestDTO).getDtoList().size() == 0 && pageRequestDTO.getPage() != 1) {
      pageRequestDTO.setPage(pageRequestDTO.getPage() - 1);
    }
    typeKeywordInit(pageRequestDTO);
    result.put("msg", bno + " 삭제");
    result.put("page", pageRequestDTO.getPage() + "");
    result.put("type", pageRequestDTO.getType() + "");
    result.put("keyword", pageRequestDTO.getKeyword() + "");
    return new ResponseEntity<>(result, HttpStatus.OK);
  }
}