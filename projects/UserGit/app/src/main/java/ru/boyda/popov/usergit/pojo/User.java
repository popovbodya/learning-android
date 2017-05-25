package ru.boyda.popov.usergit.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("login")
    private String username;

    @JsonProperty("url")
    private String url;

    @JsonProperty("score")
    private double score;

    @JsonProperty("avatar_url")
    private String avatarUrl;


    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (Double.compare(user.score, score) != 0) return false;
        if (username != null ? !username.equals(user.username) : user.username != null)
            return false;
        if (url != null ? !url.equals(user.url) : user.url != null) return false;
        return avatarUrl != null ? avatarUrl.equals(user.avatarUrl) : user.avatarUrl == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = username != null ? username.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (avatarUrl != null ? avatarUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", url='" + url + '\'' +
                ", score=" + score +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
