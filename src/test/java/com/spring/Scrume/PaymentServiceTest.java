package com.spring.Scrume;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.CustomObject.PaymentEditDto;
import com.spring.Service.PaymentService;

public class PaymentServiceTest extends AbstractTest {

	@Autowired
	private PaymentService service;

	@Test
	public void testFindAllPaymentsByUserLogged() {
		Object[][] objects = { { "testuser@gmail.com", null }, { null, AssertionError.class } };
		Stream.of(objects).forEach(x -> driverFindAllPaymentsByUserLogged((String) x[0], (Class<?>) x[1]));
	}

	protected void driverFindAllPaymentsByUserLogged(String user, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			this.service.findPaymentsByUserLogged();
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void testPago() {
		Object[][] objects = { { "testuser@gmail.com", null }, { null, AssertionError.class } };
		Stream.of(objects).forEach(x -> driverPago((String) x[0], (Class<?>) x[1]));
	}

	protected void driverPago(String user, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);
			PaymentEditDto payment = new PaymentEditDto(0, super.entities().get("proBox"));
			this.service.save(payment);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
