package com.selcukc.mongo_rest.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.Assert.notNull;

public abstract class BaseDao<W extends DatabaseWrapper<?>> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final W wrapper;

    /**
     * @param wrapper The database wrapper
     */
    protected BaseDao(final W wrapper) {
        notNull(wrapper);
        this.wrapper = wrapper;
    }

    protected W getWrapper() {
        return wrapper;
    }
}

