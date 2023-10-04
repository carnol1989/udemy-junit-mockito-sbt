package org.cnolem.appmockito.services;

import org.cnolem.appmockito.models.Examen;

import java.util.Optional;

public interface ExamenService {

    Optional<Examen> findExamenByNombreService(String nombre);
    Examen findExamenByNombreWithPreguntasService(String nombre);
    Examen guardarExamenService(Examen examen);
}
