package manipal.com.present_manipal;
import java.util.Random;
public class random_string_generate {

    public void run()
        {
            generate_string();
        }
        public String generate_string()
        {
            String s="";
            int i;
            for(i=0;i<6;i++)//length of the string
            {
                double ch=Math.random();//random number between 0-1
                int ch1=(int)Math.round(ch);//rounding the number to the nearest
                if(ch1==0)
                {
                    int n=(int)(Math.random()*(10));
                    s+=n;
                }
                else
                {
                    Random r = new Random();
                    char c = (char)(r.nextInt(26) + 'a');
                    s+=c;
                }
            }
            return s;
        }

    }

