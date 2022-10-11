package com.mike.mqttdigitalsignrsa.entity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Native;

@Repository
public interface DataItemRepository extends CrudRepository<DataItem, String> {

    @Query(
            value = " SELECT generateUUIDv4()")
    String getNewItemId();
}
