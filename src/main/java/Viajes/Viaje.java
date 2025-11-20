package Viajes;

import Main.Cliente;
import Main.Conductor;
import Main.Ruta;

import java.io.Serializable;

public sealed abstract class Viaje implements Serializable
        permits ViajeEstandar, ViajeXL, ViajeLujo {

    protected Ruta ruta;
    protected Cliente cliente;
    protected Conductor conductor;
    protected TipoViaje tipo;

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

    public Ruta ruta() { return ruta; }
    public Cliente cliente() { return cliente; }
    public Conductor conductor() { return conductor; }
    public TipoViaje tipo() {return tipo;}
}