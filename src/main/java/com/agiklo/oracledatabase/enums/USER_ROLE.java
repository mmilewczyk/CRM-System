package com.agiklo.oracledatabase.enums;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.agiklo.oracledatabase.enums.APPLICATION_USER_PERMISSION.*;

@Getter
public enum USER_ROLE {
    EMPLOYEE(Sets.newHashSet(
            CUSTOMER_READ)
    ),
    MANAGER(Sets.newHashSet(
            CUSTOMER_READ,
            CUSTOMER_WRITE)
    ),
    ADMIN(Sets.newHashSet(
            CUSTOMER_READ,
            CUSTOMER_WRITE)
    );

    private final Set<APPLICATION_USER_PERMISSION> permissions;

    USER_ROLE(Set<APPLICATION_USER_PERMISSION> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
