package com.zidane.secservice.securite;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zidane.secservice.sec.entities.AppUser;
import com.zidane.secservice.service.AccountServiceImplementation;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final String KEYENCRYPT ="1936dac99db0964fce0d13e47990ededc5437db23337c66e7b515b1bc4b6faa2";

    private final AccountServiceImplementation accountService; 

    public Map<String,String> generate(String username)
    {

        AppUser appUser = accountService.loadUserByUsername(username);
       
        return this.generateJWT(appUser);
    
        
    }

    private Map<String, String> generateJWT(AppUser appUser) {
       
      final Map<String,Object> claims=
         Map.of("nom",appUser.getName(),"email",appUser.getEmail());

      final long currenttime= System.currentTimeMillis();
      final long expirationtime = currenttime + 30 * 60 * 1000;
        
      String Brearer =  Jwts.builder().setIssuedAt(new Date(currenttime))
          .setExpiration(new Date(expirationtime))
          .setSubject(appUser.getEmail())
          .addClaims(claims)
          .signWith(getKey(), SignatureAlgorithm.HS256).compact();
         
      return Map.of("Bearer", Brearer);

    }

    protected Key getKey() {

        final byte[] decode = Decoders.BASE64.decode(KEYENCRYPT);

        return Keys.hmacShaKeyFor(decode);

    }

    public String readusername(String token) {
   
    
    return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
   
   
    }

    public boolean validateToken(String token) {
      
      Date expirationDate = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getExpiration();
      
      System.out.println("Expiration date is " + expirationDate);
      
      return expirationDate.after(new Date());
   
    }
    

}
