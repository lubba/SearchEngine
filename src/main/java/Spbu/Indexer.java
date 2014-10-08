package Spbu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Indexer {
    private static File dir;
    private static File indexPlace;
    private static String dirPlace;
    private static HashMap<String, Set<Integer>> index;
    private static String arrow = " ->";
    public static void writeIndex(){
        createIndex();
        try {
            indexPlace.createNewFile();
            FileWriter writer = new FileWriter(indexPlace);
            for (String word : index.keySet()){
                //System.out.print(word+" -> ");
                writer.append(word+arrow);
                for (Integer i : index.get(word)){
                    //System.out.print(i+" ");
                    writer.append(i+"");
                }
                //System.out.println();
                writer.append("/r/n");
            }
            writer.flush();
            writer.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void createIndex(){
        File[] files = dir.listFiles();
        if (files == null){
            System.out.println("No files in "+ dirPlace);
            return;
        }

        index = new HashMap<String, Set<Integer>>();
        ArrayList<String> collection = new ArrayList<String>();

        for (File file : files)
        {
            if (file.isFile()){
                try {
                    Document doc = Jsoup.parse(file, "UTF-8");
                    String text = doc.body().text();
                    collection.add(text);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
        for (String s : collection){
            String[] newWords = s.split("[ -]");
            for (String word : newWords){
                //TODO: убрать лишние знаки препинания
                word = word.replaceAll("[.,!?:;|\"]","");
                word = word.replace("(","");
                word = word.replace(")","");
                word = word.replace("[","");
                word = word.replace("]","");
                word = word.toLowerCase();
                //TODO: сделать из слов токены
                Integer number = collection.indexOf(s);
                if (index.containsKey(word)){
                    index.get(word).add(number);
                } else {
                    Set<Integer> set = new HashSet();
                    set.add(number);
                    index.put(word, set);
                }
            }
        }

    }
    public static void readIndex(){
        try {
            Scanner in = new Scanner(indexPlace);
            while (in.hasNext()){
                String s = in.nextLine();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args){
        dirPlace = args[0];
        dir = new File(dirPlace);
        indexPlace = new File(dirPlace + "/index.txt");
        if (!indexPlace.exists())
            writeIndex();
        readIndex();
    }
}
