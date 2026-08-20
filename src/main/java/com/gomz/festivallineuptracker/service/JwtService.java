    package com.gomz.festivallineuptracker.service;

    import com.gomz.festivallineuptracker.model.User;
    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.io.Decoders;
    import io.jsonwebtoken.security.Keys;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.stereotype.Service;

    import javax.crypto.SecretKey;
    import java.util.Date;

    @Service
    public class JwtService {

        private final String jwtSecret;
        private final long jwtExpirationMs;

        public JwtService(@Value("${jwt.secret}") String jwtSecret, @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
            this.jwtSecret = jwtSecret;
            this.jwtExpirationMs = jwtExpirationMs;
        }



        public String generateToken(User user) {

            Date now = new Date();

            return Jwts.builder().subject(user.getUsername()).claim("userId", user.getId()).claim("role", user.getRole().name()).issuedAt(now)
                    .expiration(new Date(now.getTime() + jwtExpirationMs)).signWith(getSigningKey()).compact();
        }



        public String extractUsername(String token) {
            return extractAllClaims(token).getSubject();
        }




        public boolean isTokenValid(String token, UserDetails userDetails) {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        }



        private boolean isTokenExpired(String token) {
            return extractAllClaims(token).getExpiration().before(new Date());
        }



        private Claims extractAllClaims(String token) {
            return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
        }



        private SecretKey getSigningKey() {

            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        }
    }
