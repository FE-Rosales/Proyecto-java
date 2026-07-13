package modelo;

/**
 * Clase abstracta que representa cualquier tipo de descuento aplicable
 * a un paquete turistico. Es la clase padre de DescuentoPorcentaje,
 * DescuentoMontoFijo y DescuentoTemporada.
 */

import java.time.LocalDate;

public abstract class Promocion {

    protected String codigo;
    protected LocalDate fechaInicio;
    protected LocalDate fechaFin;

    public Promocion(String codigo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.codigo = codigo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public abstract double calcularDescuento(double precio);

    public boolean estaVigente(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    public String getCodigo() { return codigo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
}