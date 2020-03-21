package com.spring.CustomValidatorsClass;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.spring.CustomValidatorsInterface.CustomUrlsValidator;

public class CustomUrlsValidatorClass implements ConstraintValidator<CustomUrlsValidator, Collection<String>> {

	@Override
	public boolean isValid(Collection<String> urls, ConstraintValidatorContext context) {
		return !urls.isEmpty() ? urls.stream().allMatch(x -> x.startsWith("http://") || x.startsWith("https://"))
				: urls.isEmpty();
	}

}
