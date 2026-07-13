package modelo;
/**
 * Empleado con acceso restringido: solo puede consultar itinerarios
 * y cupos disponibles, sin permisos de edicion. Rol "Operador".
 */

public class Operador extends Empleado {

    public Operador(String dni, String nombres, String apellidos,
                     String usuario, String contrasena) {
        super(dni, nombres, apellidos, usuario, contrasena, "Operador");
    }

    @Override
    public String mostrarPermisos() {
        return "Accede a itinerarios y cupos";
    }
}