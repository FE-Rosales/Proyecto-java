package modelo;
/**
 * Representa un movimiento de pago individual dentro de una Reserva.
 * No tiene setters: una vez registrado, un pago no deberia modificarse.
 */
import java.time.LocalDate;

public class Pago {

    private double monto;
    private TipoPago tipoPago;
    private MetodoPago metodo;
    private LocalDate fecha;

    public Pago(double monto, TipoPago tipoPago, MetodoPago metodo, LocalDate fecha) {
        this.monto = monto;
        this.tipoPago = tipoPago;
        this.metodo = metodo;
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto; 
    }
    public TipoPago getTipoPago() {
        return tipoPago; 
    }
    public MetodoPago getMetodo() {
        return metodo; 
    }
    public LocalDate getFecha() {
        return fecha; 
    }

    @Override
    public String toString() {
        return fecha + " - " + tipoPago + " (" + metodo + "): S/ " + monto;
    }
}