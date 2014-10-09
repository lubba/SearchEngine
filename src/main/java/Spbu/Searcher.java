package Spbu;

import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.util.*;

public class Searcher {
    private HashMap<String, Set<String>> index;
    private HashMap<String,String> collection;

    public void set(HashMap<String, Set<String>> ind, HashMap<String,String> coll){
        index = ind;
        collection = coll;
    }
    public void searchOne(String a){
        try {
            LuceneMorphology luceneMorph = new RussianLuceneMorphology();
            List<String> wordBaseForms = luceneMorph.getMorphInfo(a);
            a = wordBaseForms.get(0);
            a = a.substring(0, a.indexOf("|"));
        }
        catch (Exception e){

        }
        if (index.containsKey(a)){
            print(index.get(a));
        } else {
            System.out.println("Sorry, no files");
        }
    }

    public void searchAnd(String a,String b){
        LuceneMorphology luceneMorph = null;
        try {
            luceneMorph = new RussianLuceneMorphology();
            List<String> wordBaseForms = luceneMorph.getMorphInfo(a);
            a = wordBaseForms.get(0);
            a = a.substring(0, a.indexOf("|"));
        }
        catch (Exception e){

        }
        if (index.containsKey(a)){
            List<String> wordBaseForms = luceneMorph.getMorphInfo(b);
            b = wordBaseForms.get(0);
            b = b.substring(0, b.indexOf("|"));
            if (index.containsKey(b)){
                Set<String> docs1 = index.get(a);
                Set<String> docs2 = index.get(b);
                print(and(docs1,docs2));
            } else System.out.println("Sorry, no files");
        } else System.out.println("Sorry, no files");
    }


    public void searchOr(String a, String b){
        LuceneMorphology luceneMorph = null;
        try {
            luceneMorph = new RussianLuceneMorphology();
            List<String> wordBaseForms = luceneMorph.getMorphInfo(a);
            a = wordBaseForms.get(0);
            a = a.substring(0, a.indexOf("|"));
        }
        catch (Exception e){

        }
        Set<String> docs1 = null;
        Set<String> docs2 = null;
        if (index.containsKey(a)) {
            List<String> wordBaseForms = luceneMorph.getMorphInfo(b);
            b = wordBaseForms.get(0);
            b = b.substring(0, b.indexOf("|"));
            docs1 = index.get(a);
        }
        if (index.containsKey(b)){
                docs2 = index.get(b);
            }

        print(or(docs1,docs2));
    }
    private void print(Set<String> docs) {
        if ((docs == null)||(docs.isEmpty())) {
            System.out.println("Sorry, no files");
            return;
        }
        for (String doc: docs){
            System.out.print(doc+" ");
        }
        System.out.println();
    }
    private Set<String> and(Set<String> docs1, Set<String> docs2) {
        if ((docs1 == null)||(docs2 == null)) return null;
        Set<String> docs = new HashSet<String>();
        docs.addAll(docs1);
        docs.retainAll(docs2);
        return docs;
    }
    private Set<String> or(Set<String> docs1, Set<String> docs2) {
        if (docs1 == null) return docs2;
        if (docs2 == null) return docs1;
        Set<String> docs = new HashSet<String>();
        docs.addAll(docs1);
        docs.addAll(docs2);
        return docs;
    }


}
