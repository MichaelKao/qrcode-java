package com.qrcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
public class QrcodeApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(QrcodeApplication.class, args);
	}

	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 設置所有路徑都支持 CORS
                .allowedOrigins("http://localhost:4200")  // 允許來自 localhost:4200 的請求
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允許的 HTTP 方法
                .allowedHeaders("*")  // 允許的 header
                .allowCredentials(true);  // 是否允許 cookie
    }
}
