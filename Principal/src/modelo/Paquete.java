/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author FABIAN
 */
public class Paquete {
    
    private String nombre;
    private double precioBase;
    private Promocion promocion; // Aquí vive la promoción que creamos

    // Constructor, getters, setters...

    public double getPrecioFinal() {
        // El paquete "le pregunta" a la promoción si está vigente
        if (promocion != null && promocion.estaVigente(LocalDate.now())) {
            return precioBase - promocion.calcularDescuento(precioBase);
        }
        return precioBase; // Si no hay promo, devuelve el precio normal
    }
}
