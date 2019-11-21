package ie.gmit.ds;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@XmlRootElement(name= "user")
public class User {
    @NotNull
    private int userId;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;


    public User() {
        super();
    }

    public User(int userId, String username, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }


    @XmlElement
    @JsonProperty
    public int getUserId() {
        return userId;
    }

    @XmlElement
    @JsonProperty
    public String getUsername() {
        return username;
    }

    @XmlElement
    @JsonProperty
    public String getEmail() {
        return email;
    }

    @XmlElement
    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
