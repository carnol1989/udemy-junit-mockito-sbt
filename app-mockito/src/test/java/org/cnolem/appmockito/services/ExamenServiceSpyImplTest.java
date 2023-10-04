package org.cnolem.appmockito.services;

import org.cnolem.appmockito.Datos;
import org.cnolem.appmockito.models.Examen;
import org.cnolem.appmockito.repositories.ExamenRepository;
import org.cnolem.appmockito.repositories.ExamenRepositoryImpl;
import org.cnolem.appmockito.repositories.PreguntaRepository;
import org.cnolem.appmockito.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)//Habilita las anotaciones para no hacerlo en el setUp
class ExamenServiceSpyImplTest {

    @Spy
    ExamenRepositoryImpl repository;
    @Spy
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;//Inyecta las referencias por constructor

    @Test
    void testSpy() {
        List<String> preguntas = Arrays.asList("aritmética");
        doReturn(preguntas).when(preguntaRepository).findPreguntasByExamenId(anyLong());
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(preguntas);

        Examen examen = service.findExamenByNombreWithPreguntasService("Maths");

        assertEquals(5, examen.getId());
        assertEquals("Maths", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testOrdenDeInvocaciones() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenByNombreWithPreguntasService("Maths");
        service.findExamenByNombreWithPreguntasService("Language");

        InOrder inOrder = inOrder(preguntaRepository);
        inOrder.verify(preguntaRepository).findPreguntasByExamenId(5L);
        inOrder.verify(preguntaRepository).findPreguntasByExamenId(6L);
    }

    @Test
    void testOrdenDeInvocaciones2() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenByNombreWithPreguntasService("Maths");
        service.findExamenByNombreWithPreguntasService("Language");

        InOrder inOrder = inOrder(repository, preguntaRepository);
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasByExamenId(5L);
        inOrder.verify(repository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasByExamenId(6L);
    }

    @Test
    void testNumeroDeInvocaciones() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenByNombreWithPreguntasService("Maths");

        verify(preguntaRepository).findPreguntasByExamenId(5L);
        verify(preguntaRepository, times(1)).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atLeast(1)).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atLeastOnce()).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atMost(10)).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atMostOnce()).findPreguntasByExamenId(5L);
    }

    @Test
    void testNumeroDeInvocaciones2() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenByNombreWithPreguntasService("Maths");

//        verify(preguntaRepository).findPreguntasByExamenId(5L);
        verify(preguntaRepository, times(2)).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atLeast(2)).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atLeastOnce()).findPreguntasByExamenId(5L);
        verify(preguntaRepository, atMost(20)).findPreguntasByExamenId(5L);
//        verify(preguntaRepository, atMostOnce()).findPreguntasByExamenId(5L);
    }

    @Test
    void testNumeroInvocaciones3() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        service.findExamenByNombreWithPreguntasService("Maths");

        verify(preguntaRepository, never()).findPreguntasByExamenId(5L);
        verifyNoInteractions(preguntaRepository);

        verify(repository).findAll();
        verify(repository, times(1)).findAll();
        verify(repository, atLeast(1)).findAll();
        verify(repository, atLeastOnce()).findAll();
        verify(repository, atMost(1)).findAll();
        verify(repository, atMostOnce()).findAll();
    }
}