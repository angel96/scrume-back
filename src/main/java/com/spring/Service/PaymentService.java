package com.spring.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.CustomObject.PaymentEditDto;
import com.spring.CustomObject.PaymentListDto;
import com.spring.Model.Payment;
import com.spring.Model.User;
import com.spring.Repository.PaymentRepository;

@Service
@Transactional
public class PaymentService extends AbstractService {

	@Autowired
	private UserService serviceUser;

	@Autowired
	private BoxService serviceBox;

	@Autowired
	private PaymentRepository repository;

	public Collection<PaymentListDto> findPaymentsByUserLogged() {
		User user = this.serviceUser.getUserByPrincipal();
		return repository.findPaymentsByUser(user.getUserAccount().getId()).stream()
				.map(x -> new PaymentListDto(x.getExpiredDate(), x.getPaymentDate(), x.getBox().getName(),
						x.getBox().getPrice()))
				.collect(Collectors.toList());
	}

	public PaymentEditDto save(PaymentEditDto payment) {

		Payment saveTo = null;

		if (payment.getId() == 0) {
			saveTo = new Payment(LocalDate.now(), this.serviceBox.getOne(payment.getBox()),
					this.serviceUser.getUserByPrincipal().getUserAccount(), payment.getExpiredDate());
			saveTo = repository.saveAndFlush(saveTo);
			payment.setId(saveTo.getId());
		}

		return payment;
	}

	public void flush() {
		repository.flush();
	}

}
