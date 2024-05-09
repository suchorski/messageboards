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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suchorski.messageboards.api.models.User;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;

@Getter
@Component
public class LdapUtils {

    @Value("${ldap.enabled}")
    private boolean ldapEnabled;
    @Value("${ldap.provider-url}")
    private String providerUrl;
    @Value("${ldap.security-authentication}")
    private String securityAuthentication;
    @Value("${ldap.security-principal}")
    private String securityPrincipal;
    @Value("${ldap.security-credentials}")
    private String securityCredentials;
    @Value("${ldap.parameters.name}")
    private String ldapParameterName;
    @Value("${ldap.parameters.nickname}")
    private String ldapParameterNickname;
    @Value("${ldap.parameters.rank}")
    private String ldapParameterRank;
    @Value("${ldap.parameters.company}")
    private String ldapParameterCompany;
    @Value("${ldap.filters.base-ou}")
    private String ldapFilterBaseOu;
    @Value("${ldap.filters.user-filter}")
    private String ldapFilterUserFilter;

    private boolean running = false;
    private LdapContext ldapContext;

    @PostConstruct
    public void construct() {
        try {
            if (ldapEnabled) {
                final var env = new Hashtable<String, String>();
                env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                env.put(Context.PROVIDER_URL, providerUrl);
                env.put(Context.SECURITY_AUTHENTICATION, securityAuthentication);
                env.put(Context.SECURITY_PRINCIPAL, securityPrincipal);
                env.put(Context.SECURITY_CREDENTIALS, securityCredentials);
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
                    new String[] { ldapParameterName, ldapParameterNickname, ldapParameterCompany, ldapParameterRank });
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> naming = ldapContext.search(ldapFilterBaseOu,
                    String.format(ldapFilterUserFilter, cpf),
                    searchControls);
            if (naming.hasMoreElements()) {
                SearchResult result = (SearchResult) naming.next();
                Attributes attrs = result.getAttributes();
                return Optional.of(
                        User.builder()
                                .cpf(cpf)
                                .name(attrs.get(ldapParameterName).get().toString())
                                .nickname(attrs.get(ldapParameterNickname).get().toString())
                                .company(attrs.get(ldapParameterCompany).get().toString())
                                .rank(attrs.get(ldapParameterRank).get().toString())
                                .build());
            }
        }
        return Optional.empty();
    }

}
