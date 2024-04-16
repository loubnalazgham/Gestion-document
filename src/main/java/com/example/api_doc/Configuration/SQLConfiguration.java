package com.example.api_doc.Configuration;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;


@Configuration
@Transactional
public class SQLConfiguration {

    private PropertiesFactoryBean getPropertiesFactoryBean(final String path) {

        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(path));

        return propertiesFactoryBean;
    }

    @Bean
    public PropertiesFactoryBean documentProperties(){
        return getPropertiesFactoryBean("db/sql/document.properties");
    }

    @Bean
    public PropertiesFactoryBean userProperties(){
        return getPropertiesFactoryBean("db/sql/user.properties");
    }

    @Bean
    public PropertiesFactoryBean autorisationProperties(){
        return getPropertiesFactoryBean("db/sql/autorisation.properties");
    }
}
