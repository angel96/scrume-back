package com.spring.CustomValidatorsClass;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.spring.CustomValidatorsInterface.CustomUrlsValidator;

public class CustomUrlsValidatorClass implements ConstraintValidator<CustomUrlsValidator, Collection<String>> {

	@Override
	public boolean isValid(Collection<String> urls, ConstraintValidatorContext context) {
		return urls.size() > 0 ? urls.stream().allMatch(x -> x.startsWith("http://") || x.startsWith("https://"))
				: true;
	}

}
