package com.selcukc.mongo_rest.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.selcukc.mongo_rest.helper.AdminHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Calendar;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${compassnews.service.user.endpoint}")
    private String userServiceLocation;
    @Value("${compassnews.service.oauth.endpoint:https://curate.compassnews.co.uk/}")
    private String oauthServiceLocation;
    @Value("${compassnews.service.content.endpoint}")
    private String contentServiceLocation;

    @Autowired
    private AdminHelper adminHelper;

    @RequestMapping("/")
    public String home() {
        return "redirect:" + oauthServiceLocation + "/admin/dashboard";
    }

    @RequestMapping("/dashboard")
    public String adminDashboard(@RequestParam(value = "type", required = false) final String type,
                                 @RequestParam(value = "uni", required = false) final String uni,
                                 @RequestParam(value = "page", required = false) final Integer page,
                                 @RequestParam(value = "facebook", required = false) final Boolean facebook,
                                 @RequestParam(value = "validated", required = false) final Boolean validated,
                                 @RequestParam(value = "subscribed", required = false) final String subscribed,
                                 @RequestParam(value = "paying", required = false) final Boolean paying,
                                 @RequestParam(value = "before", required = false) final Long before,
                                 @RequestParam(value = "after", required = false) final Long after,
                                 @RequestParam(value = "platform", required = false) final String platform,
                                 @RequestParam(value = "email", required = false) final String email,
                                 @RequestParam(value = "geo", required = false) final String geoSearch,
                                 final Model model, final Principal principal) {
        if (type == null) {
            model.addAttribute("type", "Publisher");
        } else {
            model.addAttribute("type", type);
        }
        if (!Strings.isNullOrEmpty(uni)) {
            model.addAttribute("uni", uni);
        }
        if (page != null) {
            model.addAttribute("page", page);
        } else {
            model.addAttribute("page", "0");
        }
        if (!Strings.isNullOrEmpty(email)) {
            model.addAttribute("email", email);
        }
        if (facebook != null) {
            model.addAttribute("facebook", facebook);
        }
        if (validated != null) {
            model.addAttribute("validated", validated);
        }
        if (subscribed != null) {
            model.addAttribute("subscribed", subscribed);
        } else {
            model.addAttribute("subscribed", "all");
        }
        if (paying != null) {
            model.addAttribute("paying", paying);
        }
        final String tp = model.asMap().get("type").toString();
        if (before == null && after == null && (tp.equals("Approved") || tp.equals("Beta") || tp.equals("Student"))) {
            final Calendar oneDay = Calendar.getInstance();
            oneDay.add(Calendar.DATE, -1);
            model.addAttribute("after", oneDay.getTimeInMillis());
        } else {
            if (before != null) {
                model.addAttribute("before", before);
            }
            if (after != null) {
                model.addAttribute("after", after);
            }
        }
        if (!Strings.isNullOrEmpty(platform)) {
            model.addAttribute("platform", platform);
        }
        if (!Strings.isNullOrEmpty(geoSearch)) {
            model.addAttribute("geo", geoSearch);
        }

        LOG.debug("{} accessed admin dashboard", principal.getName());
        addCommonDashboardParams(model, principal, "admin");

        adminHelper.populateAdminDashboard(model);
        return "layouts/dashboard";
    }

    private void addCommonDashboardParams(final Model model, final Principal principal, final String dType) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("dashboardType", dType);
        model.addAttribute("contentServiceLocation", contentServiceLocation);
        model.addAttribute("userServiceLocation", userServiceLocation);
    }
}
