package com.them.orderrelay.domain.mongo.base.service;

import com.them.orderrelay.domain.mongo.base.dto.*;
import com.them.orderrelay.framework.base.dto.ResCode;
import com.them.orderrelay.framework.base.dto.ResDto;
import com.them.orderrelay.framework.exception.UserException;
import com.them.orderrelay.framework.jwt.JwtTokenProvider;
import com.them.orderrelay.framework.util.Global;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@Primary
public class LoginServiceImpl implements LoginService {
    private final UserService MongoUserService;
    private final MongoTemplate mongoTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginServiceImpl(UserService MongoUserService, MongoTemplate mongoTemplate, JwtTokenProvider jwtTokenProvider) {
        this.MongoUserService = MongoUserService;
        this.mongoTemplate = mongoTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public LoginDto login(UserDtoReq reqParam) throws Exception {

        this.checkLogin(reqParam);
        List<UserDto> list = this.getMongoUserList(reqParam);

        this.checkLogin(reqParam, list);

        return getLoginResponse(reqParam, list);

    }

    private LoginDto getLoginResponse(UserDtoReq reqParam, List<UserDto> list) throws Exception {
        LoginDto rtn = this.setLoginDto(list.get(0));
        this.setTokenExpireDate(rtn);
        rtn.setUserPw("****");
        rtn.setStoreList(this.getMongoStoreList(reqParam));
        return rtn;
    }

    private void checkLogin(UserDtoReq reqParam, List<UserDto> list) throws Exception {
        if (list.size() != 1) throw new Exception(Global.getMessageInfo().getMessage("cm0001")); /*사용자정보가 없습니다.*/

        if (!passwordEquals(reqParam.getUserPw(), list.get(0).getUserPw()))
            throw new Exception(Global.getMessageInfo().getMessage("cm0002")); /*패스워드가 맞지 않습니다.*/
    }

    @Override
    public LoginDto testLogin(UserDtoReq reqParam) throws Exception {

        this.checkLogin(reqParam);
        List<UserDto> list = this.getMongoUserList(reqParam);

        checkLogin(reqParam, list);

        LoginDto rtn = this.setTestLoginDto(list.get(0), reqParam);

        this.setTokenExpireDate(rtn, reqParam.getTokenValidTimes(), reqParam.getRefreshTokenValidTimes());
        rtn.setUserPw("****");

        return rtn;
    }

    private LoginDto setTestLoginDto(UserDto userDto, UserDtoReq reqParam) throws Exception {

        userDto.setRefreshToken(jwtTokenProvider.createToken(userDto.getUserId() + jwtTokenProvider.getSecretKey(),
                reqParam.getTokenValidTimes()));
        LoginDto rtn = setToken(userDto, reqParam);
        rtn.setStoreList(this.getMongoStoreList(reqParam));

        MongoUserService.update(userDto);
        return rtn;
    }

    private LoginDto setToken(UserDto userDto, UserDtoReq reqParam) throws Exception {
        LoginDto rtn = Global.getDataInfo().changeToOtherClass(userDto, LoginDto.class);
        rtn.setToken(jwtTokenProvider.createToken(rtn.getUserId(), reqParam.getTokenValidTimes()));
        rtn.setRefreshToken(userDto.getRefreshToken());
        return rtn;
    }


    private boolean passwordEquals(String userPw, String resUserPw) {
        return Global.getSecurityInfo().encryptSHA256(userPw).toUpperCase(Locale.ROOT)
                .equals(resUserPw.toUpperCase(Locale.ROOT));
    }

    @Override
    public ResDto logout(UserDtoReq reqParam) throws UserException {
        return null;
    }

    @Override
    public TokenDto getToken(UserDtoReq reqParam) throws Exception {
        this.checkLogin(reqParam);
        List<UserDto> list = this.getMongoUserList(reqParam);

        checkLogin(reqParam, list);

        TokenDto rtn = this.setLoginDto(list.get(0));

        this.setTokenExpireDate(rtn, reqParam.getTokenValidTimes(), reqParam.getRefreshTokenValidTimes());
        rtn.setUserPw("****");

        return rtn;
    }

    @Override
    public TokenDto getRefreshTokenNotStore(RefreshTokenDto reqParam) throws Exception {
        Claims claims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey()).parseClaimsJws(reqParam.getToken()).getBody();
        Claims refreshClaims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey()).parseClaimsJws(reqParam.getRefreshToken()).getBody();

        String userId = claims.get("sub").toString();

        this.checkToken(reqParam, claims, refreshClaims);

        UserDtoReq userDtoReq = new UserDtoReq();
        userDtoReq.setUserId(userId);

        List<UserDto> list = this.getMongoUserList(userDtoReq);
        this.checkUserId(list);

        TokenDto rtn = setLoginDto(list.get(0));

        rtn.setUserPw("****");
        this.setTokenExpireDate(rtn, reqParam.getTokenValidTimes(), reqParam.getRefreshTokenValidTimes());
        return rtn;
    }

    private void checkToken(RefreshTokenDto reqParam, Claims claims, Claims refreshClaims) throws UserException {
        this.checkRefreshTokenValidateId(claims.get("sub").toString(), refreshClaims.get("sub").toString());

        this.checkValidToken(reqParam.getRefreshToken(), "(리플레시토큰)");
    }

    @Override
    public LoginDto getRefreshToken(RefreshTokenDto reqParam) throws Exception {

        Claims claims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey()).parseClaimsJws(reqParam.getToken()).getBody();
        Claims refreshClaims = Jwts.parser().setSigningKey(jwtTokenProvider.getSecretKey()).parseClaimsJws(reqParam.getRefreshToken()).getBody();

        String userId = claims.get("sub").toString();
        checkToken(reqParam, claims, refreshClaims);

        return getMongoLoginDto(userId);
    }

    private LoginDto getMongoLoginDto(String userId) throws Exception {
        UserDtoReq userDtoReq = new UserDtoReq();
        userDtoReq.setUserId(userId);

        List<UserDto> list = this.getMongoUserList(userDtoReq);
        this.checkUserId(list);

        LoginDto rtn = setLoginDto(list.get(0));
        rtn.setStoreList(this.getMongoStoreList(userDtoReq));
        rtn.setUserPw("****");
        this.setTokenExpireDate(rtn);
        return rtn;
    }

    private List<LoginStoreDto> getMongoStoreList(UserDtoReq reqParam) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(reqParam.getUserId()));
        query.addCriteria(Criteria.where("isUse").is(reqParam.getIsUse()));
        List<LoginStoreDto> list = mongoTemplate.find(query, LoginStoreDto.class);
        return list;
    }

    private void checkValidToken(String token, String title) throws UserException {
        ResDto resDto = jwtTokenProvider.validateToken(token);
        if (!resDto.getResCd().equals(ResCode.ok.toString())) throw new UserException(resDto.getResMsg() + title);
    }

    private void checkLogin(UserDtoReq reqParam) throws UserException {
        if (StringUtils.isEmpty(reqParam.getUserId())) throw new UserException("아이디를 입력하세요");
        if (StringUtils.isEmpty(reqParam.getUserPw())) throw new UserException("패스워드를 입력하세요");
    }

    private void checkUserId(List<UserDto> list) throws UserException {
        if (list.size() != 1) throw new UserException("토큰정보가 유효하지 않습니다.");
    }

    private LoginDto setLoginDto(UserDto userDto) throws Exception {
        userDto.setRefreshToken(jwtTokenProvider.createRefreshToken(userDto.getUserId() + jwtTokenProvider.getSecretKey()));
        LoginDto rtn = Global.getDataInfo().changeToOtherClass(userDto, LoginDto.class);
        rtn.setToken(jwtTokenProvider.createToken(rtn.getUserId()));
        rtn.setRefreshToken(userDto.getRefreshToken());
        MongoUserService.update(userDto);
        return rtn;
    }

    private void setTokenExpireDate(TokenDto data) {
        data.setTokenExpireDate(jwtTokenProvider.getTokenExpireDate(jwtTokenProvider.getTokenValidMs()));
        data.setRefreshTokenExpireDate(jwtTokenProvider.getTokenExpireDate(jwtTokenProvider.getRefreshTokenValidMs()));
    }

    private void setTokenExpireDate(TokenDto data, long tokenValidTimes, long refreshTokenValidTimes) {

        data.setTokenExpireDate(jwtTokenProvider.getTokenExpireDate(tokenValidTimes));
        data.setRefreshTokenExpireDate(jwtTokenProvider.getTokenExpireDate(refreshTokenValidTimes));
    }


    private void checkRefreshTokenValidateDate(RefreshTokenDto reqParam) throws UserException {
        if (!jwtTokenProvider.isValidateToken(reqParam.getRefreshToken())) {
            throw new UserException("리플레시토큰이 유효기간이 지났습니다.");
        }
    }

    private void checkRefreshTokenValidateId(String tokenId, String refreshTokenId) throws UserException {
        if (!refreshTokenId.contains(tokenId)) {
            throw new UserException("리플레시토큰이 유효하지 않습니다.");
        }
    }

    private List<UserDto> getMongoUserList(UserDtoReq req) throws UserException {
        Query query = this.setParam(req);
        return mongoTemplate.find(query, UserDto.class);
    }

    private Query setParam(UserDtoReq req) {
        Query query = new Query();
        if (!StringUtils.isBlank(req.getUserId()))
            query.addCriteria(Criteria.where("userId").is(req.getUserId()));
        if (!StringUtils.isBlank(req.getId()))
            query.addCriteria(Criteria.where("id").is(req.getId()));
        if (!StringUtils.isBlank(req.getRefreshToken()))
            query.addCriteria(Criteria.where("refreshToken").is(req.getRefreshToken()));
        return query;
    }
}
