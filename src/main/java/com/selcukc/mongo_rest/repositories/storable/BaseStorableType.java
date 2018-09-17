package com.selcukc.mongo_rest.repositories.storable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.mongojack.DBQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public abstract class BaseStorableType implements StorableType {
    private static final String BASE_VERSION = "2015-12-01";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Optional<String> version;

    private Optional<UUID> uuid;
    private Optional<DBQuery.Query> query = Optional.empty();

    protected BaseStorableType(final UUID uuid, final String version) {
        this.uuid = Optional.ofNullable(uuid);
        this.version = Optional.ofNullable(version);
    }

    @JsonProperty("_id")
    @Override
    public Optional<UUID> getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(final UUID uuid) {
        this.uuid = Optional.ofNullable(uuid);
    }

    public String getVersion() {
        return version.orElse(BASE_VERSION);
    }

    /**
     * List of conditions to perform search on the underlying collection.
     * UUID and version are automatically added if they are present.
     *
     * @return DBQuery.Query with search conditions
     */
    protected abstract DBQuery.Query getQueryConditions();

    /**
     * Search query for the underlying condition.
     * If query was not set using setQuery then it will generate a query based
     * on the fields present.
     *
     * @return Search query
     */
    @JsonIgnore
    public DBQuery.Query getQuery() {
        if (query.isPresent()) {
            return query.get();
        }

        if (uuid.isPresent()) {
            return DBQuery.is("_id", uuid);
        }
        DBQuery.Query classQuery = getQueryConditions();
        return classQuery.is("version", getVersion());
    }

    @JsonIgnore
    public void setQuery(final DBQuery.Query query) {
        this.query = Optional.ofNullable(query);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(getClass() != obj.getClass())) {
            return false;
        }
        BaseStorableType that = (BaseStorableType) obj;
        return Objects.equal(version, that.version)
                && Objects.equal(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(version, uuid);
    }

    /**
     * Builder base class for StorableTypes.
     *
     * @param <T> Builder of instance type we are
     *            building
     */
    public abstract static class StorableBuilder<T extends StorableBuilder> {
        private static final Logger LOG = LoggerFactory.getLogger(StorableBuilder.class);
        private UUID uuid;
        private String version;

        public StorableBuilder() {
        }

        /**
         * This instance of the builder.
         *
         * @return this
         */
        protected abstract T self();

        /**
         * Builds an instance of the parent object.
         *
         * @param <Y> A BaseStorableType
         * @return Instance with values set in the builder
         */
        protected abstract <Y extends BaseStorableType> Y build();

        /**
         * Builder-style.
         *
         * @param uuid uuid
         * @return this
         */
        @JsonProperty("_id")
        public T uuid(final UUID uuid) {
            this.uuid = uuid;
            return self();
        }

        /**
         * Generates a UUID from a string.
         *
         * @param uuid String version of UUID
         * @return this
         */
        public T uuid(final String uuid) {
            this.uuid = UUID.fromString(uuid);
            return self();
        }

        /**
         * Builder-style.
         *
         * @param version version
         * @return this
         */
        public T version(final String version) {
            this.version = version;
            return self();
        }

        protected UUID getUuid() {
            return uuid;
        }

        protected String getVersion() {
            return version;
        }

        /**
         * Set an error for the instance.
         * @param err error message
         */
        @JsonProperty("$err")
        public void err(final String err) {
            LOG.error("Error is {}", err);
        }
    }
}

