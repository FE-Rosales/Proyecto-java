package modelo;
/**
 * Estados posibles de un paquete turistico. ACTIVO significa que se
 * puede seguir reservando; AGOTADO que ya no hay cupos; CANCELADO
 * que el Administrador lo dio de baja manualmente.
 */
public enum EstadoPaquete {
    ACTIVO, AGOTADO, CANCELADO
}