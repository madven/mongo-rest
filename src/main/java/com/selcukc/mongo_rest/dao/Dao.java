package com.selcukc.mongo_rest.dao;

import com.mongodb.DBObject;
import com.selcukc.mongo_rest.models.storable.StorableType;

import java.util.List;
import java.util.Optional;

/**
 * Base generic Data Access Object interface.
 *
 */
public interface Dao {

    /**
     * Creates a DB entry.
     *
     * @param <T>   Record type
     * @param input input
     * @return The ID of the resource created
     */
    <T extends StorableType> String create(final T input);

    /**
     * Deletes a DB entry.
     *
     * @param <T>   Record type
     * @param input input
     */
    <T extends StorableType> void remove(final T input);

    /**
     * Searches the DB based on input.
     *
     * @param <T>   Record type
     * @param input input
     * @return List of Record objects
     */
    <T extends StorableType> List<T> search(final T input);


    /**
     * Searches the DB based on input.
     *
     * @param <T>   Record type
     * @param input input
     * @param limit Max number of records, 0 for no limit.
     * @return List of Record objects
     */
    <T extends StorableType> List<T> search(final T input, final int limit);

    /**
     * Searches the DB based on input.
     *
     * @param <T>   Record type
     * @param input input
     * @param limit Max number of records, 0 for no limit.
     * @param offset Number of records to skip.
     * @return List of Record objects
     */
    <T extends StorableType> List<T> search(final T input, final int limit, final int offset);

    /**
     * Searches the DB based on input.
     *
     * @param <T>   Record type
     * @param input input
     * @param limit Max number of records, 0 for no limit.
     * @param offset Number of records to skip.
     * @param keys The fields that should be returned along with _id
     * @return List of Record objects
     */
    <T extends StorableType> List<T> search(final T input, final int limit, final int offset, final DBObject keys);

    /**
     * Searches the DB based on input.
     *
     * @param <T>   Record type
     * @param input input
     * @param limit Max number of records, 0 for no limit.
     * @param offset Number of records to skip.
     * @param keys The fields that should be returned along with _id
     * @param sort The sort fields
     * @return List of Record objects
     */
    <T extends StorableType> List<T> search(final T input, final int limit, final int offset, final DBObject keys,
                                            final DBObject sort);

    /**
     * Searches the DB for a single record.
     * @param input Search parameters
     * @param <T> Record type
     * @return Optional record
     */
    <T extends StorableType> Optional<T> findOne(final T input);

    /**
     * Count of records matching the input.
     *
     * @param <T>   Record type
     * @param input input
     * @return count
     */
    <T extends StorableType> long count(final T input);

    /**
     * Updates a DB entry.
     *
     * @param <T>    Record type
     * @param input  input
     * @param params Update query parameters
     * @return boolean indicating if a record was updated
     */
    <T extends StorableType> boolean update(final T input, final T params);
}

