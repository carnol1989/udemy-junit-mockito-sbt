package org.cnolem.appmockito;

import org.cnolem.appmockito.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {

    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Maths"), new Examen(6L, "Language"),
            new Examen(7L, "History"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Maths"), new Examen(null, "Language"),
            new Examen(null, "History"));

    public final static List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(new Examen(-5L, "Maths"), new Examen(-7L, "Language"),
            new Examen(null, "History"));

    public final static List<String> PREGUNTAS = Arrays.asList("aritmética", "integrales", "derivadas",
            "trigonometria", "geometria");

    public final static Examen EXAMEN = new Examen(null, "Physical");

}
