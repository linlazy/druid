package com.example.druid.db.entity;

import lombok.Data;

@Data
    public class KeyValueEntry<T,S> {
        private T key;
        private S value;
    }