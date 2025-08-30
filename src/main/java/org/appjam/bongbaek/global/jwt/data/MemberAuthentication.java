package org.appjam.bongbaek.global.jwt.data;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class MemberAuthentication extends UsernamePasswordAuthenticationToken {
    public MemberAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static MemberAuthentication createMemberAuthentication(String memberId) {
        return new MemberAuthentication(memberId, null, Collections.emptyList());  // TODO: 권한 레벨 현재 X. 필요시 추가 가능
    }
}