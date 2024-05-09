package com.suchorski.messageboards.api.components;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.suchorski.messageboards.api.models.User;
import com.suchorski.messageboards.api.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class AuthenticationUtils {

    private final UserRepository userRepository;

    public User findOrCreateUser(Authentication authentication) {
        final var jwt = (Jwt) authentication.getPrincipal();
        final var cpf = jwt.getClaimAsString("cpf");
        final var name = jwt.getClaimAsString("full_name");
        final var nickname = jwt.getClaimAsString("nickname");
        final var rank = jwt.getClaimAsString("rank");
        final var company = jwt.getClaimAsString("company_override");
        final var optionalUser = userRepository.findByCpf(cpf);
        if (optionalUser.isPresent()) {
            final var user = optionalUser.get();
            user.setName(name);
            user.setNickname(nickname);
            user.setRank(rank);
            user.setOm(company);
            return userRepository.save(user);
        } else {
            log.debug("Creating new user with CPF {}", cpf);
            final var newUser = new User(cpf, name, nickname, rank, company);
            return userRepository.save(newUser);
        }
    }

}
