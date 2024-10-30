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
public class GphotosDTO {
  private String uuid;
  private String gphotosName;
  private String path;
  public String getGphotosURL() {
    try {
      return URLEncoder.encode(path + "/" + uuid + "_" + gphotosName,"UTF-8");
    } catch (UnsupportedEncodingException e) {e.printStackTrace();}
    return "";
  }

  public String getThumbnailURL() {
    try {
      return URLEncoder.encode(path + "/s_" + uuid + "_" + gphotosName, "UTF-8");
    } catch (UnsupportedEncodingException e) {e.printStackTrace();}
    return "";
  }
}
