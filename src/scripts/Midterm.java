import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Midterm {
    private String data_path;
    Midterm(String path){
        this.data_path = path;
    }
    public void showSnippet(String query) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse(this.data_path);       //collection.xml document생성

        ArrayList<String> qArray = new ArrayList<>();
        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(query, true);
        for(int i = 0;i<kl.size();i++){
            Keyword kwrd = kl.get(i);
            qArray.add(kwrd.getString());
        }       // 쿼리 키워드 배열 생성

        HashMap<Integer,String> snippet = new HashMap<>();
        for(int i = 0;i<document.getElementsByTagName("body").getLength();i++){
            String body = document.getElementsByTagName("body").item(i).getTextContent();
            KeywordExtractor ke2 = new KeywordExtractor();
            KeywordList kl2 = ke2.extractKeyword(body,true);
            HashMap<String, Integer> bodyHash = new HashMap<>();
            for(int j = 0;j<kl2.size();j++){
                Keyword kwrd2 = kl2.get(j);
                bodyHash.put(kwrd2.getString(), kwrd2.getCnt());
            }
            String[] bodyString = body.split("");
            int index = body.indexOf(qArray.get(0));
            String newString = "";
            if(index != -1) {
                for (int m = 0; m < 30; m++) {
                    newString = newString + bodyString[index];
                    index++;
                }
            }
            snippet.put(i,newString);
        }

        Iterator<Integer> keys = snippet.keySet().iterator();
        while(keys.hasNext()){
            int key = keys.next();
            if(snippet.get(key)!=""){
                System.out.printf("%s, %s",document.getElementsByTagName("doc").item(key).getChildNodes().item(0).getTextContent(),snippet.get(key));
            }
        }

    }
}
