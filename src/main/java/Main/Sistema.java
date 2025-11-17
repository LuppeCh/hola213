package Main;

import Viajes.Viaje;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Sistema implements Serializable {

    private List<Usuario> usuarios = new ArrayList<>();
    private List<Viaje> viajes = new ArrayList<>();

    public void agregarUsuario(Usuario u) { usuarios.add(u); }
    public void agregarViaje(Viaje v) { viajes.add(v); }

    public void guardar() throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("sistema.dat"))) {
            out.writeObject(this);
        }
    }

    public static Sistema cargar() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("sistema.dat"))) {
            return (Sistema) in.readObject();
        }
    }
}