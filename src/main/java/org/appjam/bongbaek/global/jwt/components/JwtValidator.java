package org.appjam.bongbaek.global.jwt.components;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import java.util.Date;
import java.util.Objects;

import org.appjam.bongbaek.global.config.security.util.JwtProperties;
import org.appjam.bongbaek.global.exception.member.SignatureInvalidException;
import org.appjam.bongbaek.global.exception.member.TokenExpiredException;
import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidator {
	private static final String ACCESS_TOKEN_PREFIX = "Bearer ";

	private final JwtProperties jwtProperties;
	private final JwtParser jwtParser;

	/**
	 * 토큰이 Bearer로 시작하는지, null 또는 빈 문자열인지 검증
	 */
	public boolean isValidFormat(final String tokenWithBearer) {
		return tokenWithBearer != null
				&& tokenWithBearer.startsWith(ACCESS_TOKEN_PREFIX)
				&& tokenWithBearer.length() > ACCESS_TOKEN_PREFIX.length();
	}

	public void verifyToken(final String token) {
		Jws<Claims> claims = parseAndVerifySignature(token);
		verifyClaims(claims);
	}

    private Jws<Claims> parseAndVerifySignature(final String token) {
        try {
            return jwtParser.parseClaims(token);
        } catch (SecurityException e) {
            throw new SignatureInvalidException();
        } catch (MalformedJwtException e) {
            throw new TokenInvalidException();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (UnsupportedJwtException e) {
            throw new TokenInvalidException();
        } catch (JwtException e) {
            throw new TokenInvalidException();
        }
    }

	private void verifyClaims(final Jws<Claims> claims) {
		if (!isValidIssuer(claims)) {
			throw new TokenInvalidException();
		}

		if (isExpired(claims)) {
			throw new TokenExpiredException();
		}
	}

	private boolean isValidIssuer(final Jws<Claims> claims) {
		String issuer = claims.getPayload().getIssuer();
		return Objects.equals(issuer, jwtProperties.issuer());
	}

	private boolean isExpired(final Jws<Claims> claims) {
		Date expiration = claims.getPayload().getExpiration();

		if(expiration == null) {
			return true;
		}

		return expiration.before(new Date());
	}
}
