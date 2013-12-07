package tk.gbl.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;

import tk.gbl.http.Request;
import tk.gbl.http.Response;
import tk.gbl.util.XmlDeal;

/**
 * 
 * @author Don'T PH 7.0 2013-8-30
 */
public class ResponseManager {

	/**
	 * 域名
	 */
	String domain = "http://localhost";

	/**
	 * 根路径
	 */
	String rootPath;

	/**
	 * 请求路径
	 */
	String reqPath;
	/**
	 * 请求相对路径
	 */
	String reqCurrPath;

	/**
	 * 请求方法(get post...)
	 */
	String method;
	/**
	 * 类型，html servlet ...
	 */
	String type;

	OutputStream outputStream;
	PrintWriter printWriter;

	public ResponseManager(String reqPath) {
		this.reqPath = reqPath;
		if (reqPath.endsWith(".html")) {
			this.type = "html";
			return;
		}

		if (reqPath.endsWith(".do")) {
			this.type = "servlet";
			return;
		}

		if (reqPath.endsWith(".jpg") || reqPath.endsWith(".png")) {
			this.type = "img";
			return;
		}

		if (reqPath.endsWith("/")) {
			this.type = "directory";
			return;
		}
		this.type = "directory";
	}

	public String output() {
		String outStr = null;
		switch (type) {
		case "html":
			outStr = htmlOutput();
			break;
		case "servlet":
			try {
				outStr = serveltOutput();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case "img":
			outStr = imgOutput();
			break;
		case "directory":
			outStr = dirOutput();
			break;
		}

		return outStr;
	}

	private String dirOutput() {
		File dir = new File(reqPath);
		String[] list = dir.list();

		String outStr = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html; charset=UTF-8\r\n\n";
		outStr += "<html><head></head><body>";
		for (String name : list) {
			String end = "";
			if (new File(reqPath + name).isDirectory())
				end = "/";
			outStr += "<a href=\"" + "/" + name + end + "\">";
			outStr += name;
			outStr += "</a>";
			outStr += "<br>";
		}
		outStr += "</body></html>";
		printWriter.println(outStr);
		return outStr;
	}

	private String serveltOutput() throws Exception {

		// 请求相对url -> class路径
		System.out.println(rootPath + "/WEB-INF/web.xml");
		File xmlFile = new File(rootPath + "/WEB-INF/web.xml");
		Document doc = XmlDeal.generateDocument(xmlFile.toURI().toURL());
		Element rootElem = doc.getRootElement();
		String servlet_name = null;
		for (Iterator mapit = rootElem.elementIterator("servlet-mapping"); mapit
				.hasNext();) {
			Element servlet_mapping = (Element) mapit.next();
			String url_pattern = null;
			for (Iterator attrit = servlet_mapping.elementIterator(); attrit
					.hasNext();) {
				Element attr = (Element) attrit.next();

				if (attr.getName().equals("url-pattern")) {
					url_pattern = attr.getText();
				}
				if (attr.getName().equals("servlet-name")) {
					servlet_name = attr.getText();
				}
			}
			System.out.println(reqCurrPath);
			if (url_pattern.equals(reqCurrPath)) {
				break;
			}
		}

		String classPath = "";
		for (Iterator serit = rootElem.elementIterator("servlet"); serit
				.hasNext();) {
			Element servletElem = (Element) serit.next();
			String sname = null;
			for (Iterator attrit = servletElem.elementIterator(); attrit
					.hasNext();) {
				Element attr = (Element) attrit.next();

				if (attr.getName().equals("servlet-name")) {
					sname = attr.getText();
				}
				if (attr.getName().equals("servlet-class")) {
					classPath = attr.getText();
				}
			}

			if (sname.equals(servlet_name)) {
				break;
			}
		}
		System.out.println(classPath);

		// 实例化servlet
		URL urls[] = new URL[1];

		urls[0] = new File(rootPath + "/WEB-INF/classes").toURI().toURL();
		URLClassLoader loader = new URLClassLoader(urls);
		System.out.println(classPath);
		Class c = loader.loadClass(classPath);

		Servlet servlet = (Servlet) c.newInstance();
		System.out.println(servlet.getClass());
		



		Request req = new Request();
		req.setMethod(method);
		Response res = new Response();
		res.setPrintWriter(printWriter);

		// 反射invoke servlet的service方法
		servlet.service(req,res);
		return null;
	}

	private String imgOutput() {
		String str = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html; charset=UTF-8\r\n\n";
		str += "<html><head></head><body>" + "<img src=\"" + reqCurrPath
				+ "\">" + "</body></html>";
		printWriter.println(str);
		return str;
	}

	private String htmlOutput() {
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(reqPath));
		} catch (FileNotFoundException e) {
			String str = "HTTP/1.1 404 notFound\r\n"
					+ "Content-Type: text/html\r\n\n";
			str += "<html><head></head><body>404 not found</body></html>";
			printWriter.println(str);
			return str;
		}
		String outStr = "";
		String line;
		try {
			while ((line = fileReader.readLine()) != null) {
				outStr += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		outStr = "HTTP/1.1 200 OK\r\n"
				+ "Content-Type: text/html; charset=UTF-8\r\n\n" + outStr;
		printWriter.println(outStr);
		return outStr;
	}

	public String getReqPath() {
		return reqPath;
	}

	public void setReqPath(String reqPath) {
		this.reqPath = reqPath;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReqCurrPath() {
		return reqCurrPath;
	}

	public void setReqCurrPath(String reqCurrPath) {
		this.reqCurrPath = reqCurrPath;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
}
