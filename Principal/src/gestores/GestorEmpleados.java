package gestores;

import modelo.Empleado;

/**
 * Administra el arreglo de todos los empleados del sistema.
 * Permite registrar, eliminar, buscar y validar el login de empleados.
 */
public class GestorEmpleados {

    private static final int MAX_EMPLEADOS = 50;

    private Empleado[] empleados;
    private int contador;

    public GestorEmpleados() {
        empleados = new Empleado[MAX_EMPLEADOS];
        contador = 0;
        
        registrar(new modelo.Administrador("11111111", "Luis", "Admin", "admin", "admin123"));
        registrar(new modelo.AsesorDeViaje("22222222", "Ana", "Asesor", "asesor", "asesor123"));
        registrar(new modelo.Operador("33333333", "Luis", "Operador", "operador", "ope123"));
    }

    //Registra un nuevo empleado 
    public boolean registrar(Empleado e) {
        if (contador >= MAX_EMPLEADOS) return false;
        empleados[contador] = e;
        contador++;
        return true;
    }

    // Busca la posicion de un empleado por su DNI
    private int buscarIndicePorDni(String dni) {
        for (int i = 0; i < contador; i++) {
            if (empleados[i].getDni().equals(dni)) {
                return i;
            }
        }
        return -1;
    }

    // Busca y devuelve un empleado por su DNI, o null si no lo encuentra.
    public Empleado buscarPorDni(String dni) {
        int indice = buscarIndicePorDni(dni);
        if (indice == -1) return null;
        return empleados[indice];
    }

    /**
     * Elimina un empleado por su DNI, recorriendo el arreglo y
     * desplazando una posicion hacia la izquierda todos los elementos
     * que estaban despues del eliminado.
     */
    public boolean eliminar(String dni) {
        int indice = buscarIndicePorDni(dni);
        if (indice == -1) return false;

        for (int i = indice; i < contador - 1; i++) {
            empleados[i] = empleados[i + 1];
        }
        empleados[contador - 1] = null;
        contador--;
        return true;
    }

    /**
     * Recorre todos los empleados registrados y verifica si alguno
     * tiene esas credenciales. Usado por la pantalla de login.
     * @return el Empleado que inicio sesion correctamente, o null si
     * ningun empleado coincide con esas credenciales
     */
    public Empleado validarLogin(String usuario, String contrasena) {
        for (int i = 0; i < contador; i++) {
            if (empleados[i].login(usuario, contrasena)) {
                return empleados[i];
            }
        }
        return null;
    }

    //Devuelve una copia del arreglo de empleados, recortada al tamano real
    public Empleado[] listarTodos() {
        Empleado[] copia = new Empleado[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = empleados[i];
        }
        return copia;
    }

    public int getCantidadEmpleados() {
        return contador;
    }
}