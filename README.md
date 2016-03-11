# InformationRetrieval
CS 5364 Information Retrieval  

This project is capable of downloading tweets from twitter using Twitter 4J API. Once downloaded, they are preprocessed and using stanfordcore NLP 
sentiment score is calculated. At the end all these information are stored into the database.<br><br>

Tweet.java -> POJO for storing tweets information<br>
StanfordCoreNLPTool.java ->This class is capable of giving sentiment score of each tweet using StanfordCOre NLP library.<br>
Database.java -> This class is an interface to connect to the database.<br>
TwitterSentimentAnalysis.java -> initiates all process and downloades tweets from twitter.
