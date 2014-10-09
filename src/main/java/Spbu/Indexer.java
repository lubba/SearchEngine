package Spbu;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Indexer {

    private File dir;
    private File indexPlace;
    private String dirPlace;
    private String arrow = " -> ";
    private HashMap<String, Set<String>> index;
    private HashMap<String,String> collection;
//----------------------------------------------------------------------------------


    public void writeIndex(){
        try {
            indexPlace.createNewFile();
            FileWriter writer = new FileWriter(indexPlace);
            for (String word : index.keySet()){
                writer.append(word+arrow);
                for (String i : index.get(word)){
                    writer.append(i+"#+#");
                }
                writer.append("\n");
            }
            writer.flush();
            writer.close();
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
//----------------------------------------------------------------------------------
    public HashMap<String, Set<String>> createIndex(){
        try{
            index = new HashMap<String, Set<String>>();
            LuceneMorphology luceneMorph = new RussianLuceneMorphology();

            for (String key : collection.keySet()){
                String s = collection.get(key);
                String[] newWords = s.split("[\\s \\[ \\] \\( \\)-]");
                for (String word : newWords){
                    word = word.replaceAll("[.,!?:;|\"%$»#@№«/©_=]","");
                    word = word.toLowerCase();
                    try {
                        List<String> wordBaseForms = luceneMorph.getMorphInfo(word);
                        word = wordBaseForms.get(0);
                        word = word.substring(0,word.indexOf("|"));
                    }
                    catch (Exception e){
                        //System.out.println("No forms: "+word);
                    }

                    if (index.containsKey(word)){
                        index.get(word).add(key);
                    } else {
                        Set<String> set = new HashSet();
                        set.add(key);
                        index.put(word, set);
                    }
                }
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        writeIndex();
        return index;
    }
//----------------------------------------------------------------------------------
    public HashMap<String, Set<String>> readIndex(){
        try {
            Scanner in = new Scanner(indexPlace);
            index = new HashMap<String, Set<String>>();
            while (in.hasNext()){
                String s = in.nextLine();
                String key = s.substring(0,s.indexOf(arrow));
                String[] values = s.substring(s.indexOf(arrow) + arrow.length()).split("#+#");
                Set<String> val = new HashSet<String>();
                for (String value : values){
                    val.add(value);
                }
                index.put(key,val);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return index;
    }
//----------------------------------------------------------------------------------
    public void setDir(String directory){
        dirPlace = directory;
        dir = new File(dirPlace);
        indexPlace = new File(dirPlace + "/index/index.txt");
        File[] files = dir.listFiles();
        if (files == null){
            System.err.println("No files in "+ dirPlace);
        }


        collection = new HashMap<String, String>();
        try{
            for (File file : files)
            {
                if (file.isFile()){
                    Document doc = Jsoup.parse(file, "UTF-8");
                    String text = doc.body().text();
                    collection.put(file.getName(),text);
                }

            }
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
    public HashMap<String, String> getCollection(){
        return collection;
    }
}
