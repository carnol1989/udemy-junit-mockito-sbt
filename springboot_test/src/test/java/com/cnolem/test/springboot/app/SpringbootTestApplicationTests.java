package com.cnolem.test.springboot.app;

import com.cnolem.test.springboot.app.exception.DineroInsuficienteException;
import com.cnolem.test.springboot.app.models.Banco;
import com.cnolem.test.springboot.app.models.Cuenta;
import com.cnolem.test.springboot.app.repositories.BancoRepository;
import com.cnolem.test.springboot.app.repositories.CuentaRepository;
import com.cnolem.test.springboot.app.services.CuentaService;
import com.cnolem.test.springboot.app.services.CuentaServiceImpl;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.cnolem.test.springboot.app.Datos.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

	//@Mock
	@MockBean
	CuentaRepository cuentaRepository;

	//@Mock
	@MockBean
	BancoRepository bancoRepository;

	//@InjectMocks
	@Autowired
//	CuentaServiceImpl service;
	CuentaService service;

	@BeforeEach
	void setUp() {
		/*cuentaRepository = mock(CuentaRepository.class);
		bancoRepository = mock(BancoRepository.class);
		service = new CuentaServiceImpl(cuentaRepository, bancoRepository);*/
		/*Datos.CUENTA_001.setSaldo(new BigDecimal("1000"));
		Datos.CUENTA_002.setSaldo(new BigDecimal("2000"));
		Datos.BANCO.setTotalTransferencia(0);*/
	}

	@Test
	void contextLoads() {
		//when(cuentaRepository.findById(1L)).thenReturn(Datos.CUENTA_001);
		//when(cuentaRepository.findById(1L)).thenReturn(Datos.crearCuenta001());
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		//when(cuentaRepository.findById(2L)).thenReturn(Datos.CUENTA_002);
		//when(cuentaRepository.findById(2L)).thenReturn(Datos.crearCuenta002());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		//when(bancoRepository.findById(1L)).thenReturn(Datos.BANCO);
		//when(bancoRepository.findById(1L)).thenReturn(Datos.crerBanco());
		when(bancoRepository.findById(1L)).thenReturn(crerBanco());

		BigDecimal saldoCuentaOrigen = service.revisarSaldo(1L);
		BigDecimal saldoCuentaDestino = service.revisarSaldo(2L);

		assertEquals("1000", saldoCuentaOrigen.toPlainString());
		assertEquals("2000", saldoCuentaDestino.toPlainString());

		service.transferir(1L, 2L, new BigDecimal("100"), 1L);

		saldoCuentaOrigen = service.revisarSaldo(1L);
		saldoCuentaDestino = service.revisarSaldo(2L);

		assertEquals("900", saldoCuentaOrigen.toPlainString());
		assertEquals("2100", saldoCuentaDestino.toPlainString());

		int total = service.revisarTotalTransferencias(1L);

		assertEquals(1, total);

		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(3)).findById(2L);
		verify(cuentaRepository, times(2)).save(any(Cuenta.class));

		verify(bancoRepository, times(2)).findById(1L);
		verify(bancoRepository).save(any(Banco.class));

		verify(cuentaRepository, times(6)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
	}

	@Test
	void contextLoads2() {
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());
		when(cuentaRepository.findById(2L)).thenReturn(crearCuenta002());
		when(bancoRepository.findById(1L)).thenReturn(crerBanco());

		BigDecimal saldoCuentaOrigen = service.revisarSaldo(1L);
		BigDecimal saldoCuentaDestino = service.revisarSaldo(2L);

		assertEquals("1000", saldoCuentaOrigen.toPlainString());
		assertEquals("2000", saldoCuentaDestino.toPlainString());

		assertThrows(DineroInsuficienteException.class, () -> {
			service.transferir(1L, 2L, new BigDecimal("1200"), 1L);
		});

		saldoCuentaOrigen = service.revisarSaldo(1L);
		saldoCuentaDestino = service.revisarSaldo(2L);

		assertEquals("1000", saldoCuentaOrigen.toPlainString());
		assertEquals("2000", saldoCuentaDestino.toPlainString());

		int total = service.revisarTotalTransferencias(1L);

		assertEquals(0, total);

		verify(cuentaRepository, times(3)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, never()).save(any(Cuenta.class));

		verify(bancoRepository, times(1)).findById(1L);
		verify(bancoRepository, never()).save(any(Banco.class));

		verify(cuentaRepository, times(5)).findById(anyLong());
		verify(cuentaRepository, never()).findAll();
	}

	@Test
	void contextLoads3() {
		when(cuentaRepository.findById(1L)).thenReturn(crearCuenta001());

		Cuenta cuenta1 = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);

		assertSame(cuenta1, cuenta2);
		assertTrue(cuenta1 == cuenta2);
		assertEquals("Andrés", cuenta1.getPersona());
		assertEquals("Andrés", cuenta2.getPersona());

		verify(cuentaRepository, times(2)).findById(1L);
	}

	@Test
	void testFindAll() {
		//Given
		List<Cuenta> datos = Arrays.asList(crearCuenta001().orElseThrow(),
				crearCuenta002().orElseThrow());
		when(cuentaRepository.findAll()).thenReturn(datos);

		//When
		List<Cuenta> cuentas = service.findAll();

		//Then
		assertFalse(cuentas.isEmpty());
		assertEquals(2, cuentas.size());
		assertTrue(cuentas.contains(crearCuenta002().orElseThrow()));

		verify(cuentaRepository).findAll();
	}

	@Test
	void testSave() {
		//Given
		Cuenta cuentaPepe = new Cuenta(null, "Pepe", new BigDecimal("3000"));
		when(cuentaRepository.save(any())).then(invocation -> {
			Cuenta c = invocation.getArgument(0);
			c.setId(3L);
			return c;
		});

		//When
		Cuenta cuenta = service.save(cuentaPepe);

		//Then
		assertEquals("Pepe", cuenta.getPersona());
		assertEquals(3, cuenta.getId());
		assertEquals("3000", cuenta.getSaldo().toPlainString());

		verify(cuentaRepository).save(any());
	}
}
