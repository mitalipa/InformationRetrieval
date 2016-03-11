/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittersentimentanalysis;

import java.util.Date;

/**
 *
 * @author Rishmit
 */
/*
Table structure : 
    CREATE TABLE [dbo].[tbTweetStaging](
       [trend] [varchar](255) NULL,
       [tweetText] [varchar](1000) NULL,
       [userName] [varchar](255) NULL,
       [dateTime] [varchar](14) NULL,
       [location] [varchar](255) NULL,
       [sentimentScore] [int] NULL
        ) ON [PRIMARY]

    */
public class Tweet {
    private String trend;
    private String tweetText;
    private String username;
    private Date dateTime;
    private int sentimentScore;
    private String longitude;
    private String latitude;
    private String place;
    private String country;

    public Tweet(String trend, String tweetText, String username, Date dateTime, int sentimentScore, String longitude, String latitude, String place, String country) {
        this.trend = trend;
        this.tweetText = tweetText;
        this.username = username;
        this.dateTime = dateTime;
        this.sentimentScore = sentimentScore;
        this.longitude = longitude;
        this.latitude = latitude;
        this.place = place;
        this.country = country;
    }

    public Tweet() {
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(int sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Tweet{" + "trend=" + trend + ", tweetText=" + tweetText + ", username=" + username + ", dateTime=" + dateTime + ", sentimentScore=" + sentimentScore + ", longitude=" + longitude + ", latitude=" + latitude + ", place=" + place + ", country=" + country + '}';
    }
    
    
    
}
