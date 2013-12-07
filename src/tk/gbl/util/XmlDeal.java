package tk.gbl.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;

public class XmlDeal {
	private XmlDeal() {
	}

	public static Document generateDocument(String xml)
			throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		return document;
	}

	public static Document generateDocument(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	public static Element getRootElement(Document doc) {
		return doc.getRootElement();
	}

	

}

