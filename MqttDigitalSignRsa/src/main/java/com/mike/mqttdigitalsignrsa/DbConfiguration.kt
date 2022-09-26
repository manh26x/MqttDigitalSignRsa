package com.mike.mqttdigitalsignrsa


import com.mike.mqttdigitalsignrsa.converter.StringListReadingConverter
import com.mike.mqttdigitalsignrsa.converter.StringListWritingConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration

@Configuration
open class DbConfiguration : AbstractJdbcConfiguration() {
    @Bean
    override fun jdbcCustomConversions(): JdbcCustomConversions {
        return JdbcCustomConversions(
                mutableListOf(
                        StringListWritingConverter(),
                        StringListReadingConverter()
                )
        )
    }
}