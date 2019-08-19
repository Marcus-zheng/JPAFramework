package com.marcus.base.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 15:03
 **/
@Setter
@Getter
@Accessors(chain = true)
public class ResultMessage implements Serializable {
    private static final long serialVersionUID = 3425802030923835715L;

    public final static int SUCCESS = 0;

    public final static int FAIL = -1;

    private int code = 0;

    private String message = "common_op_succeed";

    private Object data;

    public ResultMessage(){
        super();
    }

    public ResultMessage(Object data){
        super();
        this.data = data;
    }

    public ResultMessage(int code, String message, Object data){
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResultMessage resultSuccess(){
        return new ResultMessage(ResultMessage.SUCCESS,"common_op_succeed",null);
    }

    public static ResultMessage resultSuccess(Object data){
        return new ResultMessage(ResultMessage.SUCCESS,"common_op_succeed",data);
    }


    public static ResultMessage returnFail(int code, String message){
        return new ResultMessage(code, message,null);
    }

    public static ResultMessage returnDefaultFail(String message){
        return new ResultMessage(ResultMessage.FAIL, message,null);
    }
}
