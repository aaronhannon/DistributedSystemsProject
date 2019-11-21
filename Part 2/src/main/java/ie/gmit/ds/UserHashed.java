package ie.gmit.ds;

import com.fasterxml.jackson.annotation.JsonProperty;
//import com.sun.xml.txw2.annotation.XmlElement;
import javax.xml.bind.annotation.*;

@XmlRootElement(name= "user")
public class UserHashed {

    private int userId;
    private String username;
    private String email;
    private String hashedPassword;
    private String salt;

    public UserHashed() {
        super();
    }


    public UserHashed(int userId, String username, String email, String hashedPassword, String salt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
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
    public String getHashedPassword() {
        return hashedPassword;
    }

    @XmlElement
    @JsonProperty
    public String getSalt() {
        return salt;
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

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
