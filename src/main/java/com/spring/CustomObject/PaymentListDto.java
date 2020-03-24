package com.spring.CustomObject;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentListDto {

	private LocalDateTime paymentDate;
	private String box;
	private Double price;

}
