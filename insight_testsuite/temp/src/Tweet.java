import java.util.HashSet; 
import java.util.Set;
import java.util.List; 
import java.util.ArrayList; 

/* Class Tweet consist of timestamp and arraylist of hashtag string
 * It will always store unique hashtag only
 */
class Tweet{
    long timeTweet;
    public ArrayList<String> hashtag; 

    public Tweet(long l, ArrayList<String> s){
        timeTweet=l;
        hashtag=rmDuplicate(s); 
    }

    public long getTimeTweet(){
        return timeTweet; 
    }
    
    // removing duplicate hashtag
    private ArrayList<String> rmDuplicate(ArrayList<String> s){
        Set<String> uniSet = new HashSet<String>(s);
        ArrayList<String> uniList = new ArrayList<String>();
        for (String str : uniSet){
            uniList.add(str); 
        }
        return uniList; 
    }

    public void prthashtag(){
        for(int i=0;i<hashtag.size();i++){
            System.out.print(hashtag.get(i)+","); 
        }
        System.out.println(""); 
    }

}
