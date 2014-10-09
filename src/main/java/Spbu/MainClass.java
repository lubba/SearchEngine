package Spbu;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class MainClass {
    static HashMap<String, Set<String>> index = null;
    static String and = " AND ";
    static String or = " OR ";
    public static void main(String[] args){
        Indexer ind = new Indexer();

        String directory = args[0];
        File dir = new File(directory+"/index/");
        dir.mkdir();
        ind.setDir(directory);
        File indexFile = new File(directory + "/index/index.txt");

        if (indexFile.exists()) {
            index = ind.readIndex();
        } else index = ind.createIndex();


        Searcher search = new Searcher();
        search.set(index, ind.getCollection());

        Scanner in = new Scanner(System.in);
        String query = "";
        while (!query.equals("\\exit")){
            query = in.nextLine();
            if (query.contains(and))
                search.searchAnd(query.substring(0,query.indexOf(and)),query.substring(query.indexOf(and)+and.length()));
            else if (query.contains(or))
                search.searchOr(query.substring(0,query.indexOf(or)),query.substring(query.indexOf(or)+or.length()));
            else if (!query.equals("\\exit")) search.searchOne(query);
        }
    }
}
