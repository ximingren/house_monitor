package com.ximingren.house.common.service;

import com.alibaba.fastjson.JSONObject;
import com.ximingren.house.common.dto.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IBaseService<T> {
    ResponseDto<T> process(JSONObject params, HttpServletRequest request, HttpServletResponse response);

}
