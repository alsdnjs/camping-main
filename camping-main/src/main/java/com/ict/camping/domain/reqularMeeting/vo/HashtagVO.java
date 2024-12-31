package com.ict.camping.domain.reqularMeeting.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HashtagVO {
  private String hashtag_idx, name;

  public HashtagVO trim() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'trim'");
  }
  
}
