package com.opentext.itom.ucmdb.integration.push;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = "com.opentext.itom.ucmdb")
@MapperScan("com.opentext.itom.ucmdb.integration.push.framework.target.vertica.mapper")
public class PushApplication {

	public static void main(String[] args) {
		SpringApplication.run(PushApplication.class, args);
	}

}
