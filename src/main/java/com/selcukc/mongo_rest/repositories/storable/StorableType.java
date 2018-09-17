package com.selcukc.mongo_rest.repositories.storable;

import org.mongojack.DBQuery;

import java.util.Optional;
import java.util.UUID;

public interface StorableType {

    /**
     * Get UUID.
     *
     * @return UUID
     */
    Optional<UUID> getUuid();

    /**
     * Set UUID.
     *
     * @param uuid uuid
     */
    void setUuid(UUID uuid);

    /**
     * Search query for the underlying condition.
     * If query was not set using setQuery then it will generate a query based
     * on the fields present.
     *
     * @return Search query
     */
    DBQuery.Query getQuery();
}
