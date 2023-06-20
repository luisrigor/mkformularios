package com.gsc.mkformularios.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * This factory method can be safely used by any code that wishes to create a unauthenticated <code>JwtAuthenticationToken</code>.
     *
     * @param principal
     * @return UsernamePasswordAuthenticationToken with false isAuthenticated() result
     * @since 5.7
     */
    public static JwtAuthenticationToken unauthenticated(Object principal) {
        return new JwtAuthenticationToken(principal);
    }

    /**
     * This factory method can be safely used by any code that wishes to create a authenticated <code>JwtAuthenticationToken</code>.
     *
     * @param principal
     * @return UsernamePasswordAuthenticationToken with true isAuthenticated() result
     * @since 5.7
     */
    public static JwtAuthenticationToken authenticated(Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, authorities);
    }

    private final Object principal;

    /**
     * This constructor can be safely used by any code that wishes to create a <code>JwtAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public JwtAuthenticationToken(Object principal) {
        super(null);
        this.principal = principal;
        super.setAuthenticated(false); // must use super, as we override
    }

    /**
     * This constructor should only be used by <code>AuthenticationManager</code> or <code>AuthenticationProvider</code> implementations that are
     * satisfied with producing a trusted (i.e. {@link #isAuthenticated()} = <code>true</code>) authentication token.
     *
     * @param principal
     * @param authorities
     */
    public JwtAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
