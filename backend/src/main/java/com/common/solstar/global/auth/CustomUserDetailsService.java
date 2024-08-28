package com.common.solstar.global.auth;

import com.common.solstar.domain.agency.entity.Agency;
import com.common.solstar.domain.agency.model.repository.AgencyRepository;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.domain.user.model.repository.UserRepository;
import com.common.solstar.global.exception.CustomException;
import com.common.solstar.global.exception.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }

        Optional<Agency> agency = agencyRepository.findByEmail(email);
        if (agency.isPresent()) {
            return agency.get();
        }
        throw new UsernameNotFoundException("User not found with email: " + email);

    }
    
    // email과 role로 유저정보 추출
    public UserDetails loadUserByEmailAndRole(String email, String role) throws UsernameNotFoundException {
        try {
            if ("USER".equals(role)) {
                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
            } else if ("AGENCY".equals(role)) {
                return agencyRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Agency not found with email: " + email));
            } else {
                throw new UsernameNotFoundException("Invalid role: " + role);
            }
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException("Failed to load user", e);
        }
    }
}
