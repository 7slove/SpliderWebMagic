package us.codecraft.webmagic;

import java.util.List;
import java.util.Map;

import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class MyTest implements PageProcessor {
    private Site site= Site.me().setRetryTimes(3);

    @Override
    public void process(Page page){
        /*List<String> newstitles=page.getHtml().xpath("//*[@id='sina_keyword_ad_area2']/a/img").all();
        for(String newstitle:newstitles){
            System.out.println(newstitle);
        }*/

    	 page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2/text()"));
         page.putField("content", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']/allText()"));//html()
         page.putField("date",page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)"));
         //page.putField("imgSrc", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']").regex("<(img|IMG)[^\\<\\>]*>").all());
         page.putField("imgSrc", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']").regex("<(?i)img(.*?)</(?i)").all());
        // System.out.println(page.getResultItems());

    }


    @Override
    public Site getSite(){
        return site;
    }

    public static void main(String[] args){
    	//http://blog.sina.com.cn/s/blog_58ae76e80100lorz.html
    	//*[@id="sina_keyword_ad_area2"]/a[1]/img
    	//http://blog.sina.com.cn/s/blog_5054769e0102wmsd.html
    	//*[@id="sina_keyword_ad_area2"]/div/div[2]/p[6]/a/img
        Spider.create(new MyTest()).addUrl("http://blog.sina.com.cn/s/blog_58ae76e80100lorz.html").run();

    }
}
