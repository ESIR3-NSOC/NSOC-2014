package com.example.esir.nsoc2014.regulator.regulation;
import java.util.Scanner;

/**
 * Created by Administrateur on 06/02/2015.
 */
public class testRegulator {

    public static void main(String[] argz){
        System.out.println("test");
        Regulator reg = new Regulator();
        reg.run();
        reg.setConsigne(20);
        Scanner keyboard = new Scanner(System.in);
        int temp = 10;
        while(temp!=999) {
            temp= keyboard.nextInt();
            System.out.println(temp);

        }
    }

}
