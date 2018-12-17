package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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


        // 搜索关键字空格处理
        String keywords = (String) searchMap.get("keywords");
        searchMap.put("keywords", keywords.replace(" ", ""));


        Map map=new HashMap<>();

        // 1. 查询列表
        map.putAll(searchList(searchMap));
        // 2. 根据关键字查询商品分类
        List<String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);

        //3.查询品牌和规格列表
        String category= (String) searchMap.get("category");
        if(!category.equals("")){
            map.putAll(searchBrandAndSpecList(category));
        }else{
            if(categoryList.size()>0){
                map.putAll(searchBrandAndSpecList(categoryList.get(0)));
            }
        }

        return map;

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




        // 1.1添加查询条件
        Criteria criteria= new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        // 1.2 按照商品分类过滤
        if(!"".equals(searchMap.get("category"))){
            Criteria filterCriteria=new  Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        // 1.3 按照品牌分类过滤
        if (!"".equals(searchMap.get("brand"))) {
            Criteria filterCriteria = new Criteria("item_brand").is(searchMap.get("brand"));

            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        // 1.4 按照规格分类过滤
        if(searchMap.get("spec")!=null){
            Map<String,String> specMap= (Map) searchMap.get("spec");
            for(String key:specMap.keySet() ){
                Criteria filterCriteria=new Criteria("item_spec_"+key).is( specMap.get(key) );
                FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }

        // 1.5 价格分类
        if (!"".equals(searchMap.get("price"))) {
            Criteria filterCriteria = new Criteria("item_price");
            String price = searchMap.get("price").toString();
            String[] split = price.split("-");
            if("*".equals(split[1])) { filterCriteria.greaterThanEqual(split[0]);
            }else {
                filterCriteria.between(split[0], split[1], true, true);
            }
            FilterQuery filterQuery = new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }

        // 1.6 分页查询
        // 获取前台传递到页码
        Integer pageNo = (Integer) searchMap.get("pageNo");
        if (pageNo==null){
            pageNo=1;// 如果页面没有传值过来，设置默认第一页
        }
        // 获取每页显示的记录数
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if(pageSize==null){
            pageSize=30;// 设置默认每页记录数
        }
        // 设置起始索引
        query.setOffset((pageNo-1)*pageSize);
        // 设置每页记录数
        query.setRows(pageSize);


        // 1.7 升序降序
        String sortValue = (String) searchMap.get("sort");// 升序是ASC 降序是DESC
        String sortField = (String) searchMap.get("sortField");// 排序字段
        // 判断页面是否传值过来
        if(sortValue!=null&&!"".equals(sortValue)){

            if ("ASC".equals(sortValue)){
                Sort sort = new Sort(Sort.Direction.ASC, "item_"+sortField);
                query.addSort(sort);
            }
            if ("DESC".equals(sortValue)){
                Sort sort = new Sort(Sort.Direction.DESC, "item_"+sortField);
                query.addSort(sort);
            }
        }



//***********  获取高亮结果集  ***********
        // 高亮页集合
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

        map.put("totalPages",page.getTotalPages());// 总页数
        map.put("total",page.getTotalElements());// 总条数


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


    @Override
    public void importList(List list) {
        solrTemplate.saveBeans(list);
        solrTemplate.commit();
    }

    @Override
    public void deleteByGoodsIds(List goodsIds) {
        Query query=new SimpleQuery("*:*");
        Criteria criteria=new Criteria("item_goodsid").in(goodsIds);
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }


}
