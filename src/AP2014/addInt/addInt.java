package AP2014.addInt;

import java.io.InputStream;

public class addInt {

    public static void main(String[] args) {

        MyInt[] num = new MyInt[2];

        if (args.length != 2)
            if (args.length < 2)
                for (int i = 0; i < 2; i++) {
                    System.out.println("Enter number "+(i+1)+" :");
                    num[i]=MyInt.fromString(MyReader.safeReadString(System.in,256));
                }
            else
                System.err.println("Invalid number of arguments");
        else
            for (int i = 0; i < 2; i++)
                    num[i] = MyInt.fromString(args[i]);

        MyInt sum=new MyInt();
        MyInt mul=MyInt.fromString("1");

        for (int i = 0; i < 2; i++) {
            sum.add(num[i]);
            mul.multiply(num[i]);
        }

        System.out.println();
        System.out.println("Sum : " + sum);
        System.out.println("Multiplication : " + mul);
    }


}

class MyInt {
    byte[] digits;//Stored in reverse order
    int digits_count;

    private MyInt (int size) {
        digits=new byte[size];
        digits_count=0;
    }

    public MyInt() {
        this(256);
    }

    public static MyInt fromString (String num) {
        MyInt myInt=new MyInt();
        myInt.digits_count=num.length();
        //TODO : negative number sign....

        for(int i=0;i<myInt.digits_count;i++)
            myInt.digits[myInt.digits_count-i-1]=
                    (byte)(num.charAt(i)-'0');

        myInt.align();
        return myInt;
    }


    public void add(MyInt num) {
        if(num.digits_count>digits_count)
            digits_count=num.digits_count;

        for(int i=0;i<digits_count;i++) {
            digits[i]+=num.digits[i];
            digits[i+1]+=digits[i]/10;
            digits[i]%=10;
        }

        align();
    }

    public void multiply (MyInt num) {
        MyInt res=new MyInt();

        for(int j=0;j<num.digits_count;j++)
            for(int i=0;i<digits_count;i++) {
                res.digits[i+j]+=(byte)(digits[i]*num.digits[j]);
                res.digits[i+1+j]+=res.digits[i]/10;
                res.digits[i+j]%=10;
            }

        res.align();
        res.copyTo(this);
    }

    public void copyTo (MyInt dst)
    {
        dst.digits=digits;
        dst.digits_count=digits_count;
    }

    public void setZero() {
        digits_count=0;
        for(int i=0;i<digits.length;i++)
            digits[i]=0;
    }

    private void align () {
        int i;

        for (i = 0; i < digits.length &&
                digits[i] != 0; i++);

        if (i == digits.length - 1) {
            digits_count=0;
            return;
        }

        digits_count=i;

        while (digits[digits_count]!=0)
            digits_count++;
    }

    @Override
    public String toString () {
        if(digits_count==0)
            return "0";

        String r="";
        for(int i=digits_count-1;i>=0;i--)
            r+=(char)(digits[i]+'0');

        return r;
    }

}

class MyReader {

    public static String getStringRaw(int maxLen, InputStream inputStream) throws Exception {

        byte[] r = new byte[maxLen];
        int i;
        int read;

        //Skip to first readable char
        do
            read = inputStream.read();
        while (isEmptyChar(read));

        for (i = 0; i < maxLen; i++) {
            if (!isEmptyChar(read)) {
                r[i] = (byte) read;
            } else break;
            read = inputStream.read();
        }
        //and last empty char will skipped too ...

        String s = "";
        for (int j = 0; j < i; j++)
            s += (char) r[j];
        return s;
    }

    public static String getStringRaw(int maxLen) throws Exception {
        return getStringRaw(maxLen, System.in);
    }

    public static String GetStringRaw() throws Exception {
        return getStringRaw(256);
    }

    static boolean isEmptyChar(int c) {
        return (c == ' ' || c == '\n' || c == 10 || c == 13);
        //windows console sends 13,10 but *unix terminal sends only 10 !
    }


    public static char safeRead(InputStream s) {
        try {
            return (char) s.read();
        } catch (Exception e) {
            return (char) -1;
        }
    }

    public static String safeReadString(InputStream s, int max) {
        try {
            return getStringRaw(max, s).toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static int safeReadInt(InputStream s) {
        return Integer.parseInt(safeReadString(s, 10));
    }

}


