package ie.gmit.ds;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
public class UserValidate {

    private int id;
    private String password;

    public UserValidate() {
    }

    public UserValidate(int id, String password) {
        this.id = id;
        this.password = password;
    }

    @XmlElement
    @JsonProperty
    public int getId() {
        return id;
    }

    @XmlElement
    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
