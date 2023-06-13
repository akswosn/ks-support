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
    basePackages = ["com.crypted.kssupport.repository.sub"],
    entityManagerFactoryRef = "subEntityManagerFactory",
    transactionManagerRef = "subTransactionManager"
)
class KstadiumWebDbDatasourceConfig (
    private val jpaProperties: JpaProperties,
    private val hibernateProperties: HibernateProperties
){
    @Bean
    @ConfigurationProperties("spring.datasource.kstadium-web-db")
    fun subDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun subDataSource(): DataSource {
        return subDataSourceProperties()
            .initializeDataSourceBuilder()
            .build()
    }

    @Bean
    fun subEntityManagerFactory(
        builder: EntityManagerFactoryBuilder
    ): LocalContainerEntityManagerFactoryBean {
        val properties = hibernateProperties.determineHibernateProperties(
            jpaProperties.properties, HibernateSettings()
        )

        return builder.dataSource(subDataSource())
            .packages("com.crypted.kssupport.entity.sub")
            .persistenceUnit("rdbEntityManager")
            .properties(properties)
            .build()
    }

    @Bean
    fun subTransactionManager(
        @Qualifier(value = "subEntityManagerFactory")  entityManagerFactory : LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager {
        var transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory.`object`

        return transactionManager
    }
}