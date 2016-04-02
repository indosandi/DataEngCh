import java.io.PrintWriter; 
import java.util.Calendar; 
import java.util.Date; 
import java.text.SimpleDateFormat; 
import java.util.ArrayList; 
import java.io.FileReader;
import java.util.Iterator;
import java.io.File; 
import java.io.FileInputStream;
import java.io.InputStreamReader; 
import java.io.BufferedReader;
import java.io.IOException; 

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 
/*
 * Main file dealing with opening file, parsing JSON
 * and output file
 *@author swibowo
 */ 
public class TweetMain{
    FileInputStream file; 
    BufferedReader br; 

    static String FILE_INPUT="../tweet_input/tweets.txt";// default input file name
    static String FILENAME_OUT="../tweet_output/output.txt"; //default output file name


    //Constants
    static String CREATED="created_at";
    static String ENTITIES="entities";
    static String HASHTAGS="hashtags";
    static String HASHTAGS_TEXT="text";
    static String UNICODE="UTF-8"; 
    static String FORMAT_OUT="%.2f"; 

    TweetHash tweetHash;  // Storing hashtags in hashmap
    TweetHeap tweetHeap;  // Storing tweet in min heap

    //Date format in created_at
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd kk:mm:ss Z yyyy");
    PrintWriter writer; 
    public static void main(String[] args) {

        // read input and output path
        FILE_INPUT=args[0]; 
        FILENAME_OUT=args[1]; 

        //run main program
        TweetMain tm=new TweetMain();  
        tm.readInput(); 
    }

    private void readInput(){

        tweetHeap=new TweetHeap(); 
        tweetHash=new TweetHash(); 
        tweetHeap.setTweetHash(tweetHash); 
        try {
             //opening up file
             FileInputStream file = new FileInputStream(FILE_INPUT);
             BufferedReader br = new BufferedReader(new InputStreamReader(file));
             PrintWriter writer = new PrintWriter(FILENAME_OUT, UNICODE); 

             //reading file line by line
             String tweetLine; 
             while( (tweetLine=br.readLine()) != null){

                 //parsing JSON tweet
                 JSONObject jsonObject = (JSONObject) new JSONParser().parse(tweetLine);
                 String createdAt =(String) jsonObject.get(CREATED); 
                 long timestamp=convertCreatedToLong(createdAt);  
                 
                 // if there is no created_at or failed to parsing
                 // then timestamps set to lowest value and tweet is skiped
                 // assumption here is, tweet start after MIN_VALUE
                 if (timestamp==Long.MIN_VALUE){
                     continue; 
                 }
                 else{
                    timestamp=convertCreatedToLong(createdAt);  
                 }

                ArrayList<String> hashtags=hashArray(jsonObject); 
                Tweet tweet=new Tweet(timestamp,hashtags); 
                tweetHeap.insert(tweet); 

                //writing output out
                System.out.println(String.format(FORMAT_OUT,tweetHash.getAve()));
                writer.println(String.format(FORMAT_OUT,tweetHash.getAve()));

                }
                writer.close(); 
                br.close();
             } catch(IOException e) {
                    e.printStackTrace();
                    System.out.println("fail Opening file");
             } catch(ParseException e){
                    System.out.println("fail parsing JSON file");
             }
    }
    
    // convert time into long timestamps
    private long convertCreatedToLong(String s){
            long output; 
            try {
                    Date d=sdf.parse(s); 
                    Calendar c = Calendar.getInstance();
                    c.setTime(d);
                    output = c.getTimeInMillis(); 
                    //System.out.println(" succesfully converted created_at to long "); 
                } catch (NullPointerException e){ //no created at, timestamps is set to lowest value
                    output=Long.MIN_VALUE; 
                    System.out.println("created_at is null, skip line"); 
                } catch (java.text.ParseException e){ //fail parsing, timestamps is set to lowest value
                    output=Long.MIN_VALUE; 
                    System.out.println("Parsing created_at is failed, skip tweet"); 
                }
            return output; 
    }

    //Extract JSONObject entities into ArrayList of Hashtags
    public ArrayList<String> hashArray(JSONObject j){
        ArrayList<String> output=new ArrayList<String>(); 
        JSONObject entity= (JSONObject) j.get(ENTITIES); 

        JSONArray hashtags=(JSONArray) entity.get(HASHTAGS); 
        for (int i=0;i<hashtags.size();i++){
            JSONObject jobj=(JSONObject) hashtags.get(i); 
            String s=(String) jobj.get(HASHTAGS_TEXT); 
            output.add(s); 
        }
        return output; 

    }
    private void prtList(ArrayList l){
        for(int i=0;i<l.size();i++){
            System.out.print(","+l.get(i));
        }
    }
}
