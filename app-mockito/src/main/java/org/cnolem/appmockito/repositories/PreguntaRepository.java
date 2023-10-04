package org.cnolem.appmockito.repositories;

import java.util.List;

public interface PreguntaRepository {

    List<String> findPreguntasByExamenId(Long id);
    void guardarVariasPreguntas(List<String> preguntas);

}
