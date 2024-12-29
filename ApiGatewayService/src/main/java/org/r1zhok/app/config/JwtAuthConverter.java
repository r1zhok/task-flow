package org.r1zhok.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class JwtAuthConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        return Mono.fromCallable(() -> {
            Collection<GrantedAuthority> authorities = Stream.concat(
                    jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                    extractResourceRoles(jwt).stream()
            ).collect(Collectors.toSet());

            return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
        });
    }

    private String getPrincipalClaimName(Jwt jwt) {
        return jwt.getClaim(JwtClaimNames.SUB);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        Collection<String> allRoles = new ArrayList<>();

        if (resourceAccess != null && resourceAccess.get("account") instanceof Map<?, ?> account) {
            if (account.containsKey("roles") && account.get("roles") instanceof Collection<?> roles) {
                allRoles.addAll((Collection<String>) roles);
            }
        }

        if (realmAccess != null && realmAccess.containsKey("roles") && realmAccess.get("roles") instanceof Collection<?> roles) {
            allRoles.addAll((Collection<String>) roles);
        }

        if (allRoles.isEmpty()) {
            return Set.of();
        }

        return allRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toSet());
    }
}