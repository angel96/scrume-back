package com.spring.CustomObject;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentListDto {

	private LocalDate expiredDate;
	private LocalDate paymentDate;
	private String box;
	private Double price;

}
