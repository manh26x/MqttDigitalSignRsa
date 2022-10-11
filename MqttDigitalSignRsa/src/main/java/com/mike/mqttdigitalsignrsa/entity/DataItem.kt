package com.mike.mqttdigitalsignrsa.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.sql.Timestamp

@Table("data_item")
data class DataItem(
        val itemId: String,
        val timestamp: Timestamp,
        val data: Float,
        val item_key: String,
        val construction: String
) :
        Persistable<String> {
    override fun getId(): String? {
        return this.itemId;
    }

    @JsonIgnore
    override fun isNew(): Boolean = true


}