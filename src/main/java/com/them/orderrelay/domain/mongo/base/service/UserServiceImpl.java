package com.them.orderrelay.domain.mongo.base.service;

import com.them.orderrelay.domain.mongo.base.MessageConstants;
import com.them.orderrelay.domain.mongo.base.MongoConstants;
import com.them.orderrelay.domain.mongo.base.dto.UserDto;
import com.them.orderrelay.domain.mongo.base.dto.UserDtoReq;
import com.them.orderrelay.framework.exception.UserException;
import com.them.orderrelay.framework.util.Global;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
public class UserServiceImpl implements UserService {

    private final MongoTemplate mongoTemplate;

    public UserServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<UserDto> getList(UserDtoReq req) throws UserException {
        return this.getUserList(req);
    }

    @Override
    public List<UserDto> getUserList(UserDtoReq req) throws UserException {
        Query query = this.setCondition(req);
        List<UserDto> list =  mongoTemplate.find(query, UserDto.class);
        if(!StringUtils.isBlank(req.getUserPw()))
        {
            if(!(list.size() == 1 && list.get(0).getUserPw().equals(Global.getSecurityInfo().encryptSHA256(req.getUserPw()))))
            {
                throw new UserException("패스워드가 맞지 않습니다.");
            }
        }
        return list;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public UserDto getData(UserDtoReq req) throws UserException {
        List<UserDto> list = this.getUserList(req);
        if(list.size() == 0) throw new UserException("sm0002", Global.getMessageInfo().getMessage("sm0002"));
        if(list.size() > 1) throw new UserException("sm0003", Global.getMessageInfo().getMessage("sm0003"));

        return list.get(0);
    }

    private Query setCondition(UserDtoReq req)
    {
        Query query = new Query();
        if(!StringUtils.isBlank(req.getUserId()))
            query.addCriteria(Criteria.where("userId").is(req.getUserId()));
        if(!StringUtils.isBlank(req.getId()))
            query.addCriteria(Criteria.where("id").is(req.getId()));
        if(!StringUtils.isBlank(req.getRefreshToken()))
            query.addCriteria(Criteria.where("refreshToken").is(req.getRefreshToken()));
        return query;
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String insert(UserDto data) throws Exception {
        data.setUserPw(Global.getSecurityInfo().encryptSHA256(data.getUserPw()));
        UserDto res = mongoTemplate.insert(data);
        return MessageConstants.insertOk;
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String update(UserDto data) throws Exception {

        Query query = new Query();
        if(!StringUtils.isBlank(data.getUserId()))
            query.addCriteria(Criteria.where("userId").is(data.getUserId()));

        Update update = new Update();
        update.set("userNm", data.getUserNm());
        if(!StringUtils.isBlank(data.getRefreshToken()))
            update.set("refreshToken", data.getRefreshToken());
        update.set("modId", data.getModId());
        update.set("modDtm", data.getModDtm());

        mongoTemplate.updateMulti(query, update, UserDto.class);

        return MessageConstants.updateOk;
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String delete(UserDtoReq req) throws Exception {

        Query query = new Query();
        if(!StringUtils.isBlank(req.getId()))
            query.addCriteria(Criteria.where("_id").is(req.getId()));
        if(!StringUtils.isBlank(req.getUserId()))
            query.addCriteria(Criteria.where("userId").is(req.getUserId()));
        mongoTemplate.remove(query, UserDto.class);

        return MessageConstants.deleteOk;
    }
}
