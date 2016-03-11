/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittersentimentanalysis;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Status;
import static twittersentimentanalysis.TwitterSentimentAnalysis.logger;

/**
 *
 * @author Rishmit
 */
public class Database {
    
    //get the database connection
    public static Connection getDatabaseConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String host = "ssttuttsa.cxrrvl79fcbm.us-west-2.rds.amazonaws.com";
        String username = "ttu_ttsa";
        String password = "rishabh01";
        int port = 1433;
        String jdbcUrl = "jdbc:sqlserver://" + host + ":" +port ;
        Connection con = null;
        try
        {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        con = DriverManager.getConnection(jdbcUrl,username,password);         
        }
        catch (SQLException ex)
        {
            System.out.println("SQLException : "+ex.getMessage());
            ex.printStackTrace();
            logger.info(ex.getMessage());
        }
       return con;         
    }
    /*
    CREATE TABLE [dbo].[tbTweetStaging](
       [trend] [varchar](255) NULL,
       [tweetText] [varchar](1000) NULL,
       [userName] [varchar](255) NULL,
       [dateTime] [varchar](14) NULL,
       [location] [varchar](255) NULL,
       [sentimentScore] [int] NULL
        ) ON [PRIMARY]

    */
    //return 0 if unsuccessful else return positive number represents number of rows added into the database
    public static int insertIntoDatabase(Connection conn , ArrayList<Tweet> listTweets) throws SQLException
    {
        int result = 0;
        for(Tweet tweet : listTweets)
        {
        PreparedStatement statement = conn.prepareStatement("insert into [dbttsa].[dbo].[tbTweetStaging] (trend,tweetText,userName,dateTime,sentimentScore,longitude,latitude,place,country) values(?,?,?,?,?,?,?,?,?)");
        statement.setString(1, tweet.getTrend());
        statement.setString(2, tweet.getTweetText());
        statement.setString(3, tweet.getUsername());
        statement.setString(4, tweet.getDateTime().toString());
        statement.setInt(5, tweet.getSentimentScore());
        statement.setString(6, tweet.getLongitude());
        statement.setString(7, tweet.getLatitude());
        statement.setString(8, tweet.getPlace());
        statement.setString(9, tweet.getCountry());
        System.out.println(tweet.toString());
        result+=statement.executeUpdate();
        }
        return result;

    }
    
    
}
