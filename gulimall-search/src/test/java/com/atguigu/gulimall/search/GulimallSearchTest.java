package com.atguigu.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.atguigu.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregator;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallSearchTest {
    @Data
    private class User{
        private String username;
        private String gender;
        private Integer age;
    }

    @Data
    @ToString
    private static class Account{
        private String firstname;
        private String lastname;
        private Long age;
        private String gender;
        private String email;
        private String city;
        private String address;
        private Long balance;
        private String employee;
        private String state;
    }

    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Test
    public void test() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void testIndexRequest() throws IOException {
        IndexRequest request = new IndexRequest("users");
        request.id("1");
        User user = new User();
        user.setUsername("zhangsan");
        user.setAge(33);
        user.setGender("男");
        String s = JSON.toJSONString(user);
        request.source(s, XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request, GulimallElasticSearchConfig.COMMON_OPTIONS);
    }

    @Test
    public void testSearchApi() throws IOException{
        // 1.创建检索请求
        SearchRequest request = new SearchRequest("newbank");
        // 2.拼接检索条件
        // 2.2 检索所有地址中包含mill的记录
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        // 2.1 按照年龄分组聚合, 并计算每个年龄的平均薪资
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("ageAgg").field("age").subAggregation(AggregationBuilders.avg("avgBalance").field("balance"));
//        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("ageAgg").field("age");
//        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avgBalance").field("balance");

        searchSourceBuilder.aggregation(aggregation);

        System.out.println("检索条件: " + searchSourceBuilder);

        request.source(searchSourceBuilder);

        // 3.执行检索
        SearchResponse searchResponse = restHighLevelClient.search(request, GulimallElasticSearchConfig.COMMON_OPTIONS);
        // 4.查看检索结果
        SearchHits hits = searchResponse.getHits();
        System.out.println("查询的记录数: " + hits.getTotalHits().value);
        SearchHit[] hitsHits = hits.getHits();
        List<Account> list = new ArrayList<>();
        for (SearchHit hitsHit : hitsHits) {
            String index = hitsHit.getIndex();
            String id = hitsHit.getId();
            String sourceAsString = hitsHit.getSourceAsString();
            Account account = JSON.parseObject(sourceAsString, Account.class);
            list.add(account);
        }
        list.forEach(System.out::println);
        // 4.1 查看聚合结果
        Terms ageAgg = searchResponse.getAggregations().get("ageAgg");
        ageAgg.getBuckets().forEach(item -> {
            String key = item.getKeyAsString();
            long docCount = item.getDocCount();
            Avg avgBalance = item.getAggregations().get("avgBalance");
            String balance = avgBalance.getValueAsString();
            System.out.println("key: " + key + ", count: " + docCount + ", avgBalance: " + balance);
        });
    }
}

