package org.itbl.controller;

import org.itbl.entity.JwtRequest;
import org.itbl.entity.JwtResponse;
import org.itbl.service.CustomUserDetail;
import org.itbl.service.CustomUserDetailsService;
import org.itbl.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MainController {
	
	@Autowired
	private JwtUtility jwtUtility;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService customUserDetailService;
	
	@PostMapping("/authenticate")
	public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
		
		System.out.println(jwtRequest);
		
		try {
			
			System.out.println("this is try block");
			
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getUsername(), jwtRequest.getPassword()
						)
					);
		}catch (BadCredentialsException e) {
			
			throw new Exception("Invalid Credintial", e);
		}
		
		CustomUserDetail userDetail = customUserDetailService.loadUserByUsername(jwtRequest.getUsername());
		
		System.out.println(userDetail.getUser());
		
		final String token = jwtUtility.generateToken1(userDetail);
		
		System.out.println(token);
		
		return new JwtResponse(token);
	}

}
