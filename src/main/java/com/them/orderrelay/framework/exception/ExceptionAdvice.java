package com.them.orderrelay.framework.exception;


import com.them.orderrelay.framework.base.dto.ResDto;
import com.them.orderrelay.framework.util.Global;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {



    // 400
    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<Object> BadRequestException(final RuntimeException ex) {
        //exceptionSave(ex);
        return ResponseEntity.badRequest().body(new ResDto("-1", ex.getMessage()));
    }


    // 401
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity handleAccessDeniedException(final AccessDeniedException ex) {
        ResDto errorDto = new ResDto("-1", ex.getMessage());
        Global.getLogInfo().warn(log, "AccessDeniedException", ex.getMessage() );
        //exceptionSave(ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    /**
     * Dto Validation
     * @param ex
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResDto> validException(MethodArgumentNotValidException ex) {
        ResDto<String> dto =this.getMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity(dto, HttpStatus.BAD_REQUEST); // 2
    }

    private ResDto getMessage(String message)
    {
        ResDto<String> rtn = new ResDto();
        setDefaultError(message, rtn);
        if(!StringUtils.isBlank(message) )
        {
            setError(message, rtn);
        }
        return rtn;
    }

    private void setDefaultError(String message, ResDto<String> rtn) {
        rtn.setResCd("-1");
        rtn.setResMsg(StringUtils.isBlank(message) ? "요청데이터가 유효하지 않습니다." : message);
    }

    private void setError(String message, ResDto<String> rtn) {
        String[] arrMsg = message.split("\\|");

        if(arrMsg.length == 2){
            rtn.setResCd(arrMsg[0]);
            rtn.setResMsg(String.format(Global.getMessageInfo().getMessage(arrMsg[0]), arrMsg[1]));
        }
    }

    //@ExceptionHandler 어노테이션과 함께 사용할 수 있다.
    //구체적인 응답 코드를 줄 뿐 아니라, 간단한 사유도 전달 할 수 있다.
    @ResponseStatus(HttpStatus.FORBIDDEN)  // 403
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ResDto> handleForbiddenException(ForbiddenException ex) {
        log.error(ex.getMessage());
        //exceptionSave(ex);
        ResDto<String> errorDto = new ResDto("-1", ex.getMessage());
        return new ResponseEntity(errorDto, HttpStatus.FORBIDDEN);
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ResDto> handleAll(final Exception ex) {
        Global.getLogInfo().error(log, "500 error", ex.getMessage());
        ResDto<String> errorDto = new ResDto("-1", ex.getMessage());
        //exceptionSave(ex);
        return new ResponseEntity(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ UserException.class })
    public ResponseEntity<ResDto> handleAll(final UserException ex) {
        Global.getLogInfo().error(log, "500 error UserException", ex.getMessage());
        //exceptionSave(ex);
        ResDto<String> errorDto = new ResDto<String>(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}