package com.central.webboard.utils.globalException.service;

import com.central.webboard.utils.globalException.domain.ErrorLog;

public interface ErrorLogService {
    void save(ErrorLog errorLog);
}
