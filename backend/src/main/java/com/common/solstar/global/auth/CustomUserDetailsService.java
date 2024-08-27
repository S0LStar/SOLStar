package com.common.solstar.global.auth;

import com.common.solstar.domain.agency.model.repository.AgencyRepository;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
    }
    
    // email과 role로 유저정보 추출
    public UserDetails loadUserByEmailAndRole(String email, String role) throws UsernameNotFoundException {
        if("USER".equals(role)) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
        }
        else if("AGENCY".equals(role)) {
            return agencyRepository.findByEmail(email)
                    .orElseThrow(() -> new ExceptionResponse(CustomException.NOT_FOUND_USER_EXCEPTION));
        }
        else throw new UsernameNotFoundException("Invalid role: " + role);
    }
}
