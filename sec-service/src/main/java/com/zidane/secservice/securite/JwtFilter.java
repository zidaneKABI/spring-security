package com.zidane.secservice.securite;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zidane.secservice.sec.entities.AppUser;
import com.zidane.secservice.service.AccountServiceImplementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@Getter
@RequiredArgsConstructor
public class JwtFilter  extends  OncePerRequestFilter {

  private final AccountServiceImplementation accountServiceImplementation ;
  private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
     {
      
      
      System.out.println("Filter is called XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      
      String token ="";
      boolean istokenvalid = true;
      String username ="";
      String autorisation = request.getHeader("Authorization");

      
      
      if(autorisation !=null  )
      {

        if(autorisation.startsWith("Bearer"))
        {
          System.out.println("Token is valid XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
          
          token = autorisation.substring(7);
          System.out.println("Token is "+ token);
          
        Claims claims = Jwts.parserBuilder()
        .setSigningKey(jwtService.getKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

        System.out.println("Claims = " + claims);


          
          
          
          istokenvalid = jwtService.validateToken(token);

          username = jwtService.readusername(token);
           
          if(istokenvalid && username !=null && SecurityContextHolder.getContext().getAuthentication() == null)
          {
            System.out.println("Token is valid XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
          
            AppUser appUser = accountServiceImplementation.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(appUser, null, appUser.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          
          }

          System.out.println("Filter is called XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
       
       
       
       
       
       
        }
  
        
        
      }


            filterChain.doFilter(request, response);


    }

}
