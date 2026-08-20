/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.frc.isi.dba.creditos.app.objetos;

import java.util.UUID;

import lombok.Data;

/**
 *
 * @author Mateo
 */
@Data
public class Cuenta {

    private String nombre;
    private double saldo;
    private String codigo;
    private NivelCrediticio nivelCrediticio;

    public Cuenta(String nombre, double saldo) {
        this.nombre = nombre;
        this.saldo = saldo;

        //Generar el codigo
        // 1. Crear un objeto UUID
        UUID uuid = UUID.randomUUID();

        // 2. Convertir ese objeto a String
        String codigoGenerado = uuid.toString();

        this.codigo = codigoGenerado;

        //Asignar nivel crediticio
        actualizarNivel();
    }

    private void actualizarNivel() {
        if (this.saldo < 0) {
            this.nivelCrediticio = NivelCrediticio.MOROSO;
        } else if (this.saldo > 0 && this.saldo < 300000) {
            this.nivelCrediticio = NivelCrediticio.BRONCE;
        } else if (this.saldo >= 300000 && this.saldo < 1000000) {
            this.nivelCrediticio = NivelCrediticio.PLATA;
        } else {
            this.nivelCrediticio = NivelCrediticio.ORO;

        }
    }

    //Metodo para mostrar los datos de la cuenta toString()
    @Override
    public String toString() {
        return "Nombre: " + nombre + " Saldo: " + saldo + " Codigo: " + codigo + " Nivel Crediticio: " + nivelCrediticio;
    }
}
