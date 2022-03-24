import org.jsoup.Jsoup;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class makeKeyword {
    private String input_file;
    private String output_file = "./index.xml";

    public makeKeyword(String file) {
        this.input_file = file;
    }
    public void convertXml() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse(this.input_file);

        for (int i = 0;i<document.getElementsByTagName("doc").getLength();i++){
            String testString = document.getElementsByTagName("doc").item(i).getChildNodes().item(1).getTextContent();
            KeywordExtractor ke = new KeywordExtractor();
            KeywordList kl = ke.extractKeyword(testString,true);
            String newBody = "";

            for(int j = 0;j<kl.size();j++) {
                Keyword kwrd = kl.get(j);
                newBody = newBody+kwrd.getString()+":"+Integer.toString(kwrd.getCnt())+"#";
                document.getElementsByTagName("doc").item(i).getChildNodes().item(1).setTextContent(newBody);
            }

        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(new File(this.output_file)));

        transformer.transform(source, result);

    }
}
