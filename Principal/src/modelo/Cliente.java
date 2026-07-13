package modelo;
/**
 * Representa a un cliente de la agencia que puede realizar una o mas
 * reservas de paquetes turisticos.
 */
import java.time.LocalDate;
import java.time.Period;

public class Cliente {

    private String documento; // DNI o pasaporte
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String telefono;
    private String correo;

    public Cliente(String documento, String nombres, String apellidos,
                    LocalDate fechaNacimiento, String nacionalidad,
                    String telefono, String correo) {
        this.documento = documento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.telefono = telefono;
        this.correo = correo;
    }

    public int calcularEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
 /**
     * Util para el calculo del precio de una reserva, ya que los
     * pasajeros menores de edad reciben un descuento.
     */
    public boolean esMenorDeEdad() {
        return calcularEdad() < 18;
    }

    public String getDocumento() {
        return documento; 
    }
    public String getNombres() {
        return nombres; 
    }
    public String getApellidos() {
        return apellidos; 
    }
    public LocalDate getFechaNacimiento() { 
        return fechaNacimiento; 
    }
    public String getNacionalidad() {
        return nacionalidad; 
    }
    public String getTelefono() {
        return telefono; 
    }
    public String getCorreo() {
        return correo; 
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono; 
    }
    public void setCorreo(String correo) {
        this.correo = correo; 
    }

    @Override
    public String toString() {
        return nombres + " " + apellidos + " (" + documento + ")";
    }
}
