package com.suchorski.messageboards.api.components;

import java.util.Hashtable;
import java.util.Optional;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.springframework.stereotype.Component;

import com.suchorski.messageboards.api.models.User;
import com.suchorski.messageboards.api.properties.LdapProperties;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Component
public class LdapUtils {

    @NonNull
    private final LdapProperties ldapProperties;

    private boolean running = false;
    private LdapContext ldapContext;

    @PostConstruct
    public void construct() {
        try {
            if (ldapProperties.getEnabled()) {
                final var env = new Hashtable<String, String>();
                env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(Context.PROVIDER_URL, ldapProperties.getProviderUrl());
                env.put(Context.SECURITY_AUTHENTICATION, ldapProperties.getSecurityAuthentication());
                env.put(Context.SECURITY_PRINCIPAL, ldapProperties.getSecurityPrincipal());
                env.put(Context.SECURITY_CREDENTIALS, ldapProperties.getSecurityCredentials());
                ldapContext = new InitialLdapContext(env, null);
                running = true;
            }
        } catch (NamingException e) {
            running = false;
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            running = false;
            if (ldapContext != null) {
                ldapContext.close();
                ldapContext = null;
            }
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findByCpf(String cpf) throws NamingException {
        if (running) {
            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(
                    new String[] {
                            ldapProperties.getParameters().getName(),
                            ldapProperties.getParameters().getNickname(),
                            ldapProperties.getParameters().getRank(),
                            ldapProperties.getParameters().getCompany() });
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> naming = ldapContext.search(ldapProperties.getFilters().getBaseOu(),
                    String.format(ldapProperties.getFilters().getUserFilter(), cpf),
                    searchControls);
            if (naming.hasMoreElements()) {
                SearchResult result = (SearchResult) naming.next();
                Attributes attrs = result.getAttributes();
                return Optional.of(
                        User.builder()
                                .cpf(cpf)
                                .name(attrs.get(ldapProperties.getParameters().getName()).get().toString())
                                .nickname(attrs.get(ldapProperties.getParameters().getNickname()).get().toString())
                                .company(attrs.get(ldapProperties.getParameters().getCompany()).get().toString())
                                .rank(attrs.get(ldapProperties.getParameters().getRank()).get().toString())
                                .build());
            }
        }
        return Optional.empty();
    }

}
