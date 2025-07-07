package org.appjam.bongbaek.global.jwt.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

public class MemberAuthentication extends UsernamePasswordAuthenticationToken {
    public MemberAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static MemberAuthentication createMemberAuthentication(UUID memberId) {
        return new MemberAuthentication(memberId, null, null); // TODO: 권한 레벨 현재 X. 필요시 추가 가능
    }
}
