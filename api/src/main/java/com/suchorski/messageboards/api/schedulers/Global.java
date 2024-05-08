package com.suchorski.messageboards.api.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.suchorski.messageboards.api.components.LdapUtils;
import com.suchorski.messageboards.api.repositories.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class Global {

    private UserRepository userRepository;
    private LdapUtils ldapUtils;

    @Scheduled(cron = "* * 2 * * *")
    public void updateUsers() {
        if (ldapUtils.isRunning()) {
            final var users = userRepository.findAll();
            users.forEach(u -> {
                try {
                    final var ldapUser = ldapUtils.findByCpf(u.getCpf());
                    if (ldapUser.isPresent()) {
                        u.setName(ldapUser.get().getName());
                        u.setNickname(ldapUser.get().getNickname());
                        u.setRank(ldapUser.get().getRank());
                        u.setOm(ldapUser.get().getOm());
                        userRepository.save(u);
                    }
                } catch (Exception e) {
                }
            });
        }
    }

}
