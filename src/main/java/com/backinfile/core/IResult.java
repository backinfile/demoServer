package com.backinfile.core;

import com.backinfile.core.serilize.ISerializable;

public interface IResult extends ISerializable {

    /**
     * 获取默认参数
     */
    <T> T getResult();

    /**
     * 获取参数
     */
    <T> T getResult(String key);

    <T> T getContext();
    <T> T getContext(String key);

    void updateContexts(Object[] contexts);


    boolean isErrorOccurred();

}
