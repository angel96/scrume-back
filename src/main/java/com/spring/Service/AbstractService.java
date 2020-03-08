package com.spring.Service;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AbstractService {

	public String getLanguageSystem() {
		return LocaleContextHolder.getLocale().getLanguage();
	}

	public <T> String nameEntity(T e) {
		String s = e.getClass().getSimpleName();

		s = s.replace(s.charAt(0), Character.toLowerCase(s.charAt(0)));

		return s;
	}

}
