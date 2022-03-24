import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Math.log;

public class indexer {
    private  String input_file;
    private String output_file = "./index.post";

    public indexer(String file){
        this.input_file = file;
    }
    public void indexMake() throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
        FileOutputStream fileStream = new FileOutputStream(this.output_file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

        HashMap keyMap = new HashMap();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse(this.input_file);

        for(int i = 0;i<document.getElementsByTagName("body").getLength();i++){
            String testString = document.getElementsByTagName("body").item(i).getTextContent();
            String[] sharp = testString.split("#");

            for(int j = 0;j< sharp.length;j++){
                int tempArray[] = new int[document.getElementsByTagName("body").getLength()];
                String[] colon = sharp[j].split(":");
                if(keyMap.containsKey(colon[0])){
                    tempArray = (int[]) keyMap.get(colon[0]);
                    tempArray[i] = Integer.parseInt(colon[1]);
                    keyMap.put(colon[0], tempArray);
                } else {
                    tempArray[i] = Integer.parseInt(colon[1]);
                    keyMap.put(colon[0], tempArray);
                }
            }
        }

        Iterator<String> keys = keyMap.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            double w = 0;
            int df = 0;
            int[] value = (int[]) keyMap.get(key);
            double[] doubles = Arrays.stream(value).asDoubleStream().toArray();
            for(int i = 0;i< value.length;i++){
                if(value[i] !=0 )
                    df++;
            }
            for(int i = 0;i<value.length;i++){
                w = value[i] * log((double)document.getElementsByTagName("body").getLength() / (double) df);
                doubles[i] = w;
            }
            keyMap.put(key,doubles);
        }
        objectOutputStream.writeObject(keyMap);
        objectOutputStream.close();
        FileInputStream fileInputStream = new FileInputStream(this.output_file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        System.out.println("읽어온 객체의 type -> "+ object.getClass());

        HashMap hashMap = (HashMap) object;
        Iterator<String> it = hashMap.keySet().iterator();

        while (it.hasNext()){
            String key = it.next();
            double[] value = (double[]) hashMap.get(key);
            String sValue = "";
            for(int i = 0;i< value.length;i++){
                sValue = sValue + Integer.toString(i) + " " + String.format("%2.1f", value[i]) +" ";
            }
            System.out.println(key + " -> " + sValue);
        }
    }
}
