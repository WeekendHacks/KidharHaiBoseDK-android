package weekendhacks.com.kahahaibosedk;

/**
 * Created by melvin on 8/13/16.
 */
public class User {

    private String phone;
    private String fcm_id;
    private String name;

    public User(){
        super();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
