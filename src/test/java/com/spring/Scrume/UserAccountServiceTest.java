package com.spring.Scrume;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import com.spring.CustomObject.UserAccountDto;
import com.spring.Model.UserAccount;
import com.spring.Security.UserAccountService;

public class UserAccountServiceTest extends AbstractTest {

	@Autowired
	private UserAccountService userAccountService;

	@Test
	public void userAccountTestSave() throws Exception {

		Object[][] objects = {
				{ "prueba@gmail.com", "Prueba12345", super.entities().get("proBox"), "ABDCDOP123456", "ABDCDOP123456",
						LocalDate.of(2021, 03, 29), null }, // Caso positivo
				{ "prueba", "Prueba12345", super.entities().get("proBox"), "ABDCDOP123456", "ABDCDOP123456",
						LocalDate.of(2021, 03, 29), ResponseStatusException.class }, // Caso negativo: username invalido
				{ "prueba@gmail.com", "prueba12345", super.entities().get("proBox"), "ABDCDOP123456", "ABDCDOP123456",
						LocalDate.of(2021, 03, 29), ResponseStatusException.class } // Caso negativo:
																					// password
				// invalida
		}; // Caso

		Stream.of(objects).forEach(x -> driverTestSave((String) x[0], (String) x[1], (Integer) x[2], (String) x[3],
				(String) x[4], (LocalDate) x[5], (Class<?>) x[6]));
	}

	protected void driverTestSave(String username, String password, Integer box, String order, String payer,
			LocalDate date, Class<?> expected) {

		Class<?> caught = null;
		try {
			UserAccountDto userAccountDto = new UserAccountDto();
			userAccountDto.setUsername(username);
			userAccountDto.setPassword(password);
			userAccountDto.setBox(box);
			userAccountDto.setOrderId(order);
			userAccountDto.setPayerId(payer);
			userAccountDto.setExpiredDate(date);
			this.userAccountService.save(userAccountDto);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}

	@Test
	public void userAccountTestUpdate() throws Exception {
		Object[][] objects = {
				{ super.entities().get("user1"), super.entities().get("account1"), "prueba22@gmail.com", null } }; // Caso
																													// positivo

		Stream.of(objects)
				.forEach(x -> driverTestUpdate((Integer) x[0], (Integer) x[1], (String) x[2], (Class<?>) x[3]));
	}

	protected void driverTestUpdate(Integer userId, Integer userAccountId, String username, Class<?> expected) {
		Class<?> caught = null;
		try {
			UserAccount userAccountDB = this.userAccountService.findOne(userAccountId);
			UserAccountDto userAccountDto = new UserAccountDto();
			userAccountDto.setPassword(userAccountDB.getPassword());
			userAccountDto.setUsername(userAccountDB.getUsername());
			userAccountDto.setUsername(username);
			this.userAccountService.update(userAccountId, userAccountDto);
		} catch (Exception oops) {
			caught = oops.getClass();
		}
		super.checkExceptions(expected, caught);
	}
}
