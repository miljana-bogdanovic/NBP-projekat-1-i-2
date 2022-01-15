package com.nbp.timelineservice.model.redis;

import java.io.Serializable;

public class TweetMessage implements Serializable {

    private String tweetId;

    private String username;

    private String body;

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public TweetMessage(final String tweetId, final String username, final String body) {
        this.tweetId = tweetId;
        this.username = username;
        this.body = body;
    }
}
