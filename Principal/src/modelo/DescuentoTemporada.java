
package modelo;
/**
 * Promocion asociada a una temporada especifica (ej. "Verano", "Fiestas Patrias"),
 * que aplica un porcentaje de descuento propio de esa temporada.
 */
import java.time.LocalDate;

public class DescuentoTemporada extends Promocion {

    private String temporada;
    private double porcentajeTemporada;

    public DescuentoTemporada(String codigo, LocalDate fechaInicio, LocalDate fechaFin,
                               String temporada, double porcentajeTemporada) {
        super(codigo, fechaInicio, fechaFin);
        this.temporada = temporada;
        this.porcentajeTemporada = porcentajeTemporada;
    }

    @Override
    public double calcularDescuento(double precio) {
        return precio * porcentajeTemporada;
    }

    public String getTemporada() { return temporada; }
}