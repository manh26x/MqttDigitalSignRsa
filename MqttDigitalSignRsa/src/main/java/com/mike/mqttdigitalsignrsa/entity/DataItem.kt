package com.mike.mqttdigitalsignrsa.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("data_item")
data class DataItem(
        val timestamp: Timestamp,
        val data: Float,
        val item_key: String,
        val construction: String
) :
        Persistable<String> {
    @JsonIgnore
    override fun getId(): String = ""
    @JsonIgnore
    override fun isNew(): Boolean = true


}