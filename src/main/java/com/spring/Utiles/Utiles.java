package com.spring.Utiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
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

	public static Map<String, Integer> leeFichero(String nombreFichero) {
		Map<String, Integer> result = null;

		try {
			result = Files.lines(Paths.get(nombreFichero)).collect(
					Collectors.toMap(x -> String.valueOf(x.split("=")[0]), x -> Integer.valueOf(x.split("=")[1])));
		} catch (IOException e) {
			System.out.println("Error en la lectura del fichero:" + e);
		}

		return result;
	}

	public static void escribeFichero(Map<String, Integer> map, String nombreFichero) {
		try {

			String content = "";

			if (map.size() > 0) {
				content = map.keySet().stream().map(x -> x + "=" + String.valueOf(map.get(x)) + "\n")
						.collect(Collectors.joining());
			}

			Files.write(Paths.get(nombreFichero), content.getBytes());
		} catch (IOException e) {
			System.out.println("No se ha podido generar el fichero: " + e);
		}
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

//		Object[] objects = { new Sprint(LocalDateTime.now(), LocalDateTime.now()), HttpStatus.ACCEPTED,
//				"Probando el mensaje uno" };
//		Object[] objects2 = { null, HttpStatus.BAD_GATEWAY, "Probando el mensaje 2" };
//
//		Utiles.assertValues(Arrays.asList(objects, objects2));
	}
}
