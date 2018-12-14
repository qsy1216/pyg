package com.pinyougou.solrutil;


import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;
import com.pinyougou.pojo.TbItemExample.Criteria;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    // 把数据放入索引库
    public void importItemData(){

        TbItemExample example = new TbItemExample();
        Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");//审核通过的才导入的
        List<TbItem> items = itemMapper.selectByExample(example);

        for(TbItem item:items){
            System.out.println(item.getId()+" "+ item.getTitle()+ " "+item.getPrice());
            Map specMap = JSON.parseObject(item.getSpec(), Map.class);//从数据库中提取规格json字符串转换为map
            item.setSpecMap(specMap);
        }

        solrTemplate.saveBeans(items);
        solrTemplate.commit();

    }
    public static void main(String[] args) {

        ApplicationContext context=new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil solrUtil=  (SolrUtil) context.getBean("solrUtil");
        solrUtil.importItemData();

    }


}
