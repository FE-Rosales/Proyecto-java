package modelo;
/**
 * Promocion que descuenta una cantidad fija de dinero, sin importar
 * el precio del paquete.
 */
import java.time.LocalDate;

public class DescuentoMontoFijo extends Promocion {

    private double montoFijo;

    public DescuentoMontoFijo(String codigo, LocalDate fechaInicio, LocalDate fechaFin,
                               double montoFijo) {
        super(codigo, fechaInicio, fechaFin);
        this.montoFijo = montoFijo;
    }
  /**
     * Usa Math.min() para evitar que el descuento supere el precio
     * del paquete y el resultado quede en negativo.
     */
    @Override
    public double calcularDescuento(double precio) {
        return Math.min(montoFijo, precio);
    }
}