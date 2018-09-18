package com.selcukc.mongo_rest.dao;

import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.selcukc.mongo_rest.models.storable.StorableType;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.util.Assert.notNull;

public class MongoDao extends BaseDao<MongoWrapper> implements Dao {
    public MongoDao(final MongoWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public <T extends StorableType> String create(final T input) {
        input.setUuid(UUID.randomUUID());
        return getWrapper().insert(input);
    }

    @Override
    public <T extends StorableType> void remove(final T input) {
        notNull(input.getUuid());
        getWrapper().delete(input);
    }

    @Override
    public <T extends StorableType> Optional<T> findOne(final T input) {
        return Optional.ofNullable(getWrapper().findOne(input));
    }

    @Override
    public <T extends StorableType> List<T> search(final T input) {
        return search(input, 0, 0);
    }

    @Override
    public <T extends StorableType> List<T> search(final T input, final int limit) {
        return search(input, limit, 0);
    }

    @Override
    public <T extends StorableType> List<T> search(T input, int limit, int offset) {
        return search(input, limit, offset, null);
    }

    @Override
    public <T extends StorableType> List<T> search(T input, int limit, int offset, DBObject keys) {
        return search(input, limit, offset, keys, null);
    }

    @Override
    public <T extends StorableType> List<T> search(T input, int limit, int offset, DBObject keys, DBObject sort) {
        final DBCursor<T> cursor = getWrapper().select(input, limit, offset, keys, sort);
        final List<T> results = new ArrayList<>();

        try {
            while (cursor.hasNext()) {
                results.add(cursor.next());
            }
        } catch (final MongoException e) {
            logger.error("Failed to read result from DB", e);
        }
        cursor.close();

        return results;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends StorableType> long count(final T input) {
        notNull(input);
        final JacksonDBCollection collection = getWrapper().getCollection(input.getClass());
        return collection.getCount(input.getQuery());
    }

    @Override
    public <T extends StorableType> boolean update(final T input, final T params) {
        notNull(input.getUuid());
        return getWrapper().update(input, params);
    }
}

