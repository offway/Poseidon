package cn.offway.Poseidon.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.offway.Poseidon.properties.QiniuProperties;

@Configuration
@EnableConfigurationProperties(QiniuProperties.class)
public class QiniuConfig {

}
