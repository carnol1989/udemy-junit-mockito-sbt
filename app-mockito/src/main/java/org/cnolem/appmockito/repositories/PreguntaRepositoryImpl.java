package org.cnolem.appmockito.repositories;

import org.cnolem.appmockito.Datos;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntaRepositoryImpl implements PreguntaRepository {
    @Override
    public List<String> findPreguntasByExamenId(Long id) {
        System.out.println("PreguntaRepositoryImpl.findPreguntasByExamenId");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVariasPreguntas(List<String> preguntas) {
        System.out.println("PreguntaRepositoryImpl.guardarVariasPreguntas");
    }
}
