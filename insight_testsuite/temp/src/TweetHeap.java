import java.util.ArrayList; 
/*
 * Class that dealing with storing tweet in heap min 
 * The reason using heap because the minimum always on root
 * and deleting/inserting minimum take log(n) operation only
 * Where n is number of tweet in graph
 *
 * This class verify old tweet based on time and discard it
 * if outside 60 second window
 * 
 * The maximum timestamp is kept in individual variable to verify
 * either new tweet is in order or out of order
 */
class TweetHeap{
    //Current index of ArrayList
    int idx=0;

    //heap of tweet 
    public ArrayList<Tweet> arrayTime;
    
    //Store Maximum value
    long maxTime=Long.MIN_VALUE; 

    long sixtysec=60000; 

    TweetHash tweetHash; 

    public TweetHeap(){
        arrayTime=new ArrayList<Tweet>(); 
        arrayTime.add(null);  //compensate for no zero index
    }
    
    //insert element in heap
    public void insert(Tweet tnumber){
        //verify if tweet is not discarded
        //based on time
        if(verifyTime(tnumber.getTimeTweet())){
            //verify exitance of graph
            if (tweetHash.insertHash(tnumber)){
                idx++;  
                arrayTime.add(tnumber);
                heapify(idx); 
                maxTime=Math.max(maxTime,tnumber.getTimeTweet());
                deleteOld(); 
            }
        }
    } 
    private boolean verifyTime(Long l){
        boolean output=false; 
        //verify empty
        if(idx==0){
            output=true; 
        }
        else if(maxTime<=l){ //in order
            output=true; 
        }
        else if(maxTime>l && (maxTime-l)<=sixtysec){ //out order
            output=true; 
        }
        else if(l==Long.MAX_VALUE){
            System.out.println("reaching MAX value of timestamps, use different data structure");
            output=true;  //should throw exception though in real world
        }
        return output; 
    }
    private void deleteOld(){ //deleting old tweet
        while(maxTime-arrayTime.get(1).getTimeTweet()>sixtysec){
            deleteTop(); 
        } 
    }

    //delete top element
    public void deleteTop(){
        if(idx==0){
            return; 
        }
        Tweet swap=arrayTime.get(idx);
        tweetHash.deleteTopHash(arrayTime.get(1)); 
        arrayTime.set(1,swap); 
        arrayTime.remove(idx); 
        idx--; 
        if(idx!=0){
            heapifyDelete(1,swap); 
        }
    }

    //percolate down
    private void heapifyDelete(int i,Tweet tweet){
        if (2*i>idx){ // at bottom
            arrayTime.set(i,tweet); 
        }
        else if(2*i == idx){ //only one leaf children
            if(arrayTime.get(2*i).getTimeTweet()<tweet.getTimeTweet()){
                arrayTime.set(i,arrayTime.get(2*i)); 
                arrayTime.set(2*i,tweet); 
            }
            else{
                arrayTime.set(i,tweet); 
            }
        }
        else if(2*i<idx){
            int j=0; 
            if(arrayTime.get(2*i).getTimeTweet()<arrayTime.get(2*i+1).getTimeTweet()){
                j=2*i;
            }
            else{
                j=2*i+1; 
            }
            if(arrayTime.get(j).getTimeTweet()<tweet.getTimeTweet()){
                arrayTime.set(i,arrayTime.get(j)); 
                heapifyDelete(j,tweet); 
            }
            else{
                arrayTime.set(i,tweet); 
            }
        }
    }

    //Do heapify to ensure root is minimum
    private void heapify(int i){
        int idxparent=i/2; 
        if(i==1){
            return;
        }
        //Do swap with its parent
        if(arrayTime.get(idxparent).getTimeTweet()>arrayTime.get(i).getTimeTweet()){
            Tweet swap=arrayTime.get(i);
            arrayTime.set(i,arrayTime.get(idxparent));
            arrayTime.set(idxparent,swap);
            heapify(idxparent);
        }
        else{
            return;
        }
    }
    public void prtheap(){
        for(int i=0;i<idx+1;i++){
            arrayTime.get(i).prthashtag(); 
        }
    }
    public void setTweetHash(TweetHash t){
        this.tweetHash=t; 
    }
}
