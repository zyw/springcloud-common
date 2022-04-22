package cn.v5cn.springboot.webmagic;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossZhiPinPageProcessor implements PageProcessor {

    private Site site = Site.me().setDomain("zhipin.com");

    @Override
    public void process(Page page) {
        final List<String> list = page.getHtml().links().regex("https://www\\.zhipin\\.com/job_detail/?query=java&city=101110100").all();
        page.addTargetRequests(list);
//        Map<String,List<String>> headers = new HashMap<>();
//        List<String> list1 = new ArrayList<>();
//        list1.add("lastCity=101110100; Hm_lvt_194df3105ad7148dcf2b98a91b5e727a=; =; =; =; =; =; =; " +
//                "=;" +
//                " =; " +
//                "=; " +
//                "=; " +
//                "=");
//        list1.add("__zp_stoken__=8114bPGtibWd9KSg%2BXVJnVF5oSXwUBC9pRWkcJWdfKUJcfD9sOg4nXmoYNCByU1FXF1dNUSgodGpQDBxCXGg8ZFNiFwZkJWM7PQ51LmJGFDQ6G3ccdiQWIAR%2BS0lKezoBEQZHQjsAe2xdBQohLA%3D%3D");
//        headers.put("Cookie", list1);
//        page.setHeaders(headers);
        Request req = new Request();
        req.addCookie("__zp_stoken__","8114bPGtibWd9DVwQb0cwVF5oSXwfJTFRXH0cJWdfKUl3F08uOg4nXmoYNDs3PEhGF1dNUSgodBcoDEJCPhQiYnh3f2kXU3RFRAZ5KQk4FDQ6G3ccZz15ZR9%2BS0lKezoBEQZHQjsAe2xdBQohLA%3D%3D");
        req.addCookie("lastCity","101110100");
        req.addCookie("Hm_lvt_194df3105ad7148dcf2b98a91b5e727a","1603341725,1603341736,1603341998,1603342002");
        req.addCookie("__fid","9005fff24bd235e91283dd5e74eb5940");
        req.addCookie("___gtid","-1029403889");
        req.addCookie("sid","sem_pz_bdpc_dasou_title");
        req.addCookie("__zp_seo_uuid__","66f10afa-2edb-485f-a183-bb699c113ba1");
        req.addCookie("Hm_lpvt_194df3105ad7148dcf2b98a91b5e727a","1603342203");
        req.addCookie("__g","sem_pz_bdpc_dasou_title");
        req.addCookie("t","VG73xz…2594%2525E6%252589%2525BE%2525E5%2525B7%2525A5%2525E4%2525BD%25259C%2525EF%2525BC%25258C%2525E6%252588%252591%2525E8%2525A6%252581%2525E8%2525B7%25259F%2525E8%252580%252581%2525E6%25259D%2525BF%2525E8%2525B0%252588%2525EF%2525BC%252581%2526linkType%253D&g=%2Fwww.zhipin.com%2Fxian%2F%3Fsid%3Dsem_pz_bdpc_dasou_title&friend_source=0&friend_source=0");
        req.addCookie("__a","26955395.1603341702.1603341702.1603341998.12.2.6.6");
        req.addCookie("__zp_sseed__","rv84DXc0y2IlYNBWoDg+X962dL4EYFtzdFuOob8GEQQ=");
        req.addCookie("__zp_sname__","c9f00c5c");
        req.addCookie("__zp_sts__","1603342214699");
        page.setRequest(req);
//        page.getHtml();
        page.putField("html",page.getHtml());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new BossZhiPinPageProcessor())
                .addUrl("https://www.zhipin.com/job_detail/?query=java&city=101110100")
                .addPipeline(new ConsolePipeline())
                .run();
    }
}
