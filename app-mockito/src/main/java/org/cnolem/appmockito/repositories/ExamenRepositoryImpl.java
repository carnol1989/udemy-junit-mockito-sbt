package org.cnolem.appmockito.repositories;

import org.cnolem.appmockito.Datos;
import org.cnolem.appmockito.models.Examen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExamenRepositoryImpl implements ExamenRepository {

    @Override
    public Examen guardarExamen(Examen examen) {
        System.out.println("ExamenRepositoryImpl.guardarExamen");
        return Datos.EXAMEN;
    }

    @Override
    public List<Examen> findAll() {
        System.out.println("ExamenRepositoryImpl.findAll");
        try {
            System.out.println("ExamenRepositoryImpl");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Datos.EXAMENES;
    }

    @Override
    public List<Examen> findAllExamen() {
        //return Collections.emptyList();
        return Arrays.asList(new Examen(5L, "Matemáticas"), new Examen(6L, "Lenguaje"),
                new Examen(7L, "Historia"));
    }
}
