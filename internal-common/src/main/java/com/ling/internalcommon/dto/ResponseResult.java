package com.ling.internalcommon.dto;

import com.ling.internalcommon.constant.CommonStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data //自动为每个属性生成getter和setter方法
@Accessors(chain = true)  //链式调用，每次set一个属性后返回整个对象（this）,传统的setter是返回void
public class ResponseResult<T> {

    private int code;
    private String message;
    private T data;

    /**
     *成功相应的方法,无data
     * @return
     * @param <T>
     */
    public static <T> ResponseResult success(){
        return new ResponseResult().setCode(CommonStatusEnum.SUCCESS.getCode()).setMessage(CommonStatusEnum.SUCCESS.getValue());
    }

    /**
     *成功相应的方法,有data
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseResult success(T data){
        return new ResponseResult().setCode(CommonStatusEnum.SUCCESS.getCode()).setMessage(CommonStatusEnum.SUCCESS.getValue()).setData(data);
    }

    /**
     * 统一的失败，只提供具体信息，存到data中
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseResult fail(T data){
        return new ResponseResult().setData(data);
    }

    /**
     * 失败，自定义失败编码和提示信息
     * @param code
     * @param message
     * @return
     * @param <T>
     */
    public static <T> ResponseResult fail(int code,String message){
        return new ResponseResult().setCode(code).setMessage(message);
    }

    /**
     * 失败，自定义失败编码，提示信息和具体错误
     * @param code
     * @param message
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseResult fail(int code,String message,String data){
        return new ResponseResult().setCode(code).setMessage(message).setData(data);
    }

}
