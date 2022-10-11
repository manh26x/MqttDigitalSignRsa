CREATE DATABASE spring_demo;

CREATE TABLE spring_demo.data_item
(
    item_id String,
    timestamp DateTime64,
    data Float32,
    item_key String,
    construction String
)
    ENGINE = MergeTree()
PRIMARY KEY (item_id);
