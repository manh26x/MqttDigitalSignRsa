package com.mike.mqttdigitalsignrsa

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.SimpleTransactionStatus

@Configuration
open class ApplicationConfiguration {
    @Bean
    open fun transactionManager(): PlatformTransactionManager {
        return object : PlatformTransactionManager {
            override fun getTransaction(definition: TransactionDefinition?): TransactionStatus =
                    SimpleTransactionStatus()

            override fun commit(status: TransactionStatus) {
            }

            override fun rollback(status: TransactionStatus) {
            }
        }
    }
}
