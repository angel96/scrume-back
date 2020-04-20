package com.spring.JWT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spring.CustomObject.UserLoginDto;
import com.spring.Model.Payment;
import com.spring.Model.User;
import com.spring.Model.UserAccount;
import com.spring.Service.PaymentService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtToken {

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * (long)60;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private JwtUserAccountService service;

	@Value("${jwt.secret}")
	private String secret;

	public UserLoginDto getUserLoginDtoFromToken(String token) {
		LinkedHashMap<?, ?> linked = getClaimFromToken(token, x -> (LinkedHashMap<?, ?>) x.get("userLoginDto"));
		return new UserLoginDto((Integer) linked.get("idUser"), (String) linked.get("username"),
				(String) linked.get("nameBox"),
				LocalDate.parse((String) linked.get("endingBoxDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);

	}

	public Date getExpirationDateFromToken(String token) {

		return getClaimFromToken(token, Claims::getExpiration);

	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);

	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return getExpirationDateFromToken(token).before(new Date());
	}

	public String generateToken(Map<String, Object> objects, UserAccount userDetails) {
		return doGenerateToken(objects, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))

				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))

				.signWith(SignatureAlgorithm.HS512, secret).compact();

	}

	public boolean validateToken(String token, UserAccount userDetails) {

		String username = getUsernameFromToken(token);

		UserLoginDto dto = getUserLoginDtoFromToken(token);

		boolean checkUserAndExpiredDate = (username.equals(userDetails.getUsername()) && 
				!isTokenExpired(token));

		User user = this.service.findUserByUsername(username);
		Payment payment = this.paymentService.findByUserAccount(user.getUserAccount());

		boolean checkDto = dto.getNameBox().equals(payment.getBox().getName())
				&& dto.getEndingBoxDate().equals(payment.getExpiredDate()) && user.getId().equals(dto.getIdUser())
				&& username.equals(dto.getUsername());

		return checkUserAndExpiredDate && checkDto;

	}
}
