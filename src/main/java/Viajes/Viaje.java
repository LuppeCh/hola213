package Viajes;

import Main.Cliente;
import Main.Conductor;
import Main.Ruta;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

// =======================================================
// ANOTACIONES DE JACKSON PARA MANEJAR POLIMORFISMO
// =======================================================
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME, // Usar el nombre de la subclase
        include = JsonTypeInfo.As.PROPERTY, // Incluir la información como una propiedad en el JSON
        property = "tipoClase" // Nombre de la propiedad que contendrá el tipo (ej: "tipoClase": "Estandar")
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ViajeEstandar.class, name = "Estandar"), // Mapea la subclase ViajeEstandar
        @JsonSubTypes.Type(value = ViajeXL.class, name = "XL"),           // Mapea la subclase ViajeXL
        @JsonSubTypes.Type(value = ViajeLujo.class, name = "Lujo")         // Mapea la subclase ViajeLujo
})
public sealed abstract class Viaje implements Serializable
        permits ViajeEstandar, ViajeXL, ViajeLujo {

    protected Ruta ruta;
    protected Cliente cliente;
    protected Conductor conductor;
    protected TipoViaje tipo;

    // CONSTRUCTOR REQUERIDO POR JACKSON PARA DESERIALIZACIÓN (si no es un Java Record)
    // Aunque es una clase abstracta, Jackson necesita este constructor para inicializar
    // los campos antes de pasarlos al constructor de la subclase.
    // Si usas Java Records, Jackson lo maneja automáticamente.
    public Viaje() {
    }

    public Viaje(Ruta ruta, Cliente cliente, Conductor conductor, TipoViaje tipo) {
        this.ruta = ruta;
        this.cliente = cliente;
        this.conductor = conductor;
        this.tipo = tipo;
    }

    public double calcularCosto(double tarifaBasePorKm) {
        double kms = ruta.distanciaMetros() / 1000.0;
        double precioBase = kms * tarifaBasePorKm;
        return tipo.calcularPrecio(precioBase);
    }

    // Jackson usa los métodos getters para la serialización
    public Ruta getRuta() { return ruta; }
    public Cliente getCliente() { return cliente; }
    public Conductor getConductor() { return conductor; }
    public TipoViaje getTipo() {return tipo;}

    // Si quieres mantener los métodos estilo record (que ya tenías):
    public Ruta ruta() { return ruta; }
    public Cliente cliente() { return cliente; }
    public Conductor conductor() { return conductor; }
    public TipoViaje tipo() {return tipo;}
}