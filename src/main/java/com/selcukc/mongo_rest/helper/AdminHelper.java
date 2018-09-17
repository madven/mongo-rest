package com.selcukc.mongo_rest.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.selcukc.mongo_rest.repositories.UserStats;
import com.selcukc.mongo_rest.repositories.storable.User;
import com.selcukc.mongo_rest.repositories.storable.User.DetailTypes;
import org.joda.time.DateTime;
import org.mongojack.DBQuery;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.selcukc.mongo_rest.repositories.storable.User.DetailTypes.LATEST_SUBMISSION;

public class AdminHelper extends DashBaseHelper {
    private static final int MAX_RESULTS = 200;
    private static final Map<String, String> PUBLISHER_SEEDS_MAP = ImmutableMap.of(
            "FT", "https://www.ft.com/",
            "Times", "http://www.thetimes.co.uk/",
            "WSJ", "https://www.wsj.com/",
            "NYT", "https://www.nytimes.com/"
    );

    protected static final long STALE_PUBLISHER = 172800000L;
    protected enum STALE_STATES {
        NOT_STALE (0),
        STALE_RSS (1),
        STALE_REGULAR (2),
        STALE_NA (3);

        private int state;

        STALE_STATES(final int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    };


    /**
     * Fills in the model with the required data for the admin dashboard.
     * @param model The model for the admin dashboard request
     */
    public void populateAdminDashboard(final Model model) {
        final Map<String, Object> filters = model.asMap();
        final String type = filters.get("type").toString();
        final boolean emailSearch = filters.containsKey("email");

        final DBQuery.Query searchParams = DBQuery.empty();
        if (emailSearch) {
            searchParams.is("username", Optional.of(filters.get("email")));
        } else {
            if (type.equals("Approved")) {
                searchParams.in("userType",
                        Optional.of(User.UserType.Beta), Optional.of(User.UserType.Student), Optional.of(User.UserType.Active), Optional.of(User.UserType.Partial));

            } else if (type.equals("Unknown")) {
                searchParams.or(DBQuery.is("userType", null), DBQuery.notExists("userType"));
            } else {
                searchParams.is("userType", Optional.of(User.UserType.valueOf(type)));
                if (type.equals("Student") && filters.containsKey("uni")) {
                    final String uniFilter = String.format(".*%s.*", filters.get("uni").toString().toLowerCase().replaceAll("\\.", "\\\\\\\\."));
                    searchParams.regex("userDetails.UNIVERSITY", Pattern.compile(uniFilter));
                }
            }
            if (filters.containsKey("facebook")) {
                if (Boolean.valueOf(filters.get("facebook").toString())) {
                    searchParams.is("userDetails.USES_FACEBOOK", "true");
                } else {
                    searchParams.is("userDetails.USES_FACEBOOK", null);
                }
            }
            if (filters.containsKey("validated")) {
                searchParams.is("validated", Boolean.valueOf(filters.get("validated").toString()));
            }
            if (filters.containsKey("subscribed") || filters.containsKey("paying")) {
                final boolean paying = Boolean.valueOf(filters.getOrDefault("paying", "false").toString());
                if (Boolean.valueOf(filters.getOrDefault("subscribed", "false").toString()) || paying) {
                    searchParams.greaterThan("subscription.validity", new Date().getTime() + "");
                    if (paying) {
                        searchParams.is("subscription.paymentActive", "true");
                    }
                } else if ("unsub".equals(filters.get("subscribed"))) {
                    searchParams.exists("subscription.suspendDate");
                } else if ("false".equals(filters.get("subscribed"))) {
                    searchParams.or(DBQuery.lessThan("subscription.validity", new Date().getTime() + ""),
                            DBQuery.notExists("subscription"),
                            DBQuery.notExists("subscription.validity"));
                }
            }
            if (filters.containsKey("before")) {
                searchParams.lessThan("userDetails.REGISTRATION_TIME", filters.get("before").toString());
            }
            if (filters.containsKey("after")) {
                searchParams.greaterThan("userDetails.REGISTRATION_TIME", filters.get("after").toString());
            }
            if (filters.containsKey("platform")) {
                final String platform = filters.get("platform").toString();
                searchParams.or(DBQuery.is("subscription.platform", filters.get("platform").toString()),
                        DBQuery.is("userDetails.PLATFORM", "WEB".equals(platform) ? "DESKTOP" : platform));
            }

            if (filters.containsKey("geo")) {
                final String geoType = filters.get("geo").toString().split(":")[0];
                final String geoQuery = filters.get("geo").toString().split(":")[1];
                if (geoType.equalsIgnoreCase("city")) {
                    searchParams.is("userLocation.locality", geoQuery);
                }
            }
        }

        final int page;
        if (filters.containsKey("page")) {
            page = Integer.valueOf(filters.get("page").toString());
        } else {
            page = 0;
        }

        final List<User> allUsers = mongoWrapper.getCollection(User.class).find(searchParams).limit(MAX_RESULTS).skip(page * MAX_RESULTS)
                .sort(new BasicDBObject("userDetails.REGISTRATION_TIME", -1)).toArray();

        final long currentTime = new Date().getTime();
        final Map<String, UserStats> userStatsMap = Maps.newLinkedHashMap();
        allUsers.forEach((user) -> {
                final UserStats info = new UserStats();
                info.setId(user.getUuid().get().toString());
                info.setUsername(user.getUsername().get());
                if (user.getUserDetails().containsKey(DetailTypes.REGISTRATION_TIME.name())) {
                    info.setRegistrationTime(user.getUserDetails().get(DetailTypes.REGISTRATION_TIME.name()));
                } else {
                    info.setRegistrationTime("N/A");
                }
                info.setRssFeed(user.getUserDetails().get(DetailTypes.RSS_FEED.name()));
                if (user.getUserDetails().containsKey(LATEST_SUBMISSION.name())) {
                    final long latestSubmission = Long.valueOf(user.getUserDetails()
                            .get(LATEST_SUBMISSION.name()));
                    if (currentTime - latestSubmission > STALE_PUBLISHER) {
                        if (Strings.isNullOrEmpty(info.getRssFeed())) {
                            info.setStaleState(STALE_STATES.STALE_REGULAR.getState());
                        } else {
                            info.setStaleState(STALE_STATES.STALE_RSS.getState());
                        }
                    } else {
                        info.setStaleState(STALE_STATES.NOT_STALE.getState());
                    }
                    info.setLatestSubmission(new DateTime(latestSubmission).toString("dd-MMM-YYYY kk:mm"));
                } else {
                    info.setLatestSubmission("N/A");
                    info.setStaleState(STALE_STATES.STALE_NA.getState());
                }
                info.setFullName(user.getUserDetails().get(DetailTypes.FULL_NAME.name()));
                info.setPublicationName(user.getUserDetails().get(DetailTypes.PUBLICATION_NAME.name()));
                info.setBio(user.getUserDetails().get(DetailTypes.BIO.name()));
                info.setFacebook(user.getUserDetails().get(DetailTypes.FACEBOOK_PROFILE.name()));
                info.setTwitter(user.getUserDetails().get(DetailTypes.TWITTER_PROFILE.name()));
                info.setWebsite(user.getUserDetails().get(DetailTypes.WEBSITE.name()));
                info.setDashboardCategory(user.getUserDetails().get(DetailTypes.DASHBOARD_CATEGORY.name()));
                info.setProfileImage(user.getUserDetails().get(DetailTypes.PROFILE_IMAGE.name()));
                info.setAltImage(user.getUserDetails().get(DetailTypes.ALT_IMAGE.name()));
                info.setCoverImage(user.getUserDetails().get(DetailTypes.COVER_IMAGE.name()));
                info.setScope(user.getUserDetails().get(DetailTypes.SCOPE.name()));
                user.getUserType().ifPresent(ut -> info.setUserType(ut.name()));
                info.setPlatform(user.getUserDetails().get(DetailTypes.PLATFORM.name()));
                info.setValidated(user.isValidated());
                info.setReferralCode(user.getUserDetails().get(DetailTypes.REFERRAL_CODE.name()));
                info.setReferrer(user.getUserDetails().get(DetailTypes.REFERRER.name()));
                info.setProvider(user.getUserDetails().get(DetailTypes.EXTERNAL_PROVIDER.name()));
                info.setValidationCode(user.getUserDetails().get(DetailTypes.VALIDATION_CODE.name()));
                info.setNotifToken(user.getUserData().getOrDefault("notifToken", ""));
                info.setDiscover(Boolean.valueOf(user.getUserDetails().getOrDefault("Discover", "false")));
                info.setDiscoverImage(user.getUserDetails().get("DiscoverImage"));
                info.setDiscoverDisplayName(user.getUserDetails().get("DiscoverDisplayName"));
                info.setPaywalled(Boolean.valueOf(user.getUserDetails().getOrDefault("Paywalled", "false")));
                info.setAutogen(Boolean.valueOf(user.getUserDetails().getOrDefault("Autogen", "false")));
                info.setSubscription(user.getSubscription());

                try {
                    info.setUserLocation(MAPPER.writeValueAsString(user.getUserLocation()));
                    info.setArticleLocation(MAPPER.writeValueAsString(user.getArticleLocation()));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                info.setCategory(user.getDefaultCategory().orElse(""));
                info.setSubCategory(user.getDefaultSubCategory().orElse(""));
                userStatsMap.put(info.getUsername(), info);
            }
        );

        if (emailSearch && !allUsers.isEmpty()) {
            final User.UserType uType = allUsers.get(0).getUserType().get();
            if (uType.equals(User.UserType.Publisher)) {
                model.addAttribute("type", uType.toString());
            } else {
                model.addAttribute("type", "Approved");
            }
        } else if ("Publisher".equals(type)) {
//            final BasicDBList docCounts = mongoWrapper.group(Document.class,
//                    new BasicDBObject("createdBy", "1"), // Group by createdBy
//                    new BasicDBObject(), // No condition
//                    new BasicDBObject("numPosts", 0), // Initialize numPosts to 0 for each user
//                    "function(obj, res) { res.numPosts += 1; }", // reduce function
//                    null); // No finalize function
//            for (final Object d : docCounts) {
//                final BasicDBObject doc = (BasicDBObject) d;
//                final String author = doc.get("createdBy").toString();
//                final int count = Double.valueOf(doc.get("numPosts").toString()).intValue();
//                if (userStatsMap.containsKey(author)) {
//                    userStatsMap.get(author).setNumPosts(count);
//                }
//            }
        }

        model.addAttribute("currentUsers", mongoWrapper.getCollection(User.class).find(searchParams).count());
        model.addAttribute("totalUsers", dao.count(new User.Builder().build()) + 20000);
        model.addAttribute("userInfo", userStatsMap.values());
        model.addAttribute("dashboardCategories", CURATION_CATEGORIES);
        model.addAttribute("healthUrl", oauthUrl + "health");

        final boolean isRssActive = Boolean.valueOf(mongoWrapper.getDb().getCollection("ssettings")
                .findOne(new BasicDBObject("_id", 1)).get("rssActive").toString());
        model.addAttribute("isRssActive", isRssActive);
    }

}
