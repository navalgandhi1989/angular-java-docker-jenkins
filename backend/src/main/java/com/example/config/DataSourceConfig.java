package com.example.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.boot.model.naming.PhysicalNamingStrategySnakeCaseImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class DataSourceConfig {

	private static final  String HIBERNATE_HBM2DDL_AUTO = "validate";

	@Value("${spring.jpa.show-sql}")
	private String HIBERNATE_SHOW_SQL;
	
	@Bean
	@DependsOn("springLiquibase")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setDataSource(dataSource);
		entityManager.setPackagesToScan(new String[] { "com.example" });
		
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManager.setJpaVendorAdapter(vendorAdapter);
		entityManager.setJpaProperties(getHibernateProperties());
		
		return entityManager;
	}

	@Bean
	public Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.default_schema", "public");
		properties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
		properties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBM2DDL_AUTO);
		properties.put("hibernate.physical_naming_strategy", PhysicalNamingStrategySnakeCaseImpl.class.getPackage().getName() + "." + PhysicalNamingStrategySnakeCaseImpl.class.getSimpleName());
		properties.put("hibernate.enable_lazy_load_no_trans", "true");
		

		return properties;
	}
}
