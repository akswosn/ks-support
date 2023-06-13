package com.crypted.kssupport.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource


@Configuration
@EnableJpaRepositories(
    basePackages = ["com.crypted.kssupport.repository.main"],
    entityManagerFactoryRef = "mainEntityManagerFactory",
    transactionManagerRef = "mainTransactionManager"
)
class KsmainDatasourceConfig (
    private val jpaProperties: JpaProperties,
    private val hibernateProperties: HibernateProperties
){
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.ksmain")
    fun mainDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    fun mainDataSource(): DataSource {
        return mainDataSourceProperties()
            .initializeDataSourceBuilder()
            .build()
    }

    @Bean
    @Primary
    fun mainEntityManagerFactory(
        builder: EntityManagerFactoryBuilder
    ): LocalContainerEntityManagerFactoryBean {
        val properties = hibernateProperties.determineHibernateProperties(
            jpaProperties.properties, HibernateSettings()
        )

        return builder.dataSource(mainDataSource())
            .packages("com.crypted.kssupport.entity.main")
            .persistenceUnit("mainEntityManager")
            .properties(properties)
            .build()
    }

    @Bean
    @Primary
    fun mainTransactionManager(
        @Qualifier(value = "mainEntityManagerFactory")  entityManagerFactory : LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager {
        var transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory.`object`

        return transactionManager
    }
}