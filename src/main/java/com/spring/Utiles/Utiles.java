package com.spring.Utiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Utiles {

	/**
	 * 
	 * @param s
	 * @return Devuelve la contraseña encriptada en el formato BCrypt
	 */

	public static String encryptedPassword(String s) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(s);
	}

	/**
	 * 
	 * @param objects
	 * @return Comprueba si todos los objetos pasados por parametros no son nulos
	 */
	public static boolean checkAllNotNull(Object... objects) {
		return Stream.of(objects).allMatch(x -> x != null);
	}

	public static String limpiaCadena(final String s) {
		return s.replaceAll("[^a-zA-Z0-9$]", "");
	}

	public static void main(String[] args) throws IOException {

		/**
		 * Metodo main para encriptar contraseñas por consola
		 */

		System.out.println("Introduzca la contraseña a encriptar: \n");
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

		String str = "";

		do {
			str = obj.readLine();
			System.out.println("La contraseña a encriptar es: " + str + "\n");
			System.out.println("La contraseña codificada es: " + Utiles.encryptedPassword(str) + "\n");
		} while (!str.equals("stop"));
	}
}
