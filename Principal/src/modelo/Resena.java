package modelo;

import java.time.LocalDate;

public class Resena {
/**
 * Representa la calificacion y comentario que un cliente deja sobre
 * un paquete turistico despues de haber viajado. Funcionalidad
 * adicional del proyecto (requerimiento de personalizacion).
 */
    private int calificacion; // de 1 a 54
    private String comentario;
    private LocalDate fecha;
    private Reserva reserva;
    private PaqueteTuristico paquete;

    public Resena(Reserva reserva, int calificacion, String comentario) {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificacion debe estar entre 1 y 5");
        }
        this.reserva = reserva;
        this.paquete = reserva.getPaquete();
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = LocalDate.now();
    }

    public boolean esPositiva() {
        return calificacion >= 4;
    }

    public int getCalificacion() {
        return calificacion; 
    }
    public String getComentario() {
        return comentario;
    }
    public LocalDate getFecha() {
        return fecha; 
    }
    public Reserva getReserva() { 
        return reserva; 
    }
    public PaqueteTuristico getPaquete() {
        return paquete; 
    }

    @Override
    public String toString() {
        return paquete.getNombre() + " - " + calificacion + "/5 - \"" + comentario + "\"";
    }
}