package com.spring.CustomObject;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEditDto {

	private int id;
	private int box;
	private LocalDate expiredDate;
	private String orderId;
	private String payerId;

}
