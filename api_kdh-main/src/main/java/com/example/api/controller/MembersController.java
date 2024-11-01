package com.example.api.controller;

import com.example.api.dto.MembersDTO;
import com.example.api.service.MembersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/members")
@RequiredArgsConstructor
public class MembersController {
  private final MembersService membersService;

  @PostMapping(value="/join")
  public ResponseEntity<Long> register(@RequestBody MembersDTO membersDTO) {
    log.info("Request to register member: " + membersDTO);

    // 기본 권한 ROLE_USER 추가
    if (membersDTO.getRoleSet() == null) {
      membersDTO.setRoleSet(new HashSet<>());
    }
    membersDTO.getRoleSet().add("ROLE_USER");
    Long mid = membersService.registerMembers(membersDTO);

    return new ResponseEntity<>(mid, HttpStatus.OK);
  }

  @PostMapping(value="/bjoin")
  public ResponseEntity<Long> bregister(@RequestBody MembersDTO membersDTO) {
    log.info("Request to register member: " + membersDTO);

    // 기본 권한 ROLE_USER 추가
    if (membersDTO.getRoleSet() == null) {
      membersDTO.setRoleSet(new HashSet<>());
    }
    membersDTO.getRoleSet().add("ROLE_MANAGER");
    Long mid = membersService.registerMembers(membersDTO);

    return new ResponseEntity<>(mid, HttpStatus.OK);
  }


  @GetMapping(value = "/{mid}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MembersDTO> read(@PathVariable("mid") Long mid) {
    log.info("read... mid: " + mid);
    return new ResponseEntity<>(membersService.getMembers(mid), HttpStatus.OK);
  }

  @DeleteMapping(value = "/{mid}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> remove(@PathVariable("mid") Long mid) {
    log.info("delete... mid: " + mid);
    membersService.removeMembers(mid);
    return new ResponseEntity<>("removed", HttpStatus.OK);
  }

  @PutMapping(value = "/{num}", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> modify(@RequestBody MembersDTO membersDTO) {
    log.info("modify... membersDTO: " + membersDTO);
    membersService.updateMembers(membersDTO);
    return new ResponseEntity<>("modified", HttpStatus.OK);
  }

  @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MembersDTO> getMemberByEmail(@PathVariable("email") String email) {
    log.info("getMemberByEmail... email: " + email);
    return new ResponseEntity<>(membersService.getMemberByEmail(email), HttpStatus.OK);
  }

//  @PutMapping(value = "/updateCash", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<String> updateCash(@RequestBody MembersDTO membersDTO) {
//    log.info("updateCash... membersDTO: " + membersDTO);
//    membersService.updateCash(membersDTO);
//    return new ResponseEntity<>("cash updated", HttpStatus.OK);
//  }

  @PostMapping(value = "/charge", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> chargeCash(@RequestBody Map<String, Object> chargeData) {
    String email = (String) chargeData.get("email");
    int addcash = (int) chargeData.get("addcash");
    membersService.chargeCash(email, addcash);
    Map<String, String> response = Map.of("message", "cash charged");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/updateLikes", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> updateLikes(@RequestBody Map<String, Object> likeData) {
    String email = (String) likeData.get("email");
    Long gno = null;

    // gno가 String으로 전달될 경우를 고려하여 Long으로 변환
    if (likeData.get("gno") instanceof String) {
      gno = Long.parseLong((String) likeData.get("gno"));
    } else if (likeData.get("gno") instanceof Number) {
      gno = ((Number) likeData.get("gno")).longValue();
    }

    if (gno == null) {
      log.warn("gno value is missing in the request");
      return new ResponseEntity<>(Map.of("error", "gno value is missing"), HttpStatus.BAD_REQUEST);
    }

    log.info("updateLikes... email: " + email + ", gno: " + gno);

    membersService.addLikes(email, gno);
    Map<String, String> response = Map.of("message", "likes updated");

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @PostMapping(value = "/removeLike", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, String>> removeLikes(@RequestBody Map<String, Object> likeData) {
    String email = (String) likeData.get("email");
    Long gno = null;

    // gno가 String으로 전달될 경우를 고려하여 Long으로 변환
    if (likeData.get("gno") instanceof String) {
      gno = Long.parseLong((String) likeData.get("gno"));
    } else if (likeData.get("gno") instanceof Number) {
      gno = ((Number) likeData.get("gno")).longValue();
    }

    if (gno == null) {
      log.warn("gno value is missing in the request");
      return new ResponseEntity<>(Map.of("error", "gno value is missing"), HttpStatus.BAD_REQUEST);
    }

    log.info("removeLike... email: " + email + ", gno: " + gno);

    membersService.removeLike(email, gno);
    Map<String, String> response = Map.of("message", "like removed");

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


}