import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;


public class kuir {
    public static void main(String[] args) throws ParserConfigurationException, IOException, TransformerException, SAXException, ClassNotFoundException {
        String command = args[0];
        String path = args[1];
        if(command.equals("-c")){
            makeCollection collection = new makeCollection(path);
            collection.makeXml();
        }
        else if(command.equals("-k")){
            makeKeyword keyword = new makeKeyword(path);
            keyword.convertXml();
        } else if(command.equals("-i")){
            indexer index = new indexer(path);
            index.indexMake();
        }
        else if(command.equals("-s")){
            String query = args[3];
            searcher search = new searcher(path);
            search.CalcSim(query);
        }
        else if(command.equals("-m")){
            String query = args[3];
            Midterm  midterm = new Midterm(path);
            midterm.showSnippet(query);
        }
    }
}
