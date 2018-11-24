package com.itheima.ssm.service;

import com.itheima.ssm.domain.SysLog;

import java.util.List;

public interface SysLogService {
    List<SysLog> findAll() throws Exception;
    public void save(SysLog sysLog) throws Exception;
}
