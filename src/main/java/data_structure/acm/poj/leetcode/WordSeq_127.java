package data_structure.acm.poj.leetcode;

import java.util.*;

/**
 * 基本思路：bfs ，构建单词之间的图关系，然后广度遍历
 */
public class WordSeq_127 {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        List<String> newWordList = new ArrayList<>(wordList.size()+2);
        newWordList.add(beginWord);
       // newWordList.add(endWord);
        newWordList.addAll(wordList);
        Map<String,LinkedList> map = buildWordMap(newWordList);
        LinkedList list = map.get(beginWord);
        if(list == null || !map.containsKey(endWord)){
            return 0;
        }
        Set<String> alreadyProcessed = new HashSet<>();
        List<Node> nodes = new LinkedList<>();
        nodes.add(new Node(beginWord,0));
        alreadyProcessed.add(beginWord);
        int result = 0;
        while (!nodes.isEmpty()){
            Node node = ((LinkedList<Node>) nodes).pop();
            if(node.word.equals(endWord)){
                return node.level+1;
            }
            LinkedList<String> words = map.get(node.word);
            if(words != null){
                for (String word : words) {

                    if(!alreadyProcessed.contains(word)){
                        nodes.add(new Node(word,node.level+1));
                        alreadyProcessed.add(word);
                    }
                }
            }
        }
        return 0;

    }
    static class Node{
        String word;
        int level;

        public Node(String word, int level) {
            this.word = word;
            this.level = level;
        }
    }

    Map<String,LinkedList> buildWordMap(List<String> wordList){
        Map<String,LinkedList> map = new HashMap<>();
        for (String word : wordList) {
            for (String word1 : wordList) {
                if(!word.equals(word1)){
                    if(isOneDiff(word,word1)){
                        map.putIfAbsent(word,new LinkedList());
                        map.get(word).add(word1);
                    }
                }
            }
        }
        return map;
    }
    boolean isOneDiff(String word1,String word2){
        int cnt = 0;
        for (int i = 0; i < word1.length(); i++) {
            if(word1.charAt(i) != word2.charAt(i)){
                cnt++;
                if(cnt > 1){
                    return false;
                }
            }
        }
        if(cnt == 1 ){
            return true;
        }
        return false;
    }
    public static void main(String[] args){
        WordSeq_127 wordSeq_127 = new WordSeq_127();
        String beginWord = "hit";
        String endWord = "cog";
        String[] wordArr = new String[]{"hot","dot","dog","log","cog"};
        int result =  wordSeq_127.ladderLength(beginWord,endWord,Arrays.asList(wordArr));
        System.out.println(result);

         beginWord = "hit";
         endWord = "cog";
        wordArr = new String[]{"hot","dot","dog","lot","log"};
         result =  wordSeq_127.ladderLength(beginWord,endWord,Arrays.asList(wordArr));
        System.out.println(result);


        beginWord = "hot";
        endWord = "hit";
        wordArr = new String[]{"hit","hat","dog","lot","log"};
        result =  wordSeq_127.ladderLength(beginWord,endWord,Arrays.asList(wordArr));
        System.out.println(result);
    }
}
