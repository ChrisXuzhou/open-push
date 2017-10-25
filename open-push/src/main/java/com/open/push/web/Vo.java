package com.open.push.web;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
class Vo {

  private String message = "Success";

  private HashMap<String, Object> info = new HashMap<>();

  public void put(String key, Object value) {
    info.put(key, value);
  }

}
