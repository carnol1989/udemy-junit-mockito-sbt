package org.cnolem.appmockito.repositories;

import org.cnolem.appmockito.models.Examen;

import java.util.List;

public interface ExamenRepository {

    Examen guardarExamen(Examen examen);
    List<Examen> findAll();
    List<Examen> findAllExamen();

}
