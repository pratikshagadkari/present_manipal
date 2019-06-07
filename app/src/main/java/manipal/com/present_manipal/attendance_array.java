package manipal.com.present_manipal;

public class attendance_array {
    double pre,tot,per;
    String sub,number,email,name,sub1,add_details;
    String regd;
    public attendance_array(String sub,double pre, double tot, double per,String number,String email,String regd,String name,String add_details) {
        this.sub=sub;
        this.pre = pre;
        this.tot = tot;
        this.per = per;
        this.number=number;
        this.email=email;
        this.regd=regd;
        this.name=name;
        this.add_details=add_details;
    }
}
