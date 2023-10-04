package org.cnolem.appmockito.services;

import org.cnolem.appmockito.models.Examen;
import org.cnolem.appmockito.repositories.ExamenRepository;
import org.cnolem.appmockito.repositories.PreguntaRepository;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService {

    private ExamenRepository examenRepository;
    private PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen> findExamenByNombreService(String nombre) {
        return examenRepository.findAll().stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();
        /*return examenRepository.findAllExamen().stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();*/
        /*Optional<Examen> examenOptional = examenRepository.findAllExamen().stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();
        Examen examen = null;
        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
        }
        return examen;*/
    }

    @Override
    public Examen findExamenByNombreWithPreguntasService(String nombre) {
        Optional<Examen> examenOptional = findExamenByNombreService(nombre);
        Examen examen = null;
        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasByExamenId(examen.getId());
            preguntaRepository.findPreguntasByExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardarExamenService(Examen examen) {
        if (!examen.getPreguntas().isEmpty()) {
            preguntaRepository.guardarVariasPreguntas(examen.getPreguntas());
        }
        return examenRepository.guardarExamen(examen);
    }

}
