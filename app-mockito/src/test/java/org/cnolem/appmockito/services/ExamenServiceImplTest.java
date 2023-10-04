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

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)//Habilita las anotaciones para no hacerlo en el setUp
class ExamenServiceImplTest {

    @Mock
    ExamenRepositoryImpl repository;
    @Mock
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;//Inyecta las referencias por constructor

    @Captor
    ArgumentCaptor<Long> captor;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);//Habilitar anotaciones para esta clase
        /*repository = mock(ExamenRepository.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(repository, preguntaRepository);*/
    }

    @Test
    void findByExamenPorNombre() {
        repository = new ExamenRepositoryImpl();
        preguntaRepository = new PreguntaRepositoryImpl();
        service = new ExamenServiceImpl(repository, preguntaRepository);
        Optional<Examen> examen = service.findExamenByNombreService("Matemáticas");
        assertNotNull(examen);
        assertEquals(5L, examen.get().getId());
        assertEquals("Matemáticas", examen.get().getNombre());
    }

    @Test
    @DisplayName("Find By Examen Por Nombre (Mockito)")
    void findByExamenPorNombreMockito() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES);
        Optional<Examen> examen = service.findExamenByNombreService("Maths");

        //assertNotNull(examen);
        assertTrue(examen.isPresent());
        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Maths", examen.get().getNombre());
    }

    @Test
    @DisplayName("Find By Examen Por Nombre lista vacia")
    void findByExamenPorNombreListaVacia() {
        List<Examen> datos = Collections.emptyList();

        when(repository.findAllExamen()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenByNombreService("Maths");

        //assertNotNull(examen);
        assertFalse(examen.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNombreWithPreguntasService("Maths");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    void testPreguntasExamenVerify() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNombreWithPreguntasService("Maths");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        verify(repository).findAllExamen();
        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        when(repository.findAllExamen()).thenReturn(Collections.emptyList());
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = service.findExamenByNombreWithPreguntasService("Maths 2");

        assertNull(examen);

        verify(repository).findAllExamen();
        verify(preguntaRepository).findPreguntasByExamenId(5L);
    }

    @Test
    void testGuardarExamen() {
        //Given -> Precondiciones de nuestro entorno de pruebas
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        //when(repository.guardarExamen(any(Examen.class))).thenReturn(Datos.EXAMEN);
        when(repository.guardarExamen(any(Examen.class))).then(new Answer<Examen>(){
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        //When -> Cuando ejecutamos un metodo real del service que queremos probar
        Examen examen = service.guardarExamenService(newExamen);

        //Then -> Entonces validamos / probamos
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Physical", examen.getNombre());

        verify(repository).guardarExamen(any(Examen.class));
        verify(preguntaRepository).guardarVariasPreguntas(anyList());
    }

    @Test
    void testManejoException() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES_ID_NULL);
        when(preguntaRepository.findPreguntasByExamenId(isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
           service.findExamenByNombreWithPreguntasService("Maths");
        });
        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(repository).findAllExamen();
        verify(preguntaRepository).findPreguntasByExamenId(isNull());
    }

    @Test
    void testArgumentMatchers() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNombreWithPreguntasService("Lenguage");

        verify(repository).findAllExamen();
        //verify(preguntaRepository).findPreguntasByExamenId(ArgumentMatchers.argThat(arg -> arg!=null && arg.equals(5L)));
        verify(preguntaRepository).findPreguntasByExamenId(ArgumentMatchers.argThat(arg -> arg!=null && arg >= 6L));
//        verify(preguntaRepository).findPreguntasByExamenId(eq(5L));
    }

    @Test
    void testArgumentMatchers2() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNombreWithPreguntasService("Maths");

        verify(repository).findAllExamen();
        verify(preguntaRepository).findPreguntasByExamenId(argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentMatchers3() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNombreWithPreguntasService("Maths");

        verify(repository).findAllExamen();
        verify(preguntaRepository).findPreguntasByExamenId(argThat((argument) -> argument != null && argument > 0));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {

        private Long argumento;

        @Override
        public boolean matches(Long argument) {
            this.argumento = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "Es para un msj personalizado de error que imprime " +
                    "mockito en caso de que falle el test " + argumento +
                    " debe ser un entero positivo";
        }
    }

    @Test
    void testArgumentCaptor() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        service.findExamenByNombreWithPreguntasService("Maths");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findPreguntasByExamenId(captor.capture());

        assertEquals(5L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVariasPreguntas(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardarExamenService(examen);
        });
    }

    @Test
    void testDoAnswer() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        doAnswer(invocation -> {
           Long id = invocation.getArgument(0);
           return id == 5L ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasByExamenId(anyLong());

        Examen examen = service.findExamenByNombreWithPreguntasService("Maths");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("geometria"));
        assertEquals(5L, examen.getId());
        assertEquals("Maths", examen.getNombre());

        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }

    @Test
    void testDoAnswerGuardarExamen() {
        //Given -> Precondiciones de nuestro entorno de pruebas
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        //when(repository.guardarExamen(any(Examen.class))).thenReturn(Datos.EXAMEN);
        doAnswer(new Answer() {
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(repository).guardarExamen(any(Examen.class));

        //When -> Cuando ejecutamos un metodo real del service que queremos probar
        Examen examen = service.guardarExamenService(newExamen);

        //Then -> Entonces validamos / probamos
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Physical", examen.getNombre());

        verify(repository).guardarExamen(any(Examen.class));
        verify(preguntaRepository).guardarVariasPreguntas(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(repository.findAllExamen()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        doCallRealMethod().when(preguntaRepository).findPreguntasByExamenId(anyLong());

        Examen examen = service.findExamenByNombreWithPreguntasService("Maths");

        assertEquals(5L, examen.getId());
        assertEquals("Maths", examen.getNombre());
    }

    @Test
    void testSpy() {
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);

        List<String> preguntas = Arrays.asList("aritmética");
        doReturn(preguntas).when(preguntaRepository).findPreguntasByExamenId(anyLong());
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
//        when(preguntaRepository.findPreguntasByExamenId(anyLong())).thenReturn(preguntas);

        Examen examen = examenService.findExamenByNombreWithPreguntasService("Maths");

        assertEquals(5, examen.getId());
        assertEquals("Maths", examen.getNombre());
        assertEquals(1, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasByExamenId(anyLong());
    }
}