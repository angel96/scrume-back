package com.spring.Service;

import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.spring.Model.BaseEntity;

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

	public static <T extends BaseEntity> void assertValues(List<Object[]> objects) {

		// Param 0 -> Object to check
		// Param 1 -> HttpStatus status
		// Param 2 -> String message

		if (objects.size() > 1) {
			objects.forEach(object -> {
				T o = (T) object[0];
				HttpStatus status = (HttpStatus) object[1];
				String message = (String) object[2];

				if (o == null) {
					throw new HttpClientErrorException(status, message);
				}
			});

		} else {

			Object[] object = objects.get(0);

			T o = (T) object[0];
			HttpStatus status = (HttpStatus) object[1];
			String message = (String) object[2];

			if (o == null) {
				throw new HttpClientErrorException(status, message);
			}
		}

	}

}
