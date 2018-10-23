/**
 * Created by G on 01/03/2016.
 */
public class TestOutput {

    public static void main(String args[]){
       // float t0=6*7;
        //float t1=2+t0;
        //float t2=t1+3;
        //float result = t2;
        /*float t0=5-2;
        float t1=10-30;
        float t2=t1+20;
        float t3=t2/20;
        float t5 =9*t3;
*/
        float t0=5-2;
        float t1=7*t0;
        float t2=10-30;
        float t3=9*t2;
        float t4=t3+20;
        float t5=t1+t4;
        float t6=4+t5;
        float t7=t6/20;
        float t9 =3*t7;

        System.out.println("NORMAL RESULT---->"+(float)(3*(4+7*(5-2)+9*(10-30)+20)/20));
        System.out.println("RESULT OF COMPILER---->"+t9);

    }

}
