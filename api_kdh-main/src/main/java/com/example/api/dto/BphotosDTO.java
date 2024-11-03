package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BphotosDTO {
  private String uuid;
  private String bphotosName;
  private String path;
  public String getBphotosURL() {
    try {
      return URLEncoder.encode(path + "/" + uuid + "_" + bphotosName,"UTF-8");
    } catch (UnsupportedEncodingException e) {e.printStackTrace();}
    return "";
  }

  public String getThumbnailURL() {
    try {
      return URLEncoder.encode(path + "/s_" + uuid + "_" + bphotosName, "UTF-8");
    } catch (UnsupportedEncodingException e) {e.printStackTrace();}
    return "";
  }
}