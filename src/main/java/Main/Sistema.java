package Main;

import Viajes.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Sistema {

    private static final String ARCHIVO_CONDUCTORES = "conductores.json";
    private static final String ARCHIVO_VIAJES = "viajes.json";

    private List<Conductor> conductores;
    private List<Viaje> viajes;
    private Gson gson;

    public Sistema() {
        // Configurar Gson con adaptador personalizado para Viaje
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Viaje.class, new ViajeTypeAdapter())
                .create();

        this.conductores = cargarConductores();
        this.viajes = cargarViajes();

        // Crear conductor por defecto si no existe
        if (conductores.isEmpty()) {
            crearConductorPorDefecto();
        }
    }

    // ========================================
    // ADAPTADOR PERSONALIZADO PARA VIAJE
    // ========================================
    private static class ViajeTypeAdapter implements JsonSerializer<Viaje>, JsonDeserializer<Viaje> {

        @Override
        public JsonElement serialize(Viaje viaje, Type type, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();

            // Guardar el tipo de viaje
            jsonObject.addProperty("tipo", viaje.tipo().name());

            // Serializar el objeto completo
            jsonObject.add("data", context.serialize(viaje));

            return jsonObject;
        }

        @Override
        public Viaje deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String tipoStr = jsonObject.get("tipo").getAsString();
            JsonElement data = jsonObject.get("data");

            // Deserializar según el tipo
            return switch (TipoViaje.valueOf(tipoStr)) {
                case Estandar -> context.deserialize(data, ViajeEstandar.class);
                case XL -> context.deserialize(data, ViajeXL.class);
                case Lujo -> context.deserialize(data, ViajeLujo.class);
            };
        }
    }

    // ========================================
    // CREAR CONDUCTOR POR DEFECTO
    // ========================================
    private void crearConductorPorDefecto() {
        Conductor conductorDefault = new Conductor("Juan Pérez", "juan.perez@gmail.com", true);
        conductores.add(conductorDefault);
        guardarConductores();
        System.out.println("✅ Conductor por defecto creado: " + conductorDefault.nombre());
    }

    // ========================================
    // OBTENER CONDUCTOR DISPONIBLE
    // ========================================
    public Conductor obtenerConductorDisponible() {
        return conductores.stream()
                .filter(Conductor::disponible)
                .findFirst()
                .orElse(null);
    }

    // ========================================
    // AGREGAR VIAJE
    // ========================================
    public void agregarViaje(Viaje viaje) {
        viajes.add(viaje);
        guardarViajes();
    }

    // ========================================
    // GUARDAR CONDUCTORES EN JSON
    // ========================================
    private void guardarConductores() {
        try (FileWriter writer = new FileWriter(ARCHIVO_CONDUCTORES)) {
            gson.toJson(conductores, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar conductores: " + e.getMessage());
        }
    }

    // ========================================
    // CARGAR CONDUCTORES DESDE JSON
    // ========================================
    private List<Conductor> cargarConductores() {
        File archivo = new File(ARCHIVO_CONDUCTORES);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(ARCHIVO_CONDUCTORES)) {
            Type listType = new TypeToken<ArrayList<Conductor>>(){}.getType();
            List<Conductor> conductoresCargados = gson.fromJson(reader, listType);
            return conductoresCargados != null ? conductoresCargados : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al cargar conductores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ========================================
    // GUARDAR VIAJES EN JSON
    // ========================================
    private void guardarViajes() {
        try (FileWriter writer = new FileWriter(ARCHIVO_VIAJES)) {
            gson.toJson(viajes, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar viajes: " + e.getMessage());
        }
    }

    // ========================================
    // CARGAR VIAJES DESDE JSON
    // ========================================
    private List<Viaje> cargarViajes() {
        File archivo = new File(ARCHIVO_VIAJES);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(ARCHIVO_VIAJES)) {
            Type listType = new TypeToken<ArrayList<Viaje>>(){}.getType();
            List<Viaje> viajesCargados = gson.fromJson(reader, listType);
            return viajesCargados != null ? viajesCargados : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al cargar viajes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ========================================
    // GETTERS
    // ========================================
    public List<Conductor> getConductores() {
        return new ArrayList<>(conductores);
    }

    public List<Viaje> getViajes() {
        return new ArrayList<>(viajes);
    }
}