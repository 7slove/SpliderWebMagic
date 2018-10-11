package us.codecraft.webmagic;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.processor.PageProcessor;

public class SinaBlogProcessor implements PageProcessor {

	public static final String URL_LIST = 
	"http://www\\.ccgp-jiangsu\\.gov\\.cn/was5/web/search\\?page=\\d+&channelid=204408&searchword=%E6%A1%A3%E6%A1%88&keyword=%E6%A1%A3%E6%A1%88&orderby=-DocrelTime&perpage=10&outlinepage=10&searchscope=&timescope=&timescopecolumn=&orderby=-DocrelTime&andsen=&total=&orsen=&exclude=";
	//public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";

	
	public static final String URL_POST = "http://www\\.ccgp-jiangsu\\.gov\\.cn/.*";
	//public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";
	// //*[@id="column1"]/div[1]/table/tbody/tr/td[2]/ol
	// //*[@id="column1"]/div[1]/table/tbody/tr/td[2]/ol
	// /html/body/div[2]/div[2]/div[2]/div[1]/h1
	// //*[@id="t_58ae76e80100gd7m"]
	// //*[@id="articlebody"]/div[1]

	private Site site = Site.me().setDomain("www.ccgp-jiangsu.gov.cn")
			.setSleepTime(2000)
			.setRetryTimes(3)
			// 设置重试次数
			.setCycleRetryTimes(3)
			// 设置循环重试次数
			.setUserAgent(
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");

	@Override
	public void process(Page page) {
		// 列表页
		if (page.getUrl().regex(URL_LIST).match()) {
			page.addTargetRequests(page.getHtml()
					.xpath("//*[@id=\"column1\"]/div[1]/table/tbody/tr/td[2]/ol").links()
					.regex(URL_POST).all());
			page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
			// 文章页
		} else {
			page.putField(
					"title",
					page.getHtml().xpath(
							"//div[@class='dtit']/h1/text()"));
			
			page.putField(
					"enclosure",
					page.getHtml()
							.xpath("//div[@class='detail_fj']//ul//li//a/@href"));
			
			/*page.putField(
					"content",
					page.getHtml()
							.xpath("//div[@id='articlebody']//div[@class='articalContent']/allText()"));
			page.putField(
					"date",
					page.getHtml()
							.xpath("//div[@id='articlebody']//span[@class='time SG_txtc']")
							.regex("\\((.*)\\)"));
			page.putField(
					"imgSrc",
					page.getHtml()
							.xpath("//div[@id='articlebody']//div[@class='articalContent']")
							.regex("<(?i)img(.*?)</(?i)").all());*/

			// 获取指定的链接
			if(StringUtils.isBlank(String.valueOf(page.getResultItems().get("enclosure")))){
				/*System.out.println("title的值为："+ page.getResultItems().get("title"));
				System.out.println("enclosure的值为空："+ page.getResultItems().get("enclosure"));*/
				page.setSkip(true); 
			}
			
			// 遍历所有结果，输出到控制台，上面例子中的"author"、"name"、"readme"都是一个key，其结果则是对应的value
		  /*for (Map.Entry<String, Object> entry : page.getResultItems().getAll().entrySet()) {
			  System.out.println("^^^^^^^^^^^^^^"+entry.getKey() + ":\t" + entry.getValue()); 
		  }*/
			 

		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new SinaBlogProcessor())
				.addUrl("http://www.ccgp-jiangsu.gov.cn/was5/web/search?page=1&channelid=204408&searchword=%E6%A1%A3%E6%A1%88&keyword=%E6%A1%A3%E6%A1%88&orderby=-DocrelTime&perpage=10&outlinepage=10&searchscope=&timescope=&timescopecolumn=&orderby=-DocrelTime&andsen=&total=&orsen=&exclude=")
				.addPipeline(new ImgPipelineTest("D:\\dataForJiangSu\\webmagic"))
				.thread(5).run();
	}

}
