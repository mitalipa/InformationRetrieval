/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittersentimentanalysis;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author Rishmit
 */
public class StanfordCoreNLPTool {

    static StanfordCoreNLP pipeline;

    public static void init() {
        //pipeline = new StanfordCoreNLP("MyPropFile.properties");
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);

    }

    public static int findSentiment(String tweet) {

        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            int sumOfSentimentScore = 0;
            int noOfSentences = 0;
            Annotation annotation = pipeline.process(processSentence(tweet));
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) 
            {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                sumOfSentimentScore+=sentiment;
                noOfSentences++;
                System.out.println("-------------**********------------------");
                System.out.println(sentence.toString()+" = "+sentiment);
                System.out.println("Tree : "+tree.toString());
                System.out.println("-------------**********-----------------");
                //StanfordCoreNLP considers the sentiment score of the longest statement in the tweet
                /*if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }*/
                //Our Action : We are considering the average of all the statements to get the approximate sentiment score
                
            }
            if(noOfSentences!=0)
                mainSentiment = sumOfSentimentScore / noOfSentences;
            else
                mainSentiment = -1;
        }
        return mainSentiment;
    }

    //This method will process the sentence before finding sentiment analysis of the statement
    // 1. Need to remove https://........ from the statement as it doens't contains any emotions
    // 2. Need to work on the hash Tags and @ words
    // 3. Need to check the RT word and VIDEO: words
    private static String processSentence(String tweet) 
    {
        //regex for finding url starting with https
        String regex = "\\b(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        tweet = tweet.replaceAll(regex, "");
        return tweet;
    }
}
