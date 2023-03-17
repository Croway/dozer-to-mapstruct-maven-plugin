package it.croway;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main {

	private static final String XML = "/home/federico/Work/dozer-to-mapstruct/src/main/resources/dozer_mapping.xml";
	private static final String XSLT_FILENAME = "/home/federico/Work/dozer-to-mapstruct/src/main/resources/dozer_mapstruct.xslt";

	public static void main(String[] args) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try (InputStream is = new FileInputStream(XML)) {

			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(is);

			// transform xml to html via a xslt file
			try (FileOutputStream output =
						 new FileOutputStream("test.java")) {
				transform(doc, output);
			}

		} catch (IOException | ParserConfigurationException |
				 SAXException | TransformerException e) {
			e.printStackTrace();
		}

	}

	private static void transform(Document doc, OutputStream output)
			throws TransformerException {

		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		// add XSLT in Transformer
		Transformer transformer = transformerFactory.newTransformer(
				new StreamSource(new File(XSLT_FILENAME)));

		transformer.transform(new DOMSource(doc), new StreamResult(output));

	}
}
