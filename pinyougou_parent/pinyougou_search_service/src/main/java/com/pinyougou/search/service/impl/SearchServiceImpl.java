package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 按关键字查询
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map searchMap) {

       Map map=new HashMap<>();

        // 1. 查询列表
        map.putAll(searchList(searchMap));
    // 2. 根据关键字查询商品分类
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);


        //3.查询品牌和规格列表
        String category= (String) searchMap.get("category");
        if(categoryList.size()>0){
            map.putAll(searchBrandAndSpecList(categoryList.get(0)));
        }

        return map;
   /*     Map map=new HashMap();
        //1.查询列表
        map.putAll(searchList(searchMap));
        //2.分组查询 商品分类列表
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList", categoryList);

        //3.查询品牌和规格列表
        String category= (String) searchMap.get("category");

            if(categoryList.size()>0){
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }


        return map;*/

    }

    /**
     * 查询分类列表数据
     * @param searchMap
     * @return
     */
    private List searchCategoryList(Map searchMap){
        List list = new ArrayList<>();
        Query query = new SimpleQuery();
        // 按照关键字查询
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
       query.addCriteria(criteria);
        // 设置分组选项
        GroupOptions groupOptions= new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        // 得到分组页
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);
        // 根据列得到分组结果集
        GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
        // 得到分页结果入口
        Page<GroupEntry<TbItem>> entries = groupResult.getGroupEntries();
        // 得到分组入口的集合
        List<GroupEntry<TbItem>> content = entries.getContent();
        for(GroupEntry<TbItem> entry:content){
            // 分组结果的名称放入list集合
            list.add(entry.getGroupValue());
        }
        return list;
    }

    /**
     * 根据关键字搜索列表
     * @param searchMap
     * @return
     */
    private Map searchList(Map searchMap){
        Map map=new HashMap<>();
        SimpleHighlightQuery query = new SimpleHighlightQuery();
        // 设置高亮域
        HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");
        // 设置高亮前缀
        highlightOptions.setSimplePrefix("<em style='color:red'>");
        // 设置高亮后缀
        highlightOptions.setSimplePostfix("</em>");
        // 设置高亮选项
        query.setHighlightOptions(highlightOptions);

        //添加查询条件
        Criteria criteria=
                new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
        //遍历page
        for (HighlightEntry<TbItem> h: page.getHighlighted()){//高亮入口集合

            TbItem tbItem = h.getEntity();// 获取源实体类
            if (h.getHighlights().size()>0&&h.getHighlights().get(0).getSnipplets().size()>0){
                // 设置高亮的结果
                tbItem.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
            }
        }

        map.put("rows", page.getContent());
        return map;

    }

    /**
     查询品牌和规格列表
     @param category 分类名称
     @return
     */
    private Map searchBrandAndSpecList(String category){
        Map map=new HashMap();
        Long templateId = (Long) redisTemplate.boundHashOps("itemCat").get(category);// 获取模板ID
        if(templateId!=null){
            //2.根据模板ID获取品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(templateId);
            map.put("brandList", brandList);
            System.out.println("品牌列表条数："+brandList.size());

            //3.根据模板ID获取规格列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(templateId);
            map.put("specList", specList);
            System.out.println("规格列表条数："+specList.size());

        }

        return map;
     }

}
