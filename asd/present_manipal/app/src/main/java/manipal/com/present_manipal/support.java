package manipal.com.present_manipal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class support extends AppCompatActivity {
    EditText sub,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        sub=findViewById(R.id.sup_sub);
        message=findViewById(R.id.sup_message);
    }

    public void sendMail(View view) {
        String sub1=sub.getText().toString();
        String to[]={"help.presentmanipal@gmail.com"};
        String message1=message.getText().toString();
        Intent em=new Intent(Intent.ACTION_SEND);
        em.putExtra(Intent.EXTRA_EMAIL,to);
        em.putExtra(Intent.EXTRA_SUBJECT,sub1);
        em.putExtra(Intent.EXTRA_TEXT,message1);
        em.setType("message/rfc822");
        startActivity(Intent.createChooser(em,"Send Email"));

    }
}
