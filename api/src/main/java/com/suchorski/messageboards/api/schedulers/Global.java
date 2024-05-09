package com.suchorski.messageboards.api.schedulers;

import java.time.Instant;

import javax.naming.NamingException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.suchorski.messageboards.api.components.LdapUtils;
import com.suchorski.messageboards.api.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Component
@Log4j2
public class Global {

    private UserRepository userRepository;
    private LdapUtils ldapUtils;

    @Scheduled(fixedDelay = 10000)
    public void updateUsers() throws NamingException {
        if (ldapUtils.isRunning()) {
            final var user = userRepository.findTop1ByOrderByLastUpdateAsc();
            if (user.isPresent() && user.get().isOneDayOld()) {
                final var ldapUser = ldapUtils.findByCpf(user.get().getCpf());
                if (ldapUser.isPresent()) {
                    final var u = user.get();
                    log.debug("Updating old user with CPF {}", u.getCpf());
                    u.setName(ldapUser.get().getName());
                    u.setNickname(ldapUser.get().getNickname());
                    u.setRank(ldapUser.get().getRank());
                    u.setCompany(ldapUser.get().getCompany());
                    u.setLastUpdate(Instant.now());
                    userRepository.save(u);
                }
            }
        }
    }
}