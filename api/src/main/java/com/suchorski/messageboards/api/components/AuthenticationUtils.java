package com.suchorski.messageboards.api.components;

import java.time.Instant;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.suchorski.messageboards.api.models.User;
import com.suchorski.messageboards.api.properties.KeycloakProperties;
import com.suchorski.messageboards.api.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class AuthenticationUtils {

    private final UserRepository userRepository;
    private final KeycloakProperties keycloakProperties;

    public User findOrCreateUser(Authentication authentication) {
        final var jwt = (Jwt) authentication.getPrincipal();
        final var cpf = jwt.getClaimAsString(keycloakProperties.getCpf());
        final var name = jwt.getClaimAsString(keycloakProperties.getName());
        final var nickname = jwt.getClaimAsString(keycloakProperties.getNickname());
        final var rank = jwt.getClaimAsString(keycloakProperties.getRank());
        final var company = jwt.getClaimAsString(keycloakProperties.getCompany());
        final var optionalUser = userRepository.findByCpf(cpf);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().isOneDayOld()) {
                log.debug("Updating old user with CPF {}", cpf);
                final var user = optionalUser.get();
                user.setName(name);
                user.setNickname(nickname);
                user.setRank(rank);
                user.setCompany(company);
                user.setLastUpdate(Instant.now());
                return userRepository.save(user);
            }
            return optionalUser.get();
        } else {
            log.debug("Creating new user with CPF {}", cpf);
            final var newUser = new User(cpf, name, nickname, rank, company);
            return userRepository.save(newUser);
        }
    }

}
