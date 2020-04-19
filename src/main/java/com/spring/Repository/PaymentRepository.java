package com.spring.Repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Payment;
import com.spring.Model.UserAccount;

@Repository
public interface PaymentRepository extends AbstractRepository<Payment> {
	@Query("select p from Payment p where p.userAccount.id = ?1 order by p.paymentDate DESC")
	Collection<Payment> findPaymentsByUser(int userAccount);

	@Query("select p from Payment p where p.userAccount = ?1 order by p.paymentDate DESC")
	List<Payment> findByUserAccount(UserAccount userAccount);
}
