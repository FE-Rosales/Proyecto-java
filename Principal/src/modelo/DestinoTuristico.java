package modelo;
/**
 * Representa un destino turistico (ciudad o lugar) que puede ser incluido
 * en uno o mas paquetes turisticos. Un destino existe independientemente
 * de los paquetes que lo incluyan (relacion de agregacion en el UML).
 */
public class DestinoTuristico {

    private String nombre;
    private String pais;
    private String ciudad;
    private String descripcion;
    private String clima;
    private String idiomaPrincipal;
    private String rutaImagen;
    

    public DestinoTuristico(String nombre, String pais, String ciudad,
                             String descripcion, String clima,
                             String idiomaPrincipal, String rutaImagen) {
        this.nombre = nombre;
        this.pais = pais;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.clima = clima;
        this.idiomaPrincipal = idiomaPrincipal;
        this.rutaImagen = rutaImagen;
    }

    public String getNombre() { 
        return nombre; 
    }
    public String getPais() {
        return pais; 
    }
    public String getCiudad() {
        return ciudad; 
    }
    public String getDescripcion() {
        return descripcion; 
    }
    public String getClima() {
        return clima; 
    }
    public String getIdiomaPrincipal() {
        return idiomaPrincipal; 
    }
    public String getRutaImagen() {
        return rutaImagen; 
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion; 
    }
    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen; 
    }

    @Override
    public String toString() {
        return nombre + ", " + ciudad + " - " + pais;
    }
}