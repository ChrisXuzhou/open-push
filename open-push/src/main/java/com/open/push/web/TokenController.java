package com.open.push.web;

import com.open.push.transfer.token.TokenRefreshRequest;
import com.open.push.transfer.token.TokenRefreshService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/token/")
public class TokenController {

  private TokenRefreshService tokenRefreshService;

  public TokenController(TokenRefreshService tokenRefreshService) {
    this.tokenRefreshService = tokenRefreshService;
  }

  @RequestMapping(value = "/refresh", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public Vo openApiRefreshToken(@RequestBody @Valid TokenRefreshRequest request) {
    log.debug("Request {}", ReflectionToStringBuilder.toString(request));

    tokenRefreshService.refreshToken(request);
    return new Vo();
  }

}
