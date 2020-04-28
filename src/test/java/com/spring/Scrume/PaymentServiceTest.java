package com.spring.Scrume;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.PaymentEditDto;
import com.spring.Model.Payment;
import com.spring.Security.UserAccountService;
import com.spring.Service.PaymentService;

public class PaymentServiceTest extends AbstractTest {

	@Autowired
	private PaymentService service;

	@Test
	public void testFindAllPaymentsByUserLogged() {
		Object[][] objects = { { "testuser1@gmail.com", null }, { null, AssertionError.class } };
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
		Object[][] objects = { 
				{ "testuser1@gmail.com", LocalDate.of(9999, 04, 24), null },
				{ null, LocalDate.of(2020, 04, 24), AssertionError.class },
				{ "testuser1@gmail.com", LocalDate.of(2020, 01, 24), ResponseStatusException.class } 
				};
		Stream.of(objects).forEach(x -> driverPago((String) x[0], (LocalDate) x[1], (Class<?>) x[2]));
	}

	protected void driverPago(String user, LocalDate date, Class<?> expected) {
		Class<?> caught = null;

		try {
			super.authenticateOrUnauthenticate(user);

			Payment paymentUser = this.service.findByUserAccount(UserAccountService.getPrincipal());
			paymentUser.setExpiredDate(LocalDate.of(2020, 03, 12));
			PaymentEditDto payment = new PaymentEditDto(0, super.entities().get("proBox"), date, "ABCD1234",
					"ASFDFD59842");
			this.service.save(payment);
			service.flush();
			super.authenticateOrUnauthenticate(null);
		} catch (Throwable oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
