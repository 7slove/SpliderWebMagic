package pachong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * 利用java的jsoup开发搜索引擎爬虫
 * @author Leinuoa
 */
public class HtmlJsoup
{
    /**
     * 根据网址和页面的编码集获取网页的源代码
     * @param url 需要下载的URL地址
     * @param encoding 网页的编码集
     * @return String 网页的源代码
     */
    public static String getHtmlResourceByURL(String url,String encoding){
        StringBuffer sb = new StringBuffer();
        URL urlObj = null;
        URLConnection uc = null;
        BufferedReader br = null;

        try{
            //建立网络连接
            urlObj = new URL(url);
            //打开网络连接
            uc = urlObj.openConnection();
            br = new BufferedReader(new InputStreamReader(uc.getInputStream(),encoding));
            String tempLine = null;//临时变量（就是一个临时文件）
            while((tempLine=br.readLine())!=null){
                sb.append(tempLine+"\n");//每读一行后自动换行
            }
        }catch(Exception e){
            e.getStackTrace();
            System.out.println("Connection timeOut......");
        }finally{
            if(null != br){
                try
                {
                    br.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
            return sb.toString();
    }

    /**
     * 根据一个图片的URL地址，通过这个URL批量下载图片到服务器
     * @param imgURL 要下载的图片所在服务器地址
     * @param filePath 下载完成后保存图片的服务器地址
     */
    public static void downImages(String imgURL,String filePath){

        try
        {
            //首先获取图片名
            String fileName = imgURL.substring(imgURL.lastIndexOf("/"));
            //创建文件的目录
            File files = new File(filePath);
            //判断文件目录是否存在，若不存在就创建一个
            if(!files.exists()){
                files.mkdirs();
            }
            //获取图片文件的下载地址
            URL url = new URL(imgURL);
            //连接网络图片地址
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            //获取连接的输出流
            InputStream is = huc.getInputStream();

            //创建文件
            File file = new File(filePath+fileName);
            //创建输出流，写入文件
            FileOutputStream out = new FileOutputStream(file);
            int i = 0;
            while((i=is.read()) != -1){
                out.write(i);
            }
            out.close();
            is.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        //根据网址和页面的编码集获取网页的源代码
        String htmlResource = getHtmlResourceByURL("http://mil.sohu.com/20151117/n426688885.shtml","gb2312");

        System.out.println(htmlResource);

        //解析源代码
        Document document = Jsoup.parse(htmlResource);
        //获取网页的图片
        //图片标签<img src="" alt="" width="" height="" />
        Elements elements = document.getElementsByTag("img");
        for(Element element:elements){
            String imgSrc = element.attr("src");
//            String imgPath = "http://i2.itc.cn/"+imgSrc;
            System.out.println("下载的图片地址："+imgSrc);
            downImages(imgSrc, "E:\\images\\");
            System.out.println("图片下载成功！============");
        }

    }
}