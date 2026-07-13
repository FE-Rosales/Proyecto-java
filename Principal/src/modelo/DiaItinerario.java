package modelo;
/**
 * Representa las actividades planificadas para un dia especifico
 * dentro del itinerario de un paquete turistico (ej. "Dia 1: llegada
 * y traslado"). No existe fuera del paquete al que pertenece.
 */
public class DiaItinerario {

    private int numeroDia;
    private String actividades;

    public DiaItinerario(int numeroDia, String actividades) {
        this.numeroDia = numeroDia;
        this.actividades = actividades;
    }

    public int getNumeroDia() {
        return numeroDia;
    }

    public void setNumeroDia(int numeroDia) {
        this.numeroDia = numeroDia;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }



    @Override
    public String toString() {
        return "Día " + numeroDia + ": " + actividades;
    }
}