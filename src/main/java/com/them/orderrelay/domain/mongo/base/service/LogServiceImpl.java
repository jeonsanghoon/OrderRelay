package com.them.orderrelay.domain.mongo.base.service;


import com.them.orderrelay.domain.mongo.base.MessageConstants;
import com.them.orderrelay.domain.mongo.base.MongoConstants;
import com.them.orderrelay.domain.mongo.base.dto.LogDto;
import com.them.orderrelay.domain.mongo.base.dto.LogDtoReq;
import com.them.orderrelay.framework.exception.ExceptionConstants;
import com.them.orderrelay.framework.exception.UserException;
import com.them.orderrelay.framework.util.Global;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class LogServiceImpl implements LogService {

    private final MongoTemplate mongoTemplate;

    public LogServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<LogDto> getList(LogDtoReq reqParam) throws Exception {
        return this.getQueryList(reqParam);
    }

    private List<LogDto> getQueryList(LogDtoReq reqParam)
    {
        Query query = this.setCondition(reqParam);
        List<LogDto> list =  mongoTemplate.find(query, LogDto.class);
        return list;
    }

    private Query setCondition(LogDtoReq reqParam) {
        Query query = new Query();
        if(!StringUtils.isBlank(reqParam.getId()))
            query.addCriteria(Criteria.where("_id").is(reqParam.getId()));
        if(!StringUtils.isBlank(reqParam.getStoreKey()))
            query.addCriteria(Criteria.where("storeKey").is(reqParam.getStoreKey()));
        if(!StringUtils.isBlank(reqParam.getUrl()))
            query.addCriteria(Criteria.where("url").regex(".*" + reqParam.getUrl() + ".*"));
        if(!StringUtils.isBlank(reqParam.getSearchText()))
            query.addCriteria(Criteria.where("memo").regex(".*" + reqParam.getSearchText() + ".*")
                            .orOperator(Criteria.where("reqJsonData").regex(".*" + reqParam.getSearchText() + ".*"))
                            .orOperator(Criteria.where("resJsonData").regex(".*" + reqParam.getSearchText() + ".*"))
            );
        if(reqParam.getFrRegDtm() != null && reqParam.getToRegDtm() != null)
            query.addCriteria(Criteria.where("regDtm").gte(reqParam.getFrRegDtm()).lte(reqParam.getToRegDtm()));
        return query;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public LogDto getData(LogDtoReq reqParam) throws Exception {
        List<LogDto> list = this.getQueryList(reqParam);
        if(list.size() == 0) throw new UserException("sm0002", Global.getMessageInfo().getMessage("sm0002"));
        if(list.size() > 1) throw new UserException("sm0003", Global.getMessageInfo().getMessage("sm0003"));

        return list.get(0);
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String insert(LogDto data) throws Exception {
        LogDto res = mongoTemplate.insert(data);
        return MessageConstants.insertOk;
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String update(LogDto data) throws Exception {
        Query query = this.setCondition(Global.getDataInfo().changeToOtherClass(data, LogDtoReq.class));
        Update update = makeUpdateParam(data);
        mongoTemplate.updateMulti(query, update, LogDto.class);
        return MessageConstants.updateOk;
    }

    private Update makeUpdateParam(LogDto data) {
        Update update = new Update();

        update.set("dlvEntpNm", data.getChannelEntpCd());
        update.set("storeKey", data.getStoreKey());
        update.set("url", data.getUrl());
        update.set("reqJsonData", data.getReqJsonData());
        update.set("resJsonData", data.getResJsonData());
        update.set("memo", data.getMemo());
        update.set("regDtm", data.getRegDtm());
        return update;
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String delete(LogDtoReq reqParam) throws Exception {
        Query query = this.setCondition(reqParam);
        mongoTemplate.remove(query, LogDto.class);
        return MessageConstants.deleteOk;
    }

    @Override
    public String errorLogInsert(String exMessage) throws Exception {
        Global.getLogInfo().debug(log, "에러로그 저장", exMessage );
        //List<String> list = Global.getDataInfo().splitByString(exMessage, ExceptionConstants.splitType);
        LogDto dto = makeLogDto(exMessage);

        mongoTemplate.insert(dto);

        return MessageConstants.insertOk;
    }


    private LogDto makeLogDto(String exMessage) {

        LogDto dto = Global.getDataInfo().convertToClass(getErrorData(exMessage),LogDto.class);
        dto.setReqJsonData(getErrorData(exMessage));
        dto.setResJsonData(getExMessage(exMessage));
        dto.setUrl(getUrl(exMessage));
        return dto;
    }

    private String getErrorData(String exMessage) {
        return Global.getDataInfo().splitByIndex(exMessage,  ExceptionConstants.splitType, 0);
    }

    private String getUrl(String exMessage) {
        return Global.getDataInfo().splitByIndex(exMessage,  ExceptionConstants.splitType, 1);
    }

    private String getExMessage(String exMessage) {
        return Global.getDataInfo().splitByIndex(exMessage,  ExceptionConstants.splitType, 2);
    }
}
