package com.them.orderrelay.framework.base.dto;

import com.them.orderrelay.framework.util.Global;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResDto<T> {
    private String resCd = "";
    private String resMsg = "";
    private T data;
    public ResDto() {
        this.resCd = "1";
        this.resMsg = Global.getMessageInfo().getMessage("cm0010");
    }

    public ResDto(T data) {
        this.resCd = "";
        this.resMsg = Global.getMessageInfo().getMessage("cm0010");
        this.data = data;
    }

    public ResDto(String resCd, String resMsg) {
        this.resCd = resCd;
        this.resMsg = resMsg;
    }

    public ResDto(T data, String resCd, String resMsg) {
        this.resCd = resCd;
        this.resMsg = resMsg;
        this.data = data;
    }

    public Boolean isOk(){
        return this.resCd.equals("1");
    }
}
