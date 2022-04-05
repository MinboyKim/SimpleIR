import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;

public class searcher {
    private String data_path;
    public searcher(String path){this.data_path = path;}
    public void CalcSim(String query) throws IOException, ClassNotFoundException, ParserConfigurationException, SAXException {
        FileInputStream fileInputStream = new FileInputStream(this.data_path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        HashMap hashMap = (HashMap) object;

        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(query, true);

        HashMap vector = new HashMap();
        for(int i = 0;i<kl.size();i++) {
            Keyword kwrd = kl.get(i);
            vector.put(kwrd.getString(), kwrd.getCnt());
        }

        Iterator<String> it = vector.keySet().iterator();
        double[] result = new double[5];
        for(int i = 0;i<5;i++) {
            while(it.hasNext()){
                String key = it.next();
                int value = (int) vector.get(key);
                double[] hvalue = (double[]) hashMap.get(key);
                if(hvalue == null){
                    hvalue = new double[5];
                }
                for(int j = 0;j<result.length;j++){
                    result[j] = result[j] + value*hvalue[j];
                }
            }
        }

        double[] sresult = new double[result.length];
        for(int i = 0;i<result.length;i++){
            sresult[i] = result[i];
        }
        int[] index = new int[5];
        for(int i = 0;i<index.length;i++){
            index[i] = i;
        }
        double temp = 0;
        int temp2 = 0;
        for(int i = 0;i<sresult.length;i++){
            for(int j = 0;j<sresult.length-1-i;j++){
                if(sresult[j] < sresult[j+1]){
                    temp = sresult[j];
                    sresult[j] = sresult[j+1];
                    sresult[j+1] = temp;
                    temp2 = index[j];
                    index[j] = index[j+1];
                    index[j+1] = temp2;
                }
            }
        }
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse("./index.xml");
        String[] title = new String[5];
        for(int i = 0;i<5;i++) {
            title[i] = document.getElementsByTagName("title").item(i).getTextContent();
        }
        if(sresult[0] == 0){
            System.out.println("검색된 문서가 없습니다.");
        } else if(sresult[1] == 0){
            System.out.printf("1. title : %s    유사도 : %f\n", title[index[0]], sresult[0]);
        }
        else if(sresult[2] == 0) {
            System.out.printf("1. title : %s    유사도 : %f\n", title[index[0]], sresult[0]);
            System.out.printf("2. title : %s    유사도 : %f\n", title[index[1]], sresult[1]);
        } else {
            System.out.printf("1. title : %s    유사도 : %f\n", title[index[0]], sresult[0]);
            System.out.printf("2. title : %s    유사도 : %f\n", title[index[1]], sresult[1]);
            System.out.printf("3. title : %s    유사도 : %f\n", title[index[2]], sresult[2]);
        }

    }
}