package modelo;
/**
 * Promocion que descuenta un porcentaje del precio.
 * Ejemplo: porcentaje = 0.15 equivale a 15% de descuento.
 */

import java.time.LocalDate;

public class DescuentoPorcentaje extends Promocion {

    private double porcentaje; // ej. 0.15 = 15%

    public DescuentoPorcentaje(String codigo, LocalDate fechaInicio, LocalDate fechaFin,
                                double porcentaje) {
        super(codigo, fechaInicio, fechaFin);
        this.porcentaje = porcentaje;
    }

    @Override
    public double calcularDescuento(double precio) {
        return precio * porcentaje;
    }
}