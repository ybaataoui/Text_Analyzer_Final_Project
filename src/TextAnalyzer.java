import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang.StringUtils;


public class TextAnalyzer {

	public static void main(String[] args) throws IOException {
		TextAnalyzer TA = new TextAnalyzer();

		String content = Files.readString(Paths.get(TA.FilePath()));
		
		String newContent1 = Between(content, "START", "LICENSE"); // substring the text wanted
		
		String[] array = newContent1.split(" "); // split the string into words and put them in an array
		
        Map<String, Integer> words = new HashMap<>(); // new hashmap
        for (String str : array) { 					 // loop through the string array	
            if (words.containsKey(str)) {			// check if the hashmap containes the str word
                words.put(str, 1 + words.get(str));  //if the word exist increment it's frequency by 1
            } else {
                words.put(str, 1);					// if not add the new word to the hashmap and put its frequency to 1
            }
        }
         
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        
        reverseSortedMap = TA.reverseMapOrder(words); // 

        //Print values
        reverseSortedMap.forEach((key, value)-> System.out.println(key + " : " + value));

	}
	
	//Sort the last 20 words descendant 
	public LinkedHashMap<String, Integer> reverseMapOrder(Map<String, Integer> words){

        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
         
        //Use Comparator.reverseOrder() for reverse ordering
        words.entrySet()
          .stream()
          .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
          .limit(20)
          .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
		
		return reverseSortedMap;	
	}
	
	//A function that substring a string between two strings.
	public static String Between(String STR , String FirstString, String LastString)
    {       
           
        String FinalString = StringUtils.substringBetween(STR, FirstString, LastString);
        
        return FinalString;
    }
	
	public String FilePath() {
		
		return "C:\\Users\\Baataoui Youssef\\Desktop\\text1.txt";
	}
}
