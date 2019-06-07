package manipal.com.present_manipal;

public class user_details {
    public String uname,email_id,regd_no;
    public String type,add_details;

    public user_details() {
    }

    public user_details(String uname, String email_id, String regd_no,String type,String add_details) {
        this.uname = uname;
        this.email_id = email_id;
        this.regd_no = regd_no;
        this.type=type;
        this.add_details=add_details;
    }

}
