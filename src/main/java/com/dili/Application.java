package com.dili;

import com.dili.ss.dto.DTOScan;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.dili.ss.retrofitful.annotation.RestfulScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 由MyBatis Generator工具自动生成
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.dili.settlement.mapper", "com.dili.ss.dao"})
@ComponentScan(basePackages={"com.dili.ss","com.dili.settlement","com.dili.uap.sdk"})
@RestfulScan({"com.dili.settlement.rpc","com.dili.uap.sdk.rpc"})
@DTOScan(value={"com.dili.ss", "com.dili.settlement.domain"})
@EnableDiscoveryClient
@EnableFeignClients

public class Application extends SpringBootServletInitializer {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
