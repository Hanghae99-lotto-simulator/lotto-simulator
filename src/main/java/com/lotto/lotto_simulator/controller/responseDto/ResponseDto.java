package com.lotto.lotto_simulator.controller.responseDto;

import com.lotto.lotto_simulator.exception.CustomResponseBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
  private boolean success;
  private T data;
  //  private Error error;
  private CustomResponseBody error;

  public static <T> ResponseDto<T> success(T data) {
    return new ResponseDto<>(true, data, null);
  }

//  public static <T> ResponseDto<T> fail(String code,String message) {
//    return new ResponseDto<>(false, null, new Error(code, message));
//  }

  public static <T> ResponseDto<T> fail(CustomResponseBody customResponseBody) {
    return new ResponseDto<T>(false, null, customResponseBody);
  }

  @Getter
  @AllArgsConstructor
  static class Error {
    private String code;
    private String message;
  }

}


