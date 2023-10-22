package com.hospitalservice.mkobo.entities;

import com.hospitalservice.mkobo.enums.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Data
@Table(name = "staff")
@AllArgsConstructor
@NoArgsConstructor
public class Staff implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    private String firstName;
    private String lastName;
    private String uuid;
    @Enumerated(EnumType.STRING)
    private Roles role;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registration_date;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
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
        return true;
    }
}
