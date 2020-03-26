package com.spring.Repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.Model.Payment;

@Repository
public interface PaymentRepository extends AbstractRepository<Payment> {
	@Query("select p from Payment p where p.userAccount.id = ?1 order by p.paymentDate DESC")
	Collection<Payment> findPaymentsByUser(int userAccount);
}
