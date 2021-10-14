package com.them.orderrelay.framework.restapi;

import com.them.orderrelay.domain.mongo.base.service.LogService;
import com.them.orderrelay.framework.exception.ExceptionConstants;
import com.them.orderrelay.framework.util.Global;
import com.them.orderrelay.framework.util.mail.MailData;
import com.them.orderrelay.framework.util.mail.MessageType;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;

@Slf4j
@Component
public class RestApiInfo<TReq, T>  {

    private final LogService logService;

    public RestApiInfo(LogService logService) {

        this.logService = logService;
    }

    public T call(TReq req, RestApiHeader header, Class<T> responseType) throws Exception
    {
        return this.call(req,header,"",responseType);
    }

    public T call(TReq req, RestApiHeader header, String url , Class<T> responseType) throws Exception
    {
        HttpHeaders headers = header.getHeader();

        RestTemplate restTemplate = restTemplate();

        Global.getLogInfo().info(log, "대행사 요청로그", Global.getDataInfo().convertToObjectMapperString(req));

        HttpEntity<TReq> request = new HttpEntity<>(req, headers);

        return callExec(req, header, url, responseType, restTemplate, request);
    }

    private T callExec(TReq req, RestApiHeader header, String url, Class<T> responseType, RestTemplate restTemplate, HttpEntity<TReq> request) throws Exception {
        ResponseEntity<T> response;
        try {
            response = restTemplate.postForEntity(URI.create(header.getServerUrl() + url), request, responseType);
            Global.getLogInfo().info(log, " 응답로그", Global.getDataInfo().convertToObjectMapperString(response));
            return response.getBody();
        }catch(Exception ex){
            logInsert(req, header, url, ex.getMessage());
            return errorParsing(responseType, (HttpClientErrorException.BadRequest) ex);
        }
    }

    private T errorParsing(Class<T> responseType, HttpClientErrorException.BadRequest ex) {
        try {
            String json = ex.getResponseBodyAsString();

            int status = ex.getRawStatusCode();
            return Global.getDataInfo().convertToObjectMapperClass(json, responseType);
        } catch(Exception e)
        {
        }
        return null;
    }

    private void logInsert(TReq req, RestApiHeader header, String url, String exMessage) throws Exception {
        try {
            String errorMsg = String.format("%s" + ExceptionConstants.splitType + "%s", Global.getDataInfo().convertToObjectMapperString(req), header.getServerUrl() + url) + ExceptionConstants.splitType + exMessage;
            logService.errorLogInsert(Global.getDataInfo().convertToObjectMapperString(req) + ExceptionConstants.splitType
                                                + header.getServerUrl() + url + ExceptionConstants.splitType
                                                + exMessage);
            Global.getMailInfo().sendMail(
                    MailData.builder().
                            toMail("shjeon@themcompany.kr").
                            title("[프렌즈플러스] Api 서버 알림이 있습니다.").
                            messageType(MessageType.error).
                            message(errorMsg).build());
        }catch(Exception ex)
        {
            throw new Exception("MongoDB 로그저장시 에러가 발생하였습니다.");
        }
    }

    public T getCall(RestApiHeader header, String url , Class<T> responseType) throws Exception
    {
        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = header.getHeader();
        ResponseEntity<T> response = restTemplate.exchange(header.getServerUrl() + url, HttpMethod.GET, new HttpEntity<>( headers),responseType);
        Global.getLogInfo().info(log, " 응답로그", Global.getDataInfo().convertToObjectMapperString(response));
        return response.getBody();
    }

    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000); // read timeout
        factory.setConnectTimeout(3000); // connection timeout

        //Apache HttpComponents HttpClient
        HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(50)//최대 커넥션 수
                  .setMaxConnPerRoute(20).build(); //각 호스트(IP와 Port의 조합)당 커넥션 풀에 생성가능한 커넥션 수
        factory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }


}
