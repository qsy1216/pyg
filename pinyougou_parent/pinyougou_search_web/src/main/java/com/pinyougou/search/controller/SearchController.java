package com.pinyougou.search.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemSearch")
public class SearchController {

    @Reference(timeout = 5000)
    private SearchService searchService;

    @RequestMapping("/search")
    public Map search(@RequestBody Map<String, Object> searchMap) {

        return searchService.search(searchMap);
    }


}
