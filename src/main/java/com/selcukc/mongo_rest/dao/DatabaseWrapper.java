package com.selcukc.mongo_rest.dao;

import com.mongodb.DBObject;
import com.selcukc.mongo_rest.repositories.storable.StorableType;

/**
 * Interface for wrapper for transactions between the underlying datastore and
 * the service.
 */
public interface DatabaseWrapper<R> {
    /**
     * Deletes records from the underlying datastore based on the input.
     *
     * @param <I> Input type
     * @param in  Record to delete
     */
    <I extends StorableType> void delete(I in);

    /**
     * Inserts a record in the underlying datastore.
     *
     * @param <I> Input type
     * @param in  Record to store
     * @return The ID of the resource created
     */
    <I extends StorableType> String insert(I in);

    /**
     * Queries records from the underlying datastore.
     *
     * @param <I> Input type
     * @param in  Search parameters
     * @param limit Max number of results, 0 for no limit
     * @param offset Skip number of results
     * @param keys The fields to be selected
     * @param sort THe sort fields
     * @return Cursor for select result
     */
    <I extends StorableType> R select(I in, int limit, int offset, DBObject keys, DBObject sort);

    /**
     * Queries records from the underlying datastore.
     *
     * @param <I> Input type
     * @param in  Search parameters
     * @return Result or null
     */
    <I extends StorableType> I findOne(I in);

    /**
     * Updates a record in the underlying datastore.
     *
     * @param <I>    Input type
     * @param in     Updated record
     * @param params The query params for the update
     * @return boolean indicating if a record was updated
     */
    <I extends StorableType> boolean update(I in, I params);
}

