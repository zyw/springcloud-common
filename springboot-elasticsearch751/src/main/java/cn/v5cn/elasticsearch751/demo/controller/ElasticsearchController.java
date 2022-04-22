package cn.v5cn.elasticsearch751.demo.controller;

import cn.v5cn.elasticsearch751.demo.entity.SecurityEvaluationElasticsearchVo;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ElasticsearchController {

    private static final String INDEX = "security-evaluation-v2";
    private static int i = 1;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @GetMapping("/insert")
    public Object insertElastic() throws IOException {

        SecurityEvaluationElasticsearchVo see = new SecurityEvaluationElasticsearchVo();
        see.setId(System.currentTimeMillis());
        if((i % 3) == 1) {
            see.setType(1);
            see.setStatus("完成，获得3学分");
        } else if((i % 3) == 2) {
            see.setType(2);
        } else if((i % 3) == 0){
            see.setType(3);
        }
        i++;
        see.setTitle("标题" + i);
        see.setName("张三" + i);
        see.setContent("拼音又叫汉语拼音（Hànyǔ Pīnyīn；英语：Chinese Pinyin、Hanyu Pinyin或Chinese Phonetic Alphabets）简称拼音（Pinyin），是中华人民共和国的汉字注音拉丁化方案，于1955年—1957年文字改革时被原中国文字改革委员会（现国家语言文字工作委员会）汉语拼音方案委员会研究制定。该拼音方案主要用于汉语普通话读音的标注，作为汉字的一种普通话音标。2012年10月1日，新汉语拼音正词法规则正式实施。\n" +
                "\n" +
                "我们天生就是爱偷懒的，那个方便就用那个，比如输入法，早期的输入法非常难用，不管是全拼还是五笔都需要输入敲很多键才能得到我们需要的文本，五笔相对来说更加准确但是需要背字根，不过随着拼音教育的普及，以及拼音输入法的更加智能，拼音输入法后来居上，慢慢变成了主流输入法，我相信现在的年轻人应该很少会用五笔输入法了吧。\n" +
                "\n" +
                "在一个搜索框里面，用户常常可能会因为打错字而输入成拼音字符或者只是会为了方便而输入拼音首字母缩写，因为这样打字最快，如果你也能够很好的支持这样的查询，那将会给用户带来意外的惊喜体验。");
        see.setFileId("1123455_" + i);
        see.setCreateTime("2020-01-20 14:04:34");


        IndexRequest indexRequest = new IndexRequest(INDEX);
        indexRequest.source(JSONObject.toJSONString(see),XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        return indexRequest;
    }

    @GetMapping("/index/{type}/{search}")
    public Object getIndex(@PathVariable("type") String type,@PathVariable("search") String search) throws IOException {
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        if(!("-1".equals(type))) {
            //使用两种方式查询固定类型的数据，must和filter,两者区别是filter不参与算分，性能更好
//            bqb.must(QueryBuilders.matchQuery("type",type));
            bqb.filter(QueryBuilders.termQuery("type",type));
        }
        //通过should方式查询
//        bqb.should(QueryBuilders.matchQuery("title",search));
//        bqb.should(QueryBuilders.matchQuery("name",search));
//        bqb.should(QueryBuilders.matchQuery("content",search));
//        bqb.should(QueryBuilders.matchQuery("title.pinyin",search));
//        bqb.should(QueryBuilders.matchQuery("name.pinyin",search));

        bqb.must(QueryBuilders.multiMatchQuery(search,"title","name","content","title.pinyin","name.pinyin"));

        SearchSourceBuilder ssb = SearchSourceBuilder
                .searchSource()
                .query(bqb)
                //返回的查询结果中包含的字段，第一个数组是包含哪些字段，第二个数组是排除哪些字段
                //排除content字段
                .fetchSource(new String[]{},new String[]{"content"})
                .from(0)
                .size(20);
        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.source(ssb);
        return restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
    }
}
