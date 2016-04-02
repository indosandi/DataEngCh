import java.util.HashMap; 
import java.util.HashSet; 
import java.util.ArrayList; 
import java.util.Map; 

/*
 * Graph representation is stored in HashMap data structure.
 * the key is combination of hashtags and value is the count of that combination
 * 
 * Example
 * hashtag1=[Apache,Spark]
 * key is 'ApacheSpark' and 'SparkApache', value = 1 ( only 1 ApacheSpark)
 * 
 * hashtag2=[Apache,Spark,Storm]
 * key are 'ApacheSpark' 'SparkApache', value = 2 ( it is added from before)
 * 'ApacheStorm' 'StormApache', value = 1
 * 'SparkStorm' 'StormSpark', value = 1
 *
 * Since the output is only average degree, then only list of edge and
 * vertices count is considered. Based on the handshaking lemma, average degree is (2 E/V)
 * In this case, E is the size of HashMap edge list and V is the size of HashMap vertices
 * 
 *The reason use HashMap is because it has constant operation time. Therefore it is scalable
 *for large number of data. 
 *
 */
class TweetHash{

   HashMap<String,Integer> edgeList=new HashMap<String,Integer>(); //list of edge
   HashMap<String,Integer> vertexCount=new HashMap<String,Integer>(); // Vertices count

   public TweetHash(){
   }

   //insert tweet and store the how many vertex
   //inside hashmap
   public boolean insertHash(Tweet t){
        int count=t.hashtag.size(); 
        boolean output=false; 
        if (count==1){  // not analyzing single hashtag
            return false; 
        }
        else if(count==0){ //not analyzing zero hashtag
            return false; 
        } 
        else if(count>1){ //hashtag more than one
            ArrayList<ArrayList<Integer>> arrij=checkEdgeInsertM(t); //Obtain each vertices between valid edge 
            ArrayList<Integer> idxCombI=arrij.get(0); // first vertex
            ArrayList<Integer> idxCombJ=arrij.get(1); // second vertex

                int asize=idxCombI.size(); 
                for (int i=0; i<asize;i++){
                    int iX=idxCombI.get(i); 
                    int iY=idxCombJ.get(i); 
                    if (vertexCount.containsKey(t.hashtag.get(iX))==false){
                        vertexCount.put(t.hashtag.get(iX),0);  //add vertex for firstime
                    }
                    if(vertexCount.containsKey(t.hashtag.get(iY))==false){
                        vertexCount.put(t.hashtag.get(iY),0); //add vertex for firstime
                    }
                    vertexCount.put(t.hashtag.get(iX),vertexCount.get(t.hashtag.get(iX))+1); 
                    vertexCount.put(t.hashtag.get(iY),vertexCount.get(t.hashtag.get(iY))+1); 
                }
               output=true;  
        }
        return output; 
   } 

   //Delete lowest Tweet from hashmap
   public void deleteTopHash(Tweet t){
        int count=t.hashtag.size(); 

        //intended for index of hashtag
        ArrayList<ArrayList<Integer>> arrij=removeEdge(t); 
        ArrayList<Integer> idxCombI=arrij.get(0); 
        ArrayList<Integer> idxCombJ=arrij.get(1); 

        int asize=idxCombI.size(); 
        for (int i=0; i<asize;i++){
            int iX=idxCombI.get(i); 
            int iY=idxCombJ.get(i); 
            
            vertexCount.put(t.hashtag.get(iX),vertexCount.get(t.hashtag.get(iX))-1); 
            vertexCount.put(t.hashtag.get(iY),vertexCount.get(t.hashtag.get(iY))-1); 
            if (vertexCount.get(t.hashtag.get(iX))==0){
                vertexCount.remove(t.hashtag.get(iX)); 
            }
            if (vertexCount.get(t.hashtag.get(iY))==0){
                vertexCount.remove(t.hashtag.get(iY)); 
            }
        }
    }
    
   //Obtain vertices,edge list to be deleted
    private ArrayList<ArrayList<Integer>>  removeEdge(Tweet t){
        int count=t.hashtag.size(); 

        //intented for index of hastag
        ArrayList<Integer> is=new ArrayList<Integer>(); 
        ArrayList<Integer> js=new ArrayList<Integer>(); 
         for(int i=0;i<count;i++){
            for(int j=i+1;j<count;j++){
                String s1=t.hashtag.get(i); 
                String s2=t.hashtag.get(j); 
                String pair=s1+s2; 
                String pair2=s2+s1; 

                //exist already, should reduce
                if (edgeList.containsKey(pair) || edgeList.containsKey(pair2)){
                   edgeList.put(pair,edgeList.get(pair)-1);  
                   edgeList.put(pair2,edgeList.get(pair2)-1);  
                   is.add(i);
                   js.add(j); 
                   if(edgeList.get(pair)==0){
                        edgeList.remove(pair); 
                   }
                   if(edgeList.get(pair2)==0){
                        edgeList.remove(pair2); 
                   }
                }
                else{ //no hashmap, do nothing 
                }
            }
        }
        ArrayList<ArrayList<Integer>> ijs=new ArrayList<ArrayList<Integer>>(); 
        ijs.add(is); 
        ijs.add(js); 
        return ijs; 
    }

   // returning valid combination of edge,vertices
   private ArrayList<ArrayList<Integer>> checkEdgeInsertM(Tweet t){
        int count=t.hashtag.size(); 

        //intended for index of hashtag
        ArrayList<Integer> is=new ArrayList<Integer>(); 
        ArrayList<Integer> js=new ArrayList<Integer>(); 
        
        //checking pair entity
        for(int i=0;i<count;i++){
            for(int j=i+1;j<count;j++){
                String s1=t.hashtag.get(i); 
                String s2=t.hashtag.get(j); 
                String pair=s1+s2; 
                String pair2=s2+s1; 

                //exist already
                if (edgeList.containsKey(pair) || edgeList.containsKey(pair2)){
                    edgeList.put(pair,edgeList.get(pair)+1); 
                    edgeList.put(pair2,edgeList.get(pair2)+1); 
                    is.add(i);
                    js.add(j); 
                }
                else{ //no exist, should add
                    edgeList.put(pair,1); 
                    edgeList.put(pair2,1); 
                    is.add(i);
                    js.add(j); 
                }
            }
        }
        ArrayList<ArrayList<Integer>> ijs=new ArrayList<ArrayList<Integer>>(); 
        ijs.add(is); 
        ijs.add(js); 
        return ijs; 
   }
   
   public void sizeNode(){
       System.out.println("vertex="+vertexCount.size());
   }
   public void sizeEdge(){
       System.out.println("edge="+edgeList.size());
   }
    public void ave(){
       System.out.println(edgeList.size()/vertexCount.size());
   }
   public int getSizeNode(){
       return vertexCount.size();
   }
   public int getSizeEdge(){
        return edgeList.size(); 
   }
   public float getAve(){
        if (vertexCount.size()==0){
            return 0.0f; 
        }
        else{
            float edge=(float) edgeList.size(); 
            float vertex=(float) vertexCount.size(); 
            return (float)(edge/vertex); 
        }
   }
   public void prtHash(){

       for (Map.Entry<String, Integer> entry : vertexCount.entrySet()) {
           String key = entry.getKey();
           System.out.println(key+"="+vertexCount.get(key)); 
       }
   }
}
