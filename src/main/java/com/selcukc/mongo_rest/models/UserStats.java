package com.selcukc.mongo_rest.models;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Map;

/**
 * @author tinks
 */
public class UserStats {
    private String id;
    private String username;
    private int numPosts;
    private String registrationTime;
    private String fullName;
    private String publicationName;
    private String bio;
    private String facebook;
    private String twitter;
    private String website;
    private String dashboardCategory;
    private String rssFeed;
    private String profileImage;
    private String altImage;
    private String coverImage;
    private String userLocation;
    private String articleLocation;
    private String category;
    private String subCategory;
    private String latestSubmission;
    private int staleState;
    private boolean newPlugin;
    private String scope;
    private String userType;
    private String platform;
    private boolean validated;
    private String provider;
    private String referralCode;
    private String referrer;
    private String validationCode;
    private String notifToken;
    private boolean discover;
    private String discoverImage;
    private String discoverDisplayName;
    private boolean paywalled;
    private boolean autogen;
    private Map<String, String> subscription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(int numPosts) {
        this.numPosts = numPosts;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDashboardCategory() {
        return dashboardCategory;
    }

    public void setDashboardCategory(String dashboardCategory) {
        this.dashboardCategory = dashboardCategory;
    }

    public String getRssFeed() {
        return rssFeed;
    }

    public void setRssFeed(String rssFeed) {
        this.rssFeed = rssFeed;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAltImage() {
        return altImage;
    }

    public void setAltImage(final String altImage) {
        this.altImage = altImage;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public UserStats setCoverImage(String coverImage) {
        this.coverImage = coverImage;
        return this;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getArticleLocation() {
        return articleLocation;
    }

    public void setArticleLocation(String articleLocation) {
        this.articleLocation = articleLocation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getLatestSubmission() {
        return latestSubmission;
    }

    public void setLatestSubmission(String latestSubmission) {
        this.latestSubmission = latestSubmission;
    }

    public int getStaleState() {
        return staleState;
    }

    public void setStaleState(int staleState) {
        this.staleState = staleState;
    }

    public boolean isNewPlugin() {
        return newPlugin;
    }

    public UserStats setNewPlugin(boolean newPlugin) {
        this.newPlugin = newPlugin;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(final String provider) {
        this.provider = provider;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(final String referralCode) {
        this.referralCode = referralCode;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(final String referrer) {
        this.referrer = referrer;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(final String validationCode) {
        this.validationCode = validationCode;
    }

    public String getNotifToken() {
        return notifToken;
    }

    public void setNotifToken(final String notifToken) {
        this.notifToken = notifToken;
    }

    public boolean isDiscover() {
        return discover;
    }

    public void setDiscover(final boolean discover) {
        this.discover = discover;
    }

    public String getDiscoverImage() {
        return discoverImage;
    }

    public void setDiscoverImage(final String discoverImage) {
        this.discoverImage = discoverImage;
    }

    public String getDiscoverDisplayName() {
        return discoverDisplayName;
    }

    public void setDiscoverDisplayName(final String discoverDisplayName) {
        this.discoverDisplayName = discoverDisplayName;
    }

    public boolean isPaywalled() {
        return paywalled;
    }

    public void setPaywalled(final boolean paywalled) {
        this.paywalled = paywalled;
    }

    public boolean isAutogen() {
        return autogen;
    }

    public void setAutogen(final boolean autogen) {
        this.autogen = autogen;
    }

    public Map<String, String> getSubscription() {
        return subscription;
    }

    public void setSubscription(final Map<String, String> subscription) {
        this.subscription = subscription;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("numPosts", numPosts)
                .add("registrationTime", registrationTime)
                .add("fullName", fullName)
                .add("publicationName", publicationName)
                .add("bio", bio)
                .add("facebook", facebook)
                .add("twitter", twitter)
                .add("website", website)
                .add("dashboardCategory", dashboardCategory)
                .add("rssFeed", rssFeed)
                .add("profileImage", profileImage)
                .add("altImage", altImage)
                .add("coverImage", coverImage)
                .add("userLocation", userLocation)
                .add("articleLocation", articleLocation)
                .add("category", category)
                .add("subCategory", subCategory)
                .add("latestSubmission", latestSubmission)
                .add("staleState", staleState)
                .add("newPlugin", newPlugin)
                .add("scope", scope)
                .add("userType", userType)
                .add("platform", platform)
                .add("validated", validated)
                .add("referralCode", referralCode)
                .add("referrer", referrer)
                .add("provider", provider)
                .add("valCode", validationCode)
                .add("discover", discover)
                .add("discoverImage", discoverImage)
                .add("discoverDisplayName", discoverDisplayName)
                .add("paywalled", paywalled)
                .add("autogen", autogen)
                .add("subscription", subscription)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UserStats)) return false;
        UserStats userStats = (UserStats) obj;
        return numPosts == userStats.numPosts
                && Objects.equal(id, userStats.id)
                && Objects.equal(username, userStats.username)
                && Objects.equal(registrationTime, userStats.registrationTime)
                && Objects.equal(fullName, userStats.fullName)
                && Objects.equal(publicationName, userStats.publicationName)
                && Objects.equal(bio, userStats.bio)
                && Objects.equal(facebook, userStats.facebook)
                && Objects.equal(twitter, userStats.twitter)
                && Objects.equal(website, userStats.website)
                && Objects.equal(dashboardCategory, userStats.dashboardCategory)
                && Objects.equal(rssFeed, userStats.rssFeed)
                && Objects.equal(profileImage, userStats.profileImage)
                && Objects.equal(altImage, userStats.altImage)
                && Objects.equal(coverImage, userStats.coverImage)
                && Objects.equal(userLocation, userStats.userLocation)
                && Objects.equal(articleLocation, userStats.articleLocation)
                && Objects.equal(category, userStats.category)
                && Objects.equal(subCategory, userStats.subCategory)
                && Objects.equal(latestSubmission, userStats.latestSubmission)
                && Objects.equal(staleState, userStats.staleState)
                && Objects.equal(scope, userStats.scope)
                && Objects.equal(userType, userStats.userType)
                && Objects.equal(platform, userStats.platform)
                && Objects.equal(referralCode, userStats.referralCode)
                && Objects.equal(referrer, userStats.referralCode)
                && Objects.equal(provider, userStats.provider)
                && newPlugin == userStats.newPlugin
                && validated == userStats.validated
                && paywalled == userStats.paywalled
                && autogen == userStats.autogen
                && discover == userStats.discover
                && Objects.equal(discoverImage, userStats.discoverImage)
                && Objects.equal(discoverDisplayName, userStats.discoverDisplayName)
                && Objects.equal(validationCode, userStats.validationCode)
                && Objects.equal(subscription, userStats.subscription);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username, numPosts, registrationTime, fullName, publicationName, bio, facebook, twitter,
                website, dashboardCategory, rssFeed, profileImage, altImage, coverImage, userLocation, articleLocation, category,
                subCategory, latestSubmission, staleState, newPlugin, scope, userType, platform, id, validated, subscription,
                referralCode, referrer, provider, validationCode, paywalled, autogen, discover, discoverImage, discoverDisplayName);
    }
}
