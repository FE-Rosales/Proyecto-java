package gestores;

import modelo.Cliente;

/**
 * Administra el arreglo de todos los clientes registrados en el sistema.
 * Se identifica cada cliente por su documento (DNI o pasaporte).
 */
public class GestorClientes {

    private static final int MAX_CLIENTES = 500;

    private Cliente[] clientes;
    private int contador;

    public GestorClientes() {
        clientes = new Cliente[MAX_CLIENTES];
        contador = 0;
    }

    // Registra un nuevo cliente si aun hay espacio disponible.
    public boolean registrar(Cliente c) {
        if (contador >= MAX_CLIENTES) return false;
        clientes[contador] = c;
        contador++;
        return true;
    }

    private int buscarIndicePorDocumento(String documento) {
        for (int i = 0; i < contador; i++) {
            if (clientes[i].getDocumento().equalsIgnoreCase(documento)) {
                return i;
            }
        }
        return -1;
    }

    public Cliente buscarPorDocumento(String documento) {
        int indice = buscarIndicePorDocumento(documento);
        if (indice == -1) return null;
        return clientes[indice];
    }

    //Elimina un cliente desplazando el resto del arreglo una posicion. 
    public boolean eliminar(String documento) {
        int indice = buscarIndicePorDocumento(documento);
        if (indice == -1) return false;

        for (int i = indice; i < contador - 1; i++) {
            clientes[i] = clientes[i + 1];
        }
        clientes[contador - 1] = null;
        contador--;
        return true;
    }

    public Cliente[] listarTodos() {
        Cliente[] copia = new Cliente[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = clientes[i];
        }
        return copia;
    }

    public int getCantidadClientes() {
        return contador;
    }
    
    // Método para actualizar los datos de un cliente existente
    public boolean modificar(Cliente clienteActualizado) {
        int indice = buscarIndicePorDocumento(clienteActualizado.getDocumento());
        if (indice == -1) return false; // Retorna falso si el DNI no existe en el arreglo
        
        // Si lo encuentra, reemplaza el objeto antiguo por el nuevo
        clientes[indice] = clienteActualizado; 
        return true;
    }
}