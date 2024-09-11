package org.example;

public class TiposDeDatos {
    public static void main(String[] args) {
        //Definicionn de tipo de datos
        byte miByte = 10; //Bytes ocupados 1
        short miShort = 20;// Bytes ocupados 2
        int miInt = 30; // Bytes ocupados 4
        long miLong = 40; // Bytes ocupados 8

        //Tipos de datos decimales
        float miFloat = 50.5f; // Bytes ocupados 4
        double miDouble = 60.5; // Bytes ocupados 8

        //Tipo de dato char = Numero sin signo representqa el codigo de un caracter
        char miChar = 'A'; // Bytes ocupados 2
        boolean miBoolean = false; // Bytes ocupados 1
        String miString = "HolaMundo"; //Bytes ocupados 12

        //Mostrar todos los valores y sus tipos de datos, en un solo System.out.println
        System.out.println("miByte: " + miByte + " tipo de dato: " + ((Object)miByte).getClass().getSimpleName());
        System.out.println("miShort: " + miShort + " tipo de dato: " + ((Object)miShort).getClass().getSimpleName());
        System.out.println("miInt: " + miInt + " tipo de dato: " + ((Object)miInt).getClass().getSimpleName());

        long miSegLong = 1000000000;
        short miSegShort;
        //miSegShort = miSegLong;//Error de compilacion
    }
}