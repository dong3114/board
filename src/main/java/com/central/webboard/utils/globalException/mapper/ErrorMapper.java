package com.central.webboard.utils.globalException.mapper;

import com.central.webboard.utils.globalException.domain.ErrorLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErrorMapper {
    void save(ErrorLog errorLog);
}
