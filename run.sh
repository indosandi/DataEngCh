javac -cp src/lib/json-simple-1.1.1.jar: -d src/bin/ src/*.java 
java -cp src/lib/json-simple-1.1.1.jar:src/bin/: TweetMain tweet_input/tweets.txt tweet_output/output.txt > src/bin/outputLinebyLine.txt
