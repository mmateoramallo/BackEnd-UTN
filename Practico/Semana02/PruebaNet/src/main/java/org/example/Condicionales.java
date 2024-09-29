package org.example;

import java.util.Random;

public class Condicionales {
    public static void main(String[] args) {
        Random dado = new Random();

        System.out.println(dado.nextInt(6));
        switch (dado.nextInt()){
            case 1:
                System.out.println("uno");
                break;
            case 2:
                System.out.println("dos");
                break;
            case 3:
                System.out.println("tres");
                break;
            case 4:
                System.out.println("cuatro");
                break;
            case 5:
                System.out.println("cinco");
                break;
            case 6:
                System.out.println("seis");
                break;
        }
    }
}
