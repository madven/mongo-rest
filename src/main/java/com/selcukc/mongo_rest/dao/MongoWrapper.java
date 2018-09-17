package com.selcukc.mongo_rest.dao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.Lists;
import com.mongodb.*;
import com.selcukc.mongo_rest.repositories.storable.Storable;
import com.selcukc.mongo_rest.repositories.storable.StorableType;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.UnknownHostException;
import java.util.List;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

public class MongoWrapper implements DatabaseWrapper<DBCursor<?>> {
    private static final Logger LOG = LoggerFactory.getLogger(MongoWrapper.class);
    private static final String DB_NAME = "sonder";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final DB db;

    /**
     * @param host Mongo host
     * @param port port
     * @throws UnknownHostException When the mongo host is not found
     */
    public MongoWrapper(final String host, final int port) throws UnknownHostException {
        db = getDb(host, port);
    }

    @Autowired
    private void setJdk8Module(final Jdk8Module jdk8Module) {
        MAPPER.registerModule(jdk8Module);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Creates a DB instance.
     *
     * @param host Mongo host
     * @param port port
     * @return DB instance for DB_NAME
     * @throws UnknownHostException When the mongo host is not found
     */
    protected DB getDb(final String host, final int port) throws UnknownHostException {
        final String[] hosts = host.split(",");
        final List<ServerAddress> addressList = Lists.newArrayList();
        for (String s : hosts) {
            addressList.add(new ServerAddress(s, port));
        }
        return new MongoClient(addressList).getDB(DB_NAME);
    }

    public DB getDb() {
        return db;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <I extends StorableType> void delete(final I in) {
        notNull(in);
        final JacksonDBCollection collection = getCollection(in.getClass());
        collection.remove(new BasicDBObject("_id", in.getUuid()));
    }

    /**
     * Performs a group by operation.
     * @param clazz The collection on which to operate
     * @param keys The fields for the grouping
     * @param condition Search conditions
     * @param init Initial values for group by result
     * @param reduce Reduce function
     * @param finalize Finalize function
     * @return BasicDBList with results
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BasicDBList group(final Class clazz,
                             final DBObject keys, final DBObject condition, final DBObject init,
                             final String reduce, final String finalize) {
        notNull(clazz);
        final JacksonDBCollection collection = getCollection(clazz);
        return (BasicDBList) collection.group(keys, condition, init, reduce, finalize);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <I extends StorableType> String insert(final I in) {
        notNull(in);
        final JacksonDBCollection collection = getCollection(in.getClass());
        return collection.insert(in).getSavedId().toString();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <I extends StorableType> DBCursor<I> select(final I in, final int limit, final int offset,
                                                       final DBObject keys, final DBObject sort) {
        notNull(in);
        final JacksonDBCollection collection = getCollection(in.getClass());
        DBCursor<I> cursor;
        if (keys != null) {
            cursor = collection.find(in.getQuery(), keys);
        } else {
            cursor = collection.find(in.getQuery());
        }

        cursor = cursor.skip(offset);

        if (limit > 0) {
            cursor = cursor.limit(limit);
        }

        if (sort != null) {
            cursor = cursor.sort(sort);
        }

        return cursor;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <I extends StorableType> I findOne(final I in) {
        notNull(in);
        final JacksonDBCollection collection = getCollection(in.getClass());

        return (I) collection.findOne(in.getQuery());
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <I extends StorableType> boolean update(final I in, final I params) {
        notNull(in);
        notNull(params);
        final JacksonDBCollection collection = getCollection(in.getClass());
        return collection.update(params.getQuery(), collection.convertToDbObject(in)).getN() == 1;
    }

    /**
     * Gets a collection.
     *
     * @param <I>  Input type marked @Storable
     * @param type Variable to determine type of I
     * @return DBCollection for the collection name
     */
    public <I extends StorableType> JacksonDBCollection<I, DBObject> getCollection(@Storable final Class<I> type) {
        notNull(type);
        final String collectionName = type.getAnnotation(Storable.class).value();
        hasText(collectionName);
        return JacksonDBCollection.wrap(db.getCollection(collectionName), type, DBObject.class, MAPPER);
    }
}

