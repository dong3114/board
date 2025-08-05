package com.central.webboard.utils.globalException.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorLog {
    private String uri;
    private String method;
    private String message;
    private String stackTrace;
    private Date createdAt;
}
