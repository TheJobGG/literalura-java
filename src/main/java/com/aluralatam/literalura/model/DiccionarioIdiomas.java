package com.aluralatam.literalura.model;

import java.util.HashMap;
import java.util.Map;

public class DiccionarioIdiomas {
    private static final Map<String, String> IDIOMAS = new HashMap<>();

    static {
        IDIOMAS.put("es", "Español");
        IDIOMAS.put("en", "Inglés");
        IDIOMAS.put("fr", "Francés");
        IDIOMAS.put("de", "Alemán");
        IDIOMAS.put("it", "Italiano");
        IDIOMAS.put("pt", "Portugués");
    }

    public static String obtenerNombreDelIdioma(String codigoIdioma) {
        return IDIOMAS.getOrDefault(codigoIdioma, "Nombre de idioma no encontrado...");
    }
}
