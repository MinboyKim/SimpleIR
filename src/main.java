import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, SAXException {
        xmlMaker m = new xmlMaker();
        m.xmlMake();
        kkma km = new kkma();
        km.kkmaMake();
    }
}
