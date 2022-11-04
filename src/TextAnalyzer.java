import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.lang.StringUtils;

/**
 * a class that analyze a text and print the words concurrency and print the first 20 words sorted
 * @author Baataoui Youssef
 *
 */

public class TextAnalyzer {
	
	/**
	 * 
	 * @param args the command line arguments - unused
	 * @throws IOException to generate and exception if an error occurs during handling the file
	 */

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
	
	/**
	 * reverseMapOrder is a function that takes a map as parameter, sort it, 
	 * and put just the first 20 words in a new map
	 * @param words is a map parameter
	 * @return returns a sorted map of 20 workds
	 */
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
	/**
	 * Between is a function that substratct a string from another string using start word and end word
	 * @param STR is a parameter represent the whole text
	 * @param FirstString is a parameter that represent the start word 
	 * @param LastString is a parameter that represent the end word
	 * @return a specific string
	 */
	public static String Between(String STR , String FirstString, String LastString)
    {       
           
        String FinalString = StringUtils.substringBetween(STR, FirstString, LastString);
        
        return FinalString;
    }
	
	/**
	 * a function that returns the file path.
	 * @return returns a file location
	 */
	
	public String FilePath() {
		
		return "C:\\Users\\Baataoui Youssef\\Desktop\\text1.txt";
	}
}
