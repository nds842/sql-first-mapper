package com.github.nds842.sqlfirst.springsample;

import com.github.nds842.sqlfirst.apc.SqlFirstApcConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SqlFirstApcConfig(baseDaoClassName = "com.github.nds842.sqlfirst.springsample.repository.BaseSpringDao")
@SpringBootApplication
public class Application implements CommandLineRunner {
    
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
    }
    
}