import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class xmlMaker {
    public void xmlMake() throws ParserConfigurationException, IOException, TransformerException {
        File myDir = new File("C:\\Users\\minboy\\Desktop\\SimpleIR\\2주차 실습 html");
        File[] files = myDir.listFiles();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document document = docBuilder.newDocument();

        Element docs = document.createElement("docs");
        document.appendChild(docs);

        for (int i = 0 ; i < files.length; i++) {
            org.jsoup.nodes.Document psfile = Jsoup.parse(files[i], "UTF-8");
            String title = psfile.getElementsByTag("title").text();
            String body = psfile.getElementsByTag("body").text();

            Element doc = document.createElement("doc");
            doc.setAttribute("id", Integer.toString(i));
            docs.appendChild(doc);

            Element docutitle = document.createElement("title");
            docutitle.appendChild(document.createTextNode(title));
            doc.appendChild(docutitle);

            Element docubody = document.createElement("body");
            docubody.appendChild(document.createTextNode(body));
            doc.appendChild(docubody);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(new File("C:\\Users\\minboy\\Desktop\\SimpleIR\\collection.xml")));

        transformer.transform(source, result);
    }
}
