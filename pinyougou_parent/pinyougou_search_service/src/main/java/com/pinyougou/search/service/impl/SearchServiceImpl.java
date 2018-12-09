package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
    /*    Map map = new HashMap();

        Query query = new SimpleQuery();
        // 添加查询条件
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        map.put("rows", page.getContent());
        return map;*/

            Query query=new SimpleQuery();
            //添加查询条件
            Criteria criteria=
                    new Criteria("item_keywords").is(searchMap.get("keywords"));
            query.addCriteria(criteria);
            ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);
            Map<String,Object> map=new HashMap<>();
            map.put("rows", page.getContent());
            return map;



    }
}
