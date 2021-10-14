package com.them.orderrelay.domain.mongo.base.service;

import com.mongodb.client.result.UpdateResult;
import com.them.orderrelay.domain.mongo.base.MessageConstants;
import com.them.orderrelay.domain.mongo.base.dto.StoreDto;
import com.them.orderrelay.domain.mongo.base.dto.StoreDtoReq;
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
public class StoreServiceImpl implements StoreService {

    private final MongoTemplate mongoTemplate;

    public StoreServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public List<StoreDto> getList(StoreDtoReq reqParam) throws Exception {

        return this.getStoreList(reqParam);
    }

    private List<StoreDto> getStoreList(StoreDtoReq reqParam)
    {
        Query query = this.setCondition(reqParam);
        List<StoreDto> list =  mongoTemplate.find(query, StoreDto.class);
        return list;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public StoreDto getData(StoreDtoReq param) throws UserException {
        List<StoreDto> list = this.getStoreList(param);
        if(list.size() == 0) throw new UserException("sm0002", Global.getMessageInfo().getMessage("sm0002"));
        if(list.size() > 1) throw new UserException("sm0003", Global.getMessageInfo().getMessage("sm0003"));

        return list.get(0);
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String insert(StoreDto data) throws Exception {
        StoreDto res = mongoTemplate.insert(data);
        return MessageConstants.insertOk;
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String update(StoreDto data) throws Exception {
        Query query = this.setCondition(Global.getDataInfo().changeToOtherClass(data, StoreDtoReq.class));

        Update update = new Update();
        update.set("entpNm", data.getEntpNm());
        update.set("storeNm", data.getStoreNm());
        update.set("dlvEntpNm", data.getDlvEntpNm());
        update.set("isUse", data.getIsUse());
        update.set("modId", data.getModId());
        update.set("modDtm", data.getModDtm());
        update.set("depositAmt",data.getDepositAmt());
        UpdateResult result = mongoTemplate.updateMulti(query, update, StoreDto.class);
        return MessageConstants.updateOk;
    }

    @Transactional(rollbackFor= Exception.class)
    @Override
    public String delete(StoreDtoReq reqParam) throws Exception {
        Query query =this.setCondition(reqParam);

        mongoTemplate.remove(query, StoreDto.class);

        return MessageConstants.deleteOk;

    }

    private Query setCondition(StoreDtoReq param){
        Query query = new Query();
        if(!StringUtils.isBlank(param.getId()))
            query.addCriteria(Criteria.where("_id").is(param.getId()));
        if(!StringUtils.isBlank(param.getEntpCd()))
            query.addCriteria(Criteria.where("entpCd").is(param.getEntpCd()));
        if(!StringUtils.isBlank(param.getChannelEntpCd()))
            query.addCriteria(Criteria.where("channelEntpCd").is(param.getChannelEntpCd()));
        if(!StringUtils.isBlank(param.getStoreCd()))
            query.addCriteria(Criteria.where("storeCd").is(param.getStoreCd()));
        if(!StringUtils.isBlank((param.getChannelStoreCd())))
            query.addCriteria(Criteria.where("channelStoreCd").is(param.getChannelStoreCd()));
        if(param.getIsUse() != null)
            query.addCriteria(Criteria.where("isUse").is(param.getIsUse()));


        return query;
    }

    @Override
    public <T> String StoreUpdateDepositAmt(T dto, String dpstBalAmt) throws Exception {
        StoreDtoReq storeDtoReq = Global.getDataInfo().changeToOtherClass(dto, StoreDtoReq.class);
        List<StoreDto> list = this.getStoreList(storeDtoReq);

        if(list.size() == 0) {
            Global.getLogInfo().info(log,"예치금 업데이트", "매장정보가 없습니다." );
            return "";
        }
        StoreDto data = this.getStoreList(storeDtoReq).get(0);
        data.setDepositAmt(Global.getNumberInfo().parseDouble(dpstBalAmt));
        this.update(data);
        return MessageConstants.updateOk;
    }
}
