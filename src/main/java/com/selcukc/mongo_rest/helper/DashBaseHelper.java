package com.selcukc.mongo_rest.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.selcukc.mongo_rest.dao.Dao;
import com.selcukc.mongo_rest.dao.MongoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Set;

public abstract class DashBaseHelper {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected static final List<String> CURATION_CATEGORIES = ImmutableList.of("General", "Politics", "Economy",
            "Intersectionality", "Arts & Entertainment", "Science & Technology", "Sport", "Magazine", "Student");

    protected static final Set<String> LOC_KEYS = ImmutableSet.of("City", "Country", "State", "University", "Latitude",
            "Longitude");
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${compassnews.service.oauth.endpoint:https://curate.compassnews.co.uk/}")
    protected String oauthUrl;
    @Value("${compassnews.website.edit.endpoint:https://curate.compassnews.co.uk/admin/edit/}")
    protected String editUrl;
    @Value("${compassnews.website.view.endpoint:https://compassnews.co.uk/article/}")
    protected String viewUrl;

    @Autowired
    protected MongoWrapper mongoWrapper;

    @Autowired
    protected Dao dao;

}
