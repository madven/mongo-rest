package com.selcukc.mongo_rest.models.storable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.mongojack.DBQuery;

import java.util.*;

import static com.selcukc.mongo_rest.models.storable.User.UserType.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = User.Builder.class)
@Storable("users")
public class User extends BaseStorableType {

	public enum UserType {
        Active,
        Admin,
        Banned,
        Beta,
        Partial,
        Publisher,
        Student,
        Waiting
    }

    /**
     * The keys for the UserDetails field.
     */
    public enum DetailTypes {
        HASHED_PASSWORD,
        EXTERNAL_PROVIDER,
        USES_FACEBOOK,
        FULL_NAME,
        BIO,
        BIRTHDAY,
        LATEST_SUBMISSION,
        REGISTRATION_TIME,
        LAST_LOGIN_TIME,
        PUBLICATION_NAME,
        PROFILE_IMAGE,
        ALT_IMAGE,
        COVER_IMAGE,
        WRITE_TOKEN,
        RSS_FEED,
        FACEBOOK_PROFILE,
        TWITTER_PROFILE,
        WEBSITE,
        DASHBOARD_CATEGORY,
        PLATFORM,
        REFERRAL_CODE,
        REFERRER,
        SCOPE,
        UNIVERSITY,
        VALIDATION_CODE,
        VALIDATION_TIMEOUT,
        GDPR_OPTED_IN,
        GDPR_OPT_IN_TIME
    }

    /**
     * Personalization filter keys.
     */
    public enum PersonalizationFilterType {
        CATEGORIES,
        FEATURED_CATEGORIES,
        FEATURED_TAGS,
        TAGS
    }

    public static final List<UserType> ACTIVE_TYPES = ImmutableList.of(Active, Beta, Student, Admin);
    public static final List<UserType> LIMITED_TYPES = ImmutableList.of(Partial, Waiting);

    public static final Map<String, List<String>> DEFAULT_FILTERS = ImmutableMap.of(
            PersonalizationFilterType.CATEGORIES.name(), ImmutableList.of(),
            PersonalizationFilterType.FEATURED_CATEGORIES.name(), ImmutableList.of(),
            PersonalizationFilterType.FEATURED_TAGS.name(), ImmutableList.of(),
            PersonalizationFilterType.TAGS.name(), ImmutableList.of());

    private final Optional<String> username;
    private final Map<String, String> userDetails;
    private final Map<String, String> userPreferences;
    private final Map<String, String> userData;
    private final Map<String, String> userLocation;
    private final Map<String, String> subscription;
    private final Map<String, String> articleLocation;
    private final Map<String, List<String>> personalizationFilters;
    private final Optional<String> defaultCategory;
    private final Optional<String> defaultSubCategory;
    private final Optional<UserType> userType;
    private final boolean validated;
    private final boolean isExternal;
    private final boolean isAdmin;

    private User(final UUID userUuid,
                 final String userVersion,
                 final String username,
                 final Map<String, String> userDetails,
                 final Map<String, String> userPreferences,
                 final Map<String, String> userData,
                 final Map<String, String> userLocation,
                 final Map<String, String> subscription,
                 final Map<String, String> articleLocation,
                 final Map<String, List<String>> personalizationFilters,
                 final String defaultCategory,
                 final String defaultSubCategory,
                 final UserType userType,
                 final boolean validated,
                 final boolean isExternal,
                 final boolean isAdmin) {
        super(userUuid, userVersion);
        this.username = Optional.ofNullable(username);
        this.userDetails = userDetails;
        this.userPreferences = userPreferences;
        this.userData = userData;
        this.userLocation = userLocation;
        this.subscription = subscription;
        this.articleLocation = articleLocation;
        this.personalizationFilters = personalizationFilters;
        this.defaultCategory = Optional.ofNullable(defaultCategory);
        this.defaultSubCategory = Optional.ofNullable(defaultSubCategory);
        this.userType = Optional.ofNullable(userType);
        this.validated = validated;
        this.isExternal = isExternal;
        this.isAdmin = isAdmin;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Map<String, String> getUserDetails() {
        return userDetails;
    }

    public Map<String, String> getUserPreferences() {
        return userPreferences;
    }

    public Map<String, String> getUserData() {
        return userData;
    }

    public Map<String, String> getUserLocation() {
        return userLocation;
    }

    public Map<String, String> getSubscription() {
        return subscription;
    }

    public Map<String, String> getArticleLocation() {
        return articleLocation;
    }

    public Map<String, List<String>> getPersonalizationFilters() {
        return personalizationFilters;
    }

    public Optional<String> getDefaultCategory() {
        return defaultCategory;
    }

    public Optional<String> getDefaultSubCategory() {
        return defaultSubCategory;
    }

    public Optional<UserType> getUserType() {
        return userType;
    }

    public boolean isValidated() {
        return validated;
    }

    public boolean isExternal() {
        return isExternal;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    protected DBQuery.Query getQueryConditions() {
        if (username.isPresent()) {
            return DBQuery.is("username", username);
        } else if (userDetails.containsKey(DetailTypes.REFERRAL_CODE.name())) {
            return DBQuery.is("userDetails.REFERRAL_CODE", userDetails.get(DetailTypes.REFERRAL_CODE.name()));
        } else if (userDetails.containsKey(DetailTypes.LATEST_SUBMISSION.name())) {
            if ("$null".equals(userDetails.get(DetailTypes.LATEST_SUBMISSION.name()))) {
                return DBQuery.notExists("userDetails.LATEST_SUBMISSION");
            } else {
                return DBQuery.exists("userDetails.LATEST_SUBMISSION");
            }
        } else if (isAdmin) {
            return DBQuery.is("admin", true);
        } else if (userType.isPresent()) {
            if (userType.get().equals(UserType.Student) && userDetails.containsKey(DetailTypes.UNIVERSITY.name())) {
                return DBQuery.is("userDetails.UNIVERSITY", userDetails.get(DetailTypes.UNIVERSITY.name()))
                        .is("userType", userType);
            } else {
                return DBQuery.is("userType", userType);
            }
        } else {
            return DBQuery.empty();
        }
    }

    public void setReferralCode(final String referralCode) {
        userDetails.put(DetailTypes.REFERRAL_CODE.name(), referralCode);
    }

    /**
     * Set a random UUID-format validation code in the user details.
     */
    public void setValidationCode() {
        final Calendar timeout = Calendar.getInstance();
        if (!userDetails.containsKey(DetailTypes.VALIDATION_CODE.name())
                || timeout.getTimeInMillis() > Long.parseLong(userDetails.getOrDefault(DetailTypes.VALIDATION_TIMEOUT.name(), "0"))) {
            timeout.add(Calendar.HOUR, 3);
            userDetails.put(DetailTypes.VALIDATION_CODE.name(), UUID.randomUUID().toString());
            userDetails.put(DetailTypes.VALIDATION_TIMEOUT.name(), timeout.getTimeInMillis() + "");
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("UUID", getUuid())
                .add("Username", username)
                .add("User Details", userDetails)
                .add("User Preferences", userPreferences)
                .add("User Data", userData)
                .add("User Location", userLocation)
                .add("Subscription", subscription)
                .add("Article Location", articleLocation)
                .add("Personalization Filters", personalizationFilters)
                .add("Default Category", defaultCategory)
                .add("Default SubCategory", defaultSubCategory)
                .add("User Type", userType)
                .add("Validated", validated)
                .add("External", isExternal)
                .add("Admin", isAdmin)
                .add("Version", getVersion())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid(), getVersion(), username, userDetails, userPreferences, userLocation,
                subscription, articleLocation, personalizationFilters, defaultCategory, defaultSubCategory, isExternal,
                validated, isAdmin, userType, userData);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(getClass() != obj.getClass())) {
            return false;
        }

        final User that = (User) obj;

        return super.equals(obj)
                && Objects.equal(getUuid(), that.getUuid())
                && Objects.equal(getVersion(), that.getVersion())
                && Objects.equal(username, that.username)
                && Objects.equal(userDetails, that.userDetails)
                && Objects.equal(userPreferences, that.userPreferences)
                && Objects.equal(userData, that.userData)
                && Objects.equal(userLocation, that.userLocation)
                && Objects.equal(subscription, that.subscription)
                && Objects.equal(articleLocation, that.articleLocation)
                && Objects.equal(personalizationFilters, that.personalizationFilters)
                && Objects.equal(defaultCategory, that.defaultCategory)
                && Objects.equal(defaultSubCategory, that.defaultSubCategory)
                && Objects.equal(userType, that.userType)
                && Objects.equal(isExternal, that.isExternal)
                && Objects.equal(isAdmin, that.isAdmin)
                && validated == that.validated;
    }

    /**
     * Create builder with current values.
     * @return Builder
     */
    public Builder toBuilder() {
        return new Builder()
                .admin(isAdmin)
                .userDetails(userDetails)
                .userPreferences(userPreferences)
                .userData(userData)
                .userLocation(userLocation)
                .subscription(subscription)
                .articleLocation(articleLocation)
                .personalizationFilters(personalizationFilters)
                .defaultCategory(defaultCategory.orElse(""))
                .defaultSubCategory(defaultSubCategory.orElse(""))
                .external(isExternal)
                .userType(userType.orElse(null))
                .username(getUsername().orElse(""))
                .validated(validated)
                .uuid(getUuid().orElse(null))
                .version(getVersion());
    }

    /**
     * Builder.
     */
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends BaseStorableType.StorableBuilder<Builder> {
        private String username;
        private Map<String, String> userDetails = Maps.newHashMap();
        private Map<String, String> userPreferences = Maps.newHashMap();
        private Map<String, String> userData = Maps.newHashMap();
        private Map<String, String> userLocation = Maps.newHashMap();
        private Map<String, String> subscription = Maps.newHashMap();
        private Map<String, String> articleLocation = Maps.newHashMap();
        private Map<String, List<String>> personalizationFilters = Maps.newHashMap();
        private String defaultCategory;
        private String defaultSubCategory;
        private UserType userType;
        private boolean validated = false;
        private boolean isExternal = false;
        private boolean isAdmin = false;

        public Builder() {
            super();
        }

        /**
         * Builder-style.
         *
         * @param username username
         * @return this
         */
        public Builder username(final String username) {
            this.username = username;
            return this;
        }

        /**
         * Builder-style.
         *
         * @param userDetails user details
         * @return this
         */
        public Builder userDetails(final Map<String, String> userDetails) {
            if (MapUtils.isNotEmpty(userDetails)) {
                this.userDetails.putAll(userDetails);
            }
            return this;
        }

        /**
         * Builder-style.
         *
         * @param userPreferences userPreferences
         * @return this
         */
        public Builder userPreferences(final Map<String, String> userPreferences) {
            if (MapUtils.isNotEmpty(userPreferences)) {
                this.userPreferences.putAll(userPreferences);
            }
            return this;
        }


        /**
         * Builder-style.
         *
         * @param userData userData
         * @return this
         */
        public Builder userData(final Map<String, String> userData) {
            if (MapUtils.isNotEmpty(userData)) {
                this.userData.putAll(userData);
            }
            return this;
        }

        /**
         * Builder-style.
         *
         * @param userLocation userLocation
         * @return this
         */
        public Builder userLocation(final Map<String, String> userLocation) {
            if (MapUtils.isNotEmpty(userLocation)) {
                this.userLocation.putAll(userLocation);
            }
            return this;
        }

        /**
         * Builder-style.
         *
         * @param subscription subscription
         * @return this
         */
        public Builder subscription(final Map<String, String> subscription) {
            if (MapUtils.isNotEmpty(subscription)) {
                this.subscription.putAll(subscription);
            }
            return this;
        }

        /**
         * Builder-style.
         *
         * @param articleLocation articleLocation
         * @return this
         */
        public Builder articleLocation(final Map<String, String> articleLocation) {
            if (MapUtils.isNotEmpty(articleLocation)) {
                this.articleLocation.putAll(articleLocation);
            }
            return this;
        }


        /**
         * Builder-style.
         *
         * @param personalizationFilters personalizationFilters
         * @return this
         */
        public Builder personalizationFilters(final Map<String, List<String>> personalizationFilters) {
            if (MapUtils.isNotEmpty(personalizationFilters)) {
                this.personalizationFilters.putAll(personalizationFilters);
            }
            return this;
        }

        /**
         * Builder-style.
         *
         * @param defaultCategory defaultCategory
         * @return this
         */
        public Builder defaultCategory(final String defaultCategory) {
            this.defaultCategory = defaultCategory;
            return this;
        }

        /**
         * Builder-style.
         *
         * @param defaultSubCategory defaultSubCategory
         * @return this
         */
        public Builder defaultSubCategory(final String defaultSubCategory) {
            this.defaultSubCategory = defaultSubCategory;
            return this;
        }

        /**
         * Builder-style.
         *
         * @param validated validated
         * @return Builder instance
         */
        public Builder validated(boolean validated) {
            this.validated = validated;
            return this;
        }

        /**
         * Builder-style.
         *
         * @param external is external?
         * @return this
         */
        public Builder external(final boolean external) {
            this.isExternal = external;
            return this;
        }

        /**
         * Builder-style.
         *
         * @param userType user type
         * @return this
         */
        public Builder userType(final UserType userType) {
            this.userType = userType;
            return this;
        }

        /**
         * Builder-style.
         *
         * @param admin is admin?
         * @return this
         */
        public Builder admin(final boolean admin) {
            this.isAdmin = admin;
            return this;
        }

        /**
         * Builds instance.
         *
         * @return User if valid
         */
        @SuppressWarnings("unchecked")
        @Override
        public User build() {
            return new User(getUuid(), getVersion(), username, userDetails, userPreferences, userData, userLocation,
                    subscription, articleLocation, personalizationFilters, defaultCategory, defaultSubCategory,
                    userType, validated, isExternal, isAdmin);
        }

        @Override
        public User.Builder self() {
            return this;
        }
    }
}

