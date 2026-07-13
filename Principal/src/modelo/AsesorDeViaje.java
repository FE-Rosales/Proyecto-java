package modelo;
/**
 * Empleado encargado de la atencion al cliente: gestiona clientes,
 * crea reservas y registra pagos. Corresponde al rol "Asesor de Viaje".
 */

public class AsesorDeViaje extends Empleado {

    public AsesorDeViaje(String dni, String nombres, String apellidos,
                          String usuario, String contrasena) {
        super(dni, nombres, apellidos, usuario, contrasena, "Asesor de Viaje");
    }

    @Override
    public String mostrarPermisos() {
        return "Gestiona clientes, reservas y pagos";
    }
}