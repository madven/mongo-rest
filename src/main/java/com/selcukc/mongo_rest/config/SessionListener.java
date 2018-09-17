package com.selcukc.mongo_rest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
    private static final Logger LOG = LoggerFactory.getLogger(SessionListener.class);
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setMaxInactiveInterval(21600);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        LOG.debug("Session destroyed: {} {} {}", event.getSession(), event.toString(), event.getSession().getMaxInactiveInterval());
    }
}