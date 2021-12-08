package com.dysen.socket_library.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class XmlUtil {
		  public static Document read(String filePath)
		    throws MalformedURLException, DocumentException
		  {
		    SAXReader saxReader = new SAXReader();
		    Document document = saxReader.read(new File(filePath));
		    return document;
		  }

		  public static Document read(InputStream is)
		    throws MalformedURLException, DocumentException
		  {
		    SAXReader saxReader = new SAXReader();
		    Document document = saxReader.read(is);
		    return document;
		  }

		  public static void write(Document document, String filePath, String characterSet)
		    throws IOException
		  {
		    OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		    if (characterSet == null)
		    {
		      outputFormat.setEncoding("UTF-8");
		    }
		    XMLWriter writer = new XMLWriter(new FileOutputStream(filePath), outputFormat);
		    writer.write(document);
		    writer.close();
		  }

		  public static void write(Document document, File xmlFile, String characterSet)
		    throws IOException
		  {
		    OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		    if (characterSet == null)
		    {
		      outputFormat.setEncoding("UTF-8");
		    }
		    XMLWriter writer = new XMLWriter(new FileOutputStream(xmlFile), outputFormat);
		    writer.write(document);
		    writer.close();
		  }
}
