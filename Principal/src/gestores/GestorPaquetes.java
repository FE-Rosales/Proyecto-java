package gestores;

import modelo.PaqueteTuristico;
import modelo.TipoPaquete;
import modelo.EstadoPaquete;

/**
 * Administra el arreglo de todos los paquetes turisticos del sistema.
 * Se identifica cada paquete por su codigo, que es unico.
 */
public class GestorPaquetes {

    private static final int MAX_PAQUETES = 200;

    private PaqueteTuristico[] paquetes;
    private int contador;

    public GestorPaquetes() {
        paquetes = new PaqueteTuristico[MAX_PAQUETES];
        contador = 0;
    }

    //Registra un nuevo paquete si aun hay espacio disponible. 
    public boolean registrar(PaqueteTuristico p) {
        if (contador >= MAX_PAQUETES) return false;
        paquetes[contador] = p;
        contador++;
        return true;
    }

    private int buscarIndicePorCodigo(String codigo) {
        for (int i = 0; i < contador; i++) {
            if (paquetes[i].getCodigo().equalsIgnoreCase(codigo)) {
                return i;
            }
        }
        return -1;
    }

    public PaqueteTuristico buscarPorCodigo(String codigo) {
        int indice = buscarIndicePorCodigo(codigo);
        if (indice == -1) return null;
        return paquetes[indice];
    }

    //Elimina un paquete desplazando el resto del arreglo una posicion. 
    public boolean eliminar(String codigo) {
        int indice = buscarIndicePorCodigo(codigo);
        if (indice == -1) return false;

        for (int i = indice; i < contador - 1; i++) {
            paquetes[i] = paquetes[i + 1];
        }
        paquetes[contador - 1] = null;
        contador--;
        return true;
    }

    public PaqueteTuristico[] listarTodos() {
        PaqueteTuristico[] copia = new PaqueteTuristico[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = paquetes[i];
        }
        return copia;
    }

    /**
     * Filtra los paquetes por tipo (Nacional, Aventura, etc).
     * Primero cuenta cuantos coinciden, luego arma un arreglo del
     * tamano exacto, porque no sabemos cuantos habra hasta contarlos.
     */
    public PaqueteTuristico[] listarPorTipo(TipoPaquete tipo) {
        int cantidad = 0;
        for (int i = 0; i < contador; i++) {
            if (paquetes[i].getTipo() == tipo) {
                cantidad++;
            }
        }

        PaqueteTuristico[] resultado = new PaqueteTuristico[cantidad];
        int j = 0;
        for (int i = 0; i < contador; i++) {
            if (paquetes[i].getTipo() == tipo) {
                resultado[j] = paquetes[i];
                j++;
            }
        }
        return resultado;
    }

    //Filtra solo los paquetes que siguen activos (con cupo disponible)
    public PaqueteTuristico[] listarActivos() {
        int cantidad = 0;
        for (int i = 0; i < contador; i++) {
            if (paquetes[i].getEstado() == EstadoPaquete.ACTIVO) {
                cantidad++;
            }
        }

        PaqueteTuristico[] resultado = new PaqueteTuristico[cantidad];
        int j = 0;
        for (int i = 0; i < contador; i++) {
            if (paquetes[i].getEstado() == EstadoPaquete.ACTIVO) {
                resultado[j] = paquetes[i];
                j++;
            }
        }
        return resultado;
    }

    public int getCantidadPaquetes() {
        return contador;
    }
}