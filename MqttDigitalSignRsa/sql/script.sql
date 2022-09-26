CREATE DATABASE spring_demo;

CREATE TABLE spring_demo.data_item
(
    item_id String,
    timestamp DateTime64,
    data FLOAT,
    item_key String,
    construction String
)
    ENGINE = MergeTree()
PRIMARY KEY (item_id);