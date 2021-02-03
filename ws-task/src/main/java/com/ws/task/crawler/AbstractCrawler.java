package com.ws.task.crawler;

import com.ws.task.AbstractTask;
import com.ws.task.model.CrawlerItem;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AbstractCrawler extends AbstractTask implements PageProcessor {

    private final static Proxy proxy = new Proxy("127.0.0.1",10809);

    private ConcurrentHashMap<String,CrawlerItem> datas = new ConcurrentHashMap<>();

    private boolean useProxy;

    private String[] pageUrls;

    public  AbstractCrawler(String ...pageUrl){
        this(false,pageUrl);
    }
    public  AbstractCrawler(boolean useProxy,String ...pageUrls){
        //page去重
        for (String pageUrl :
                pageUrls) {
            CrawlerItem item = new CrawlerItem();
            item.setUrl(pageUrl);
            datas.put(pageUrl,item);
        }
        this.pageUrls = pageUrls;
        this.useProxy =useProxy;
    }

    /**
     * 解析方式
     * @param page
     */
    public abstract CrawlerItem parse(Page page);


    @Override
    public void process(Page page) {
        datas.get(page.getUrl().get()).onSuccess(parse(page));
    }

    @Override
    public Site getSite() {
        return Site
                .me()
                .setDomain("sehuatang.org")
                .setSleepTime(3000)
                .setRetryTimes(5)
                .setRetrySleepTime(3000)
                .setTimeOut(300000)
                .setUserAgent(
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    }

    private void scan() {
        InnerDownloader downloader = new InnerDownloader();
        if(useProxy){
            downloader.setProxyProvider(SimpleProxyProvider.from(proxy));
        }
        int total = pageUrls.length;
        if(total > 0){
            Spider.create(this)
                    .setDownloader(downloader)
                    // 添加初始化的URL
                    .addUrl(pageUrls)
                    // 使用单线程
                    .thread(total<10?total:10)
                    // 运行
                    .run();
        }

    }

    @Override
    public void run(){
        scan();
    }


    public class InnerDownloader  extends HttpClientDownloader {
        @Override
        public Page download(Request request, Task task) {
            datas.get(request.getUrl()).onStart();
            return super.download(request, task);
        }
        @Override
        protected void onError(Request request) {
            datas.get(request.getUrl()).onError();
        }
    }
}
