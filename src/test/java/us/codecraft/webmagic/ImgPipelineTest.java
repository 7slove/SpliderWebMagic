package us.codecraft.webmagic;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import com.alibaba.fastjson.JSON;

public class ImgPipelineTest extends FilePersistentBase implements Pipeline {

	public ImgPipelineTest() {
		setPath("/dataForJiangSu/webmagic");
	}

	public ImgPipelineTest(String path) {
		setPath(path);
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		/*String path = (new StringBuilder()).append(this.path)
				.append(task.getUUID()).append(PATH_SEPERATOR).toString();*/
		try {
			// 保存文字
			/*PrintWriter printWriter = new PrintWriter(new FileWriter(
					getFile((new StringBuilder())
							.append(path)
							.append(PATH_SEPERATOR)
							.append(resultItems
									.getAll()
									.get("title")
									.toString()
									.replaceAll("[\\pP|~|$|^|<|>|\\||\\+|=]*",
											""))
							.append(PATH_SEPERATOR)
							.append(resultItems
									.getAll()
									.get("title")
									.toString()
									.replaceAll("[\\pP|~|$|^|<|>|\\||\\+|=]*",
											"")).append(".json").toString())));

			printWriter.write(JSON.toJSONString(resultItems.getAll()));
			printWriter.close();*/
			// 保存图片
			for (String key : resultItems.getAll().keySet()) {
				if (!resultItems.getAll().get("imgSrc").toString().equals("[]")) {
					Pattern p = Pattern.compile("(?:real_src=\"?)(.*?)\"?\\s");
					Matcher m = p.matcher(resultItems.getAll().get("imgSrc")
							.toString());
					URL url = null;

					String path2 = (new StringBuilder())
							.append(this.path)
							.append(task.getUUID())
							.append(PATH_SEPERATOR)
							.append(resultItems
									.getAll()
									.get("title")
									.toString()
									.replaceAll("[\\pP|~|$|^|<|>|\\||\\+|=]*",
											"")).toString();

					File file = new File(path2);
					if (!file.isDirectory()) {
						file.mkdirs();
					}

					String pathString = "";
					Integer i = 0;
					while (m.find()) {
						url = new URL(m.group(1));
						i++;
						pathString = (new StringBuilder())
								.append(this.path)
								.append(task.getUUID())
								.append(PATH_SEPERATOR)
								.append(resultItems
										.getAll()
										.get("title")
										.toString()
										.replaceAll(
												"[\\pP|~|$|^|<|>|\\||\\+|=]*",
												"")).append(PATH_SEPERATOR)
								.append(i.toString()).toString();
						DataInputStream dataInputStream = new DataInputStream(
								url.openStream());
						FileOutputStream fileOutputStream = new FileOutputStream(
								new File(pathString + ".jpg"));
						byte[] buffer = new byte[1024];
						int length;
						while ((length = dataInputStream.read(buffer)) > 0) {
							fileOutputStream.write(buffer, 0, length);
						}
						dataInputStream.close();
						fileOutputStream.close();
						// System.out.println(m.group(1));
					}
				}
			}
			
				
				
				URL url = null;
				String href = String.valueOf(resultItems.getAll().get("enclosure"));
				if(href.indexOf("www") > 0){
					url = new URL(href);
					String path2 = (new StringBuilder())
							.append(this.path)
							.append(PATH_SEPERATOR)
							.append(resultItems
									.getAll()
									.get("title")
									.toString()).toString();

					File file = new File(path2);
					if (!file.isDirectory()) {
						file.mkdirs();
					}
					
					/*String pathString = (new StringBuilder())
							.append(this.path)
							.append(PATH_SEPERATOR)
							.append(resultItems
									.getAll()
									.get("title")
									.toString())
							.append(PATH_SEPERATOR)
							.append(i.toString()).toString();*/
					//System.out.println("^^^^^^^^^^^^^^^^^^^"+resultItems.getAll().get("title"));
					//System.out.println("^^^^^^^^^^^^^^^^^^^"+href);
				}
				
			
		} catch (Exception e) {
			System.out.println("**************URL解析异常*************");
			e.printStackTrace();
		}
	}
}
