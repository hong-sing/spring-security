package com.cos.security.config.auth;

import com.cos.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// Security Session => Authentication => UserDetails(여기서는 PrincipalDeatils 가 된다.)

@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;  //콤포지션

    // 해당 User의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 우리 사이트에서 1년동안 회원이 로그인을 안하면, 휴면 계정으로 하기로 함.
        // 현재시간 - 로그인시간 => 1년을 초과하면 return false

        return true;
    }
}
