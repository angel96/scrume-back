package com.spring.Utiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jboss.logging.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Utiles {

	protected static final Logger log = Logger.getLogger(Utiles.class);

	/**
	 * 
	 * @param s
	 * @return Devuelve la contraseña encriptada en el formato BCrypt
	 */

	public static String encryptedPassword(String s) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(s);
	}

	public static Boolean matchesPassword(String password, String encrypted) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, encrypted);
	}

	/**
	 * 
	 * @param objects
	 * @return Comprueba si todos los objetos pasados por parametros no son nulos
	 */
	public static boolean checkAllNotNull(Object... objects) {
		return Stream.of(objects).allMatch(Objects::nonNull);
	}

	public static String limpiaCadena(final String s) {
		return s.replaceAll("[^a-zA-Z0-9$]", "");
	}

	public static SortedMap<String, Integer> leeFichero(String nombreFichero) {
		SortedMap<String, Integer> result = new TreeMap<>();

		try (Stream<String> input = Files.lines(Paths.get(nombreFichero))) {
			input.forEach(x -> {
				String entityName = "";
				Integer id = 0;

				try {
					entityName = x.split("=")[0].equals("null") || x.split("=")[0] == null ? "" : x.split("=")[0];
					id = x.split("=")[1].equals("null") || x.split("=")[1] == null ? 0
							: Integer.valueOf(x.split("=")[1]);
				} catch (NumberFormatException e) {
					entityName = x.split("=")[0].equals("null") || x.split("=")[0] == null ? "" : x.split("=")[0];
				} 
				result.put(entityName, id);
			});

		} catch (IOException e) {
			log.error("Error en la lectura del fichero:" + e);
		}

		return result;
	}

	public static void escribeFichero(SortedMap<String, Integer> map, String nombreFichero) {
		String content = " ";

		try {
			if (map.size() > 0) {
				content = map.keySet().stream().map(x -> x + "=" + (map.get(x) == null ? 0 : map.get(x)) + "\n")
						.collect(Collectors.joining());
			}

			Files.write(Paths.get(nombreFichero), content.getBytes());
		} catch (IOException e) {
			log.error("No se ha podido generar el fichero: " + e);
		}

	}

	public static void main(String[] args) throws IOException {

		/**
		 * Metodo main para encriptar contraseñas por consola
		 */

		System.console().writer().println("Introduzca la contraseña a encriptar: \n");
		BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));

		String str = "";

		do {
			str = obj.readLine();
			System.console().writer().println("La contraseña a encriptar es: " + str + "\n");
			System.console().writer().println("La contraseña codificada es: " + Utiles.encryptedPassword(str) + "\n");
		} while (!str.equals("stop"));

	}
}
