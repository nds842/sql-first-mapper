package com.github.nds842.sqlfirst.springsample;

import com.github.nds842.sqlfirst.apc.DaoType;
import com.github.nds842.sqlfirst.apc.SqlFirstApcConfig;
import com.github.nds842.sqlfirst.springsample.system.SqlFirstSpringQueryExecutor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SqlFirstApcConfig(
        daoType = DaoType.SPRING_REPOSITORY,
        queryExecutorClassName = "com.github.nds842.sqlfirst.springsample.system.SqlFirstSpringQueryExecutor"
)
@SpringBootApplication(
        scanBasePackages = "com.github.nds842.sqlfirst.springsample.system"
)

public class Application implements CommandLineRunner {
    
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
    }
    
    @Bean
    public SqlFirstSpringQueryExecutor getSqlFirstSpringQueryExecutor(){
        return new SqlFirstSpringQueryExecutor();
    }
    
}