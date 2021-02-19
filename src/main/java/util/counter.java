package util;

import java.util.Scanner;

public class counter {
     int h;
     int m;
     int s;

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        StringBuffer stringBuffer=new StringBuffer();
        counter c=new counter();
        while (true){
            c.addM(scanner.nextInt());
            c.addS(scanner.nextInt());
            stringBuffer.setLength(0);
            stringBuffer.append(c.h>10?c.h+":":"0"+c.h+":");
            stringBuffer.append(c.m>10?c.m+":":"0"+c.m+":");
            stringBuffer.append(c.s>10?c.s:"0"+c.s);
            System.out.println(stringBuffer);
        }
    }
    public void addS(Integer s){
        this.s+=s;
        if(this.s>=60){
            this.s-=60;
            addM(1);
        }
    }
    public void addM(Integer m){
        this.m+=m;
        if(this.m>=60){
            this.m-=60;
            this.h+=1;
        }
    }
}
