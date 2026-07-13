package modelo;
/**
 * Representa un servicio especifico incluido dentro de un paquete
 * turistico (ej. vuelo, hotel, traslado, visita guiada, seguro de viaje).
 * No tiene sentido fuera del paquete al que pertenece (composicion en el UML).
 */
public class ServicioIncluido {

    private String descripcion;
    private String tipoServicio; // ej. Vuelo, Hotel, Traslado, Visita guiada, Seguro

    public ServicioIncluido(String descripcion, String tipoServicio) {
        this.descripcion = descripcion;
        this.tipoServicio = tipoServicio;
    }

    public String getDescripcion() { return descripcion; }
    public String getTipoServicio() { return tipoServicio; }

    @Override
    public String toString() {
        return tipoServicio + ": " + descripcion;
    }
}