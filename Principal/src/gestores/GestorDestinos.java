package gestores;

import modelo.DestinoTuristico;

/**
 * Administra el arreglo de todos los destinos turisticos del sistema.
 * Como DestinoTuristico no tiene un codigo unico propio, se identifica
 * cada destino por la combinacion de nombre y ciudad.
 */
public class GestorDestinos {

    private static final int MAX_DESTINOS = 100;

    private DestinoTuristico[] destinos;
    private int contador;

    public GestorDestinos() {
        destinos = new DestinoTuristico[MAX_DESTINOS];
        contador = 0;
    }

    
    public boolean registrar(DestinoTuristico d) {
        if (contador >= MAX_DESTINOS) return false;
        destinos[contador] = d;
        contador++;
        return true;
    }

    
    private int buscarIndice(String nombre, String ciudad) {
        for (int i = 0; i < contador; i++) {
            if (destinos[i].getNombre().equalsIgnoreCase(nombre)
                    && destinos[i].getCiudad().equalsIgnoreCase(ciudad)) {
                return i;
            }
        }
        return -1;
    }

    // Elimina un destino desplazando el resto del arreglo una posicion.
    public DestinoTuristico buscar(String nombre, String ciudad) {
        int indice = buscarIndice(nombre, ciudad);
        if (indice == -1) return null;
        return destinos[indice];
    }

  
    public boolean eliminar(String nombre, String ciudad) {
        int indice = buscarIndice(nombre, ciudad);
        if (indice == -1) return false;

        for (int i = indice; i < contador - 1; i++) {
            destinos[i] = destinos[i + 1];
        }
        destinos[contador - 1] = null;
        contador--;
        return true;
    }

    //Devuelve una copia del arreglo de destinos, recortada al tamano real.
    public DestinoTuristico[] listarTodos() {
        DestinoTuristico[] copia = new DestinoTuristico[contador];
        for (int i = 0; i < contador; i++) {
            copia[i] = destinos[i];
        }
        return copia;
    }

    public int getCantidadDestinos() {
        return contador;
    }
    
    // Actualiza los datos de un destino existente
    public boolean modificar(String nombreOriginal, String ciudadOriginal, DestinoTuristico destinoModificado) {
        int indice = buscarIndice(nombreOriginal, ciudadOriginal);
        if (indice == -1) return false; // No lo encontró
        
        // Reemplaza el objeto antiguo por el nuevo
        destinos[indice] = destinoModificado;
        return true;
    }
    
}