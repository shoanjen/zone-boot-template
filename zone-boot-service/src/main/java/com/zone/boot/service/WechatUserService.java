package com.zone.boot.service;

import com.zone.boot.domain.api.ApiResult;
import com.zone.boot.domain.entity.WechatUser;
import com.zone.boot.domain.request.UpdateUserNicknameReq;
import com.baomidou.mybatisplus.extension.service.IService;


public interface WechatUserService extends IService<WechatUser> {
    /**
     * 通过openid查询用户
     * @param openid
     * @return
     */
    WechatUser findByOpenid(String openid);

    /**
     * 通过ucid查询用户
     * @param userId
     * @return
     */
     WechatUser findById(String userId);

    /**
     * 用户注册
     * @param openid
     * @param sessionKey
     * @param nickname
     * @param avatarUrl
     * @return
     */
     WechatUser createUser(String openid, String sessionKey, String nickname, String avatarUrl);

    /**
     * 更新用户
     * @param userId
     * @param nickname
     * @param avatarUrl
     * @param sessionKey
     * @return
     */
     WechatUser updateUser(String userId, String nickname, String avatarUrl, String sessionKey);

    /**
     * 更新用户登录信息
     * @param userId
     * @param loginIp
     * @param userAgent
     */
     void updateLoginInfo(String userId, String loginIp, String userAgent);

    /**
     * 更新用户昵称
     * @return 更新后的用户信息
     */
     ApiResult<Boolean> updateUserNickname(UpdateUserNicknameReq request);
}
