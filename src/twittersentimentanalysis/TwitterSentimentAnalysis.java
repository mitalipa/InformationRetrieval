/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittersentimentanalysis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import twitter4j.conf.ConfigurationBuilder;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import twitter4j.GeoLocation;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


/**
 *
 * @author Rishmit
 */
public class TwitterSentimentAnalysis {

    /**
     * @param args the command line arguments
     */
    public final static String EMPTY = "";
    static Logger logger = Logger.getLogger("MyLog");
    static FileHandler fh;
    public static void main(String[] args) {
        
        try {
            fh = new FileHandler("C:/Mitali/Logs/MyLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter); 
            logger.info("Twitter Sentiment Analysis Started!!!");
            ConfigurationBuilder cb;
            Twitter twitter;
            cb = getAuthentication();
            twitter = new TwitterFactory(cb.build()).getInstance();
            ArrayList<Tweet> listOfTweets = getAllTweets(twitter);
             System.out.println("Size : "+listOfTweets.size());
            int result = insertIntoDatabase(listOfTweets);
            logger.info("Number of tweets added = "+result);
        } catch (IOException ex) {
            Logger.getLogger(TwitterSentimentAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            logger.info(ex.getMessage());
        } catch (SecurityException ex) {
            Logger.getLogger(TwitterSentimentAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            logger.info(ex.getMessage());
        }

    }
    

    //Authentication with the twitter
    private static ConfigurationBuilder getAuthentication() {
        logger.info("getAuthentication()");
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("9BT6FbCsTcytgbwJ7VxAyQD8M");
        cb.setOAuthConsumerSecret("xF5UK7HODhAm0oazHHN69DAcxheVv6FhMRQMII6XEaTy8tm8un");
        cb.setOAuthAccessToken("1085852592-3RvAULajehASiHR4vzLTgfL42PGdzrvzXmhXR3X");
        cb.setOAuthAccessTokenSecret("M6gDdcTLwsPTKHTyDxlHklCPJ0X3lR44xPa6ItCfXfTim");
        return cb;
    }

    //get all the tweets
    private static ArrayList<Tweet> getAllTweets(Twitter twitter) {
        logger.info("getAllTweets");
        List<Status> listTweets = null;
        ArrayList<Tweet> listOfTweets = new ArrayList<Tweet>();
        try {
            Trends trends = twitter.getPlaceTrends(23424977);
            // System.out.println(trends.getLocation());
            Trend trend[] = trends.getTrends();
            logger.info("Number of Trends : "+trend.length);
            for (Trend trendTemp : trend) {
                //System.out.println("Name = " + trendTemp.getName());
                //System.out.println("***************");
                Query query = new Query(trendTemp.getQuery());
                query.setCount(100);
                QueryResult queryResult = twitter.search(query);
                listTweets = queryResult.getTweets();
                StanfordCoreNLPTool.init();
                for (Status status : listTweets) {
                    //System.out.println(status.getText());
                    Tweet tweet = getTweetObject(status);
                    if(tweet!=null)
                    {
                        tweet.setTrend(trendTemp.getName());
                        listOfTweets.add(tweet);
                    }
                }
            }
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterSentimentAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            logger.info(ex.getMessage());
        }
        return listOfTweets;
    }

    private static Tweet getTweetObject(Status status) {
        Tweet tweet = new Tweet();
        int sentimentScore = StanfordCoreNLPTool.findSentiment(status.getText());
        if(sentimentScore!=-1)
        {
            tweet.setDateTime(status.getCreatedAt());
            tweet.setTweetText(status.getText());
            tweet.setUsername(status.getUser().getScreenName());
            tweet.setSentimentScore(sentimentScore);
            GeoLocation geoLocation = status.getGeoLocation();
            if(geoLocation!=null)
            {
                tweet.setLongitude(geoLocation.getLongitude()+"");
                tweet.setLatitude(geoLocation.getLatitude()+"");
            }
            else
            {
                tweet.setLongitude(null);//
                tweet.setLatitude(null);//
            }
            Place place = status.getPlace();
            if (place != null) {
                tweet.setCountry(place.getCountry());
                tweet.setPlace(place.getFullName());
            } 
            else 
            {
                tweet.setCountry(null);//
                tweet.setPlace(null);//
            }
        }
        else
            tweet = null;
        return tweet;
    }

    private static int insertIntoDatabase(ArrayList<Tweet> listTweets) {
        int result = -1;
        try {
            Connection databaseConnection = Database.getDatabaseConnection();
            if (databaseConnection != null) {
                System.out.println("Database connection successful!!!!");
                result = Database.insertIntoDatabase(databaseConnection, listTweets);
                if (result < 0) {
                    System.out.println("An Error has been Occurred while inserting into the database");
                } else {
                    System.out.println(result + " Tweets Added!!!");
                }
            } else {
                System.out.println("Database NOT Connected!!");
            }

        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(TwitterSentimentAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            logger.info(ex.getMessage());
        }
        return result;
    }
}
