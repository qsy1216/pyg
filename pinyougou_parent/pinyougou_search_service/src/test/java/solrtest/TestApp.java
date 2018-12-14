package solrtest;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

public class TestApp {

    //slor原生代码  ，适合查询
    @Test
    public void test1() throws SolrServerException {
       //连接solr服务
        HttpSolrServer httpSolrServer = new HttpSolrServer("http://127.0.0.1:8080/solr");
        //查询响应
        SolrQuery query = new SolrQuery("item_title:手机");
        //过滤
//        query.addFilterQuery("")
        query.setRows(5);//设置每页多少条
        QueryResponse queryResponse = httpSolrServer.query(query);
        SolrDocumentList results = queryResponse.getResults();
        for (SolrDocument document : results) {
            Object item_title = document.get("item_title");
            System.out.println(item_title);
        }
    }
}
