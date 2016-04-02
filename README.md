# DataEngCh

Source code intended to solve code challenge of insight data engineering. All main code is inside src folder. There are 4 main files and the files are structured with simple package so that it is easier to be reviewed.

### Library
I am using java library to handle JSON file. The jar "json-simple-1.1.1.jar" is included inside src/lib folder. Basically by downloading / cloning repo, all library is included and no need to download it from separate server. However, incase it were needed, I got the package from https://code.google.com/archive/p/json-simple/ 

### Files
- TweetMain.java : Main file, dealing with input/output file and parsing JSON
- TweetHeap.java : Class that dealing with storing tweet in heap min. The reason using heap because the minimum element is always on root and deleting/inserting take log(n) operation only, where n is number of tweet in graph. This class verify old tweet based on time and discard if outside 60 second window. The maximum timestamp is kept in individual variable to verify either tweet is in order or out of order. 
- Tweet.java : Class Tweet consist of timestamp and arraylist of hashtag string. It will always store unique hashtag only. 
- TweetHash.java : Class that represent graph in hashmap data structure. More detail in graph section    

### Graph
Graph representation is stored in HashMap data structure. The key is combination of hashtags and value is the count of that combination. 
Example :

 hashtag1=[Apache,Spark]
 key is 'ApacheSpark' and 'SparkApache', value = 1 ( only 1 ApacheSpark)
 
 hashtag2=[Apache,Spark,Storm]
 key are 'ApacheSpark' 'SparkApache', value = 2 ( it is added from before)
 'ApacheStorm' 'StormApache', value = 1
 'SparkStorm' 'StormSpark', value = 1

 Since the output is average degree, then only list of edge and vertices count is considered. Based on the handshaking lemma, average degree is (2 E/V). In this case, E is the size of HashMap edge list and V is the size of HashMap vertices
 
The reason use HashMap is because it has constant operation time. Therefore it is scalable for large number of data. 

### Run
I have tested in macintosh, all is functional properly. File run.sh has been configured to include all library
```sh
$ sh < run.sh
```
or
```sh
$ ./run.sh
```

I have tried, it passed testing "test-2-tweets-all-distinct". 
