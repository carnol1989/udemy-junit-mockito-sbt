package com.cnolem.test.springboot.app;

import com.cnolem.test.springboot.app.repositories.BancoRepository;
import com.cnolem.test.springboot.app.repositories.CuentaRepository;
import com.cnolem.test.springboot.app.services.CuentaService;
import com.cnolem.test.springboot.app.services.CuentaServiceImpl;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {

	CuentaRepository cuentaRepository;
	BancoRepository bancoRepository;

	CuentaService service;

	@BeforeEach
	void setUp() {
		cuentaRepository = mock(CuentaRepository.class);
		bancoRepository = mock(BancoRepository.class);
		service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
	}

	@Test
	void contextLoads() {
		when(cuentaRepository.findById(1L)).thenReturn(Datos.CUENTA_001);
		when(cuentaRepository.findById(2L)).thenReturn(Datos.CUENTA_002);
		when(bancoRepository.findById(1L)).thenReturn(Datos.BANCO);

		BigDecimal saldoCuentaOrigen = service.revisarSaldo(1L);
		BigDecimal saldoCuentaDestino = service.revisarSaldo(2L);

		assertEquals("1000", saldoCuentaOrigen.toPlainString());
		assertEquals("2000", saldoCuentaDestino.toPlainString());

		service.transferir(1L, 2L, new BigDecimal("100"), 1L);

		saldoCuentaOrigen = service.revisarSaldo(1L);
		saldoCuentaDestino = service.revisarSaldo(2L);

		assertEquals("900", saldoCuentaOrigen.toPlainString());
		assertEquals("2100", saldoCuentaDestino.toPlainString());
	}

}
