package com.suchorski.messageboards.api.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ldap")
public class LdapProperties {

    private Boolean enabled;
    private String providerUrl;
    private String securityAuthentication;
    private String securityPrincipal;
    private String securityCredentials;
    private LdapParameters parameters;
    private LdapFilters filters;

    @Getter
    @Setter
    public static class LdapParameters {
        private String name;
        private String nickname;
        private String rank;
        private String company;
    }

    @Getter
    @Setter
    public static class LdapFilters {
        private String baseOu;
        private String userFilter;
    }

}
