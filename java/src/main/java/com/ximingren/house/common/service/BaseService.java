package com.ximingren.house.common.service;

import com.alibaba.fastjson.JSONObject;
import com.ximingren.house.common.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//为什么用了抽象类，又要用接口类
public abstract class BaseService<T> implements IBaseService<T> {
    public ResponseDto<T> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
