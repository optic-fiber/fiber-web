package com.cheroliv.fiber.config

import groovy.transform.CompileStatic
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.documentation.swagger2.annotations.EnableSwagger2

//import groovy.transform.CompileStatic
//import org.springframework.boot.autoconfigure.domain.EntityScan
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Import
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories
//import org.springframework.jdbc.datasource.DriverManagerDataSource
//import org.springframework.orm.jpa.JpaTransactionManager
//import org.springframework.orm.jpa.JpaVendorAdapter
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
//import org.springframework.orm.jpa.vendor.Database
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
//import org.springframework.transaction.PlatformTransactionManager
//import org.springframework.transaction.annotation.EnableTransactionManagement
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
//
//import javax.sql.DataSource
//import javax.validation.Validator
@CompileStatic
@Configuration
@ComponentScan("com.cheroliv.fiber")
//@EnableConfigurationProperties([FiberProperties.class])
//@EnableTransactionManagement
//@EnableJpaRepositories("com.cheroliv.fiber.dao")
//@EntityScan("com.cheroliv.fiber.domain")
//@Import([CacheConfiguration.class])
//@EnableConfigurationProperties([FiberProperties.class])
@EnableCaching
@EnableFeignClients(basePackages = "com.cheroliv.fiber")
@EnableSwagger2
@Import([FeignClientsConfiguration.class,
        SecurityConfiguration.class])
class Config {


//    @Bean
//    Validator validator() {
//        new LocalValidatorFactoryBean()
//    }
//
//    @Bean
//    DataSource dataSource() {
//        new DriverManagerDataSource(
//                driverClassName: "org.postgresql.Driver",
//                url: "jdbc:postgresql://localhost:5432/fiber",
//                username: "tech",
//                password: "tech")
//    }
//
//    @Bean
//    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean emf =
//                new LocalContainerEntityManagerFactoryBean(
//                        jpaVendorAdapter: jpaVendorAdapter(),
//                        dataSource: dataSource())
//        emf.setPackagesToScan("com.cheroliv.fiber.domain")
//        emf
//    }
//
//    @Bean
//    JpaVendorAdapter jpaVendorAdapter() {
//        new HibernateJpaVendorAdapter(
//                showSql: true,
//                generateDdl: true,
//                database: Database.POSTGRESQL)
//    }
//
//    @Bean
//    PlatformTransactionManager transactionManager() {
//        new JpaTransactionManager()
//    }
}
