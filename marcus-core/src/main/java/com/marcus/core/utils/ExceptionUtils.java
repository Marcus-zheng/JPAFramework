package com.marcus.core.utils;

import com.marcus.base.exception.BusinessException;
import com.marcus.base.vo.ResultMessage;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import java.util.List;

public abstract class ExceptionUtils {




    public static ResultMessage getErrorResult(Throwable e){


        e = getThrowable(e);

        if (e instanceof BusinessException)
        {
            BusinessException ex = (BusinessException) e;

            return ResultMessage.returnFail(ex.getCode(),ex.getMessage());
        }
        else if (e instanceof BindException)
        {
            BindException ex = (BindException) e;

            return ResultMessage.returnDefaultFail(getBindErrorMessage(ex));
        }
        else
        {
            return ResultMessage.returnDefaultFail("系统异常");
        }


    }

    private static String getBindErrorMessage(BindException ex) {

        List<ObjectError> allErrors = ex.getAllErrors();

        StringBuilder errorMessage = new StringBuilder();

        allErrors.forEach( (error) -> errorMessage.append(error.getDefaultMessage()).append(","));

        if (errorMessage.length() > 0)
        {
            return errorMessage.substring(0,errorMessage.length() - 1);
        }

        return "系统异常";
    }


    public static String getErrorMessage(Throwable e){

        return getThrowable(e).getMessage();

    }

    private static Throwable getThrowable(Throwable e)
    {

        Throwable cause = e.getCause();

        if (cause != null)
        {
            e = getThrowable(cause);
        }

        return e;
    }
}
