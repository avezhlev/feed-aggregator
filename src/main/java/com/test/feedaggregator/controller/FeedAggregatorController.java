package com.test.feedaggregator.controller;

import com.test.feedaggregator.service.FeedEntriesService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class FeedAggregatorController {

    private static final String ATTR_NAME_FILTER = "filter";

    private final FeedEntriesService service;

    @Autowired
    public FeedAggregatorController(FeedEntriesService service) {
        this.service = service;
    }

    @InitBinder(ATTR_NAME_FILTER)
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(String.class, "title", new StringTrimmerEditor(true));
    }

    @GetMapping
    public String getFeedEntries(Model model, @ModelAttribute(ATTR_NAME_FILTER) Filter filter) {
        model.addAttribute("entries", service.getFeedEntries(filter != null ? filter.getTitle() : null));
        return "index";
    }

    @PostMapping
    public String postFeedEntriesFilter(Model model, @ModelAttribute(ATTR_NAME_FILTER) Filter filter) {
        return getFeedEntries(model, filter);
    }

    @Setter
    @Getter
    public static class Filter {
        private String title;
    }
}
