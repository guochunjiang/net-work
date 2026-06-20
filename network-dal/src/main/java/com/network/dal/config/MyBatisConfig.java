package com.network.dal.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.network.dal.mapper")
public class MyBatisConfig {

}
