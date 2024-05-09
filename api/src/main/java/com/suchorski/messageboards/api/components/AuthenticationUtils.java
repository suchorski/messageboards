package com.suchorski.messageboards.api.components;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.suchorski.messageboards.api.models.User;
import com.suchorski.messageboards.api.repositories.UserRepository;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@NoArgsConstructor
@Log4j2
public class AuthenticationUtils {

    @Value("${keycloak.claims.cpf}")
    private String keycloakClaimCpf;
    @Value("${keycloak.claims.name}")
    private String keycloakClaimName;
    @Value("${keycloak.claims.nickname}")
    private String keycloakClaimNickname;
    @Value("${keycloak.claims.rank}")
    private String keycloakClaimRank;
    @Value("${keycloak.claims.company}")
    private String keycloakClaimCompany;

    @Autowired
    private UserRepository userRepository;

    public User findOrCreateUser(Authentication authentication) {
        final var jwt = (Jwt) authentication.getPrincipal();
        final var cpf = jwt.getClaimAsString(keycloakClaimCpf);
        final var name = jwt.getClaimAsString(keycloakClaimName);
        final var nickname = jwt.getClaimAsString(keycloakClaimNickname);
        final var rank = jwt.getClaimAsString(keycloakClaimRank);
        final var company = jwt.getClaimAsString(keycloakClaimCompany);
        final var optionalUser = userRepository.findByCpf(cpf);
        if (optionalUser.isPresent() && optionalUser.get().isOneDayOld()) {
            log.debug("Updating old user with CPF {}", cpf);
            final var user = optionalUser.get();
            user.setName(name);
            user.setNickname(nickname);
            user.setRank(rank);
            user.setCompany(company);
            user.setLastUpdate(Instant.now());
            return userRepository.save(user);
        } else {
            log.debug("Creating new user with CPF {}", cpf);
            final var newUser = new User(cpf, name, nickname, rank, company);
            return userRepository.save(newUser);
        }
    }

}
