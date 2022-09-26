package com.mike.mqttdigitalsignrsa.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataItemRepository extends CrudRepository<DataItem, String> {
}
