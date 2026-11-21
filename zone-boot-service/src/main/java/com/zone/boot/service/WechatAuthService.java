package com.zone.boot.service;


import com.zone.boot.domain.api.ApiResult;
import com.zone.boot.domain.request.LoginReq;
import com.zone.boot.domain.response.LoginRes;
import jakarta.servlet.http.HttpServletRequest;

public interface WechatAuthService {
    /**
     * 微信小程序用户登录
     * @param request
     * @param httpRequest
     * @return
     */
    ApiResult<LoginRes> miniAppLogin(LoginReq request, HttpServletRequest httpRequest);

}
