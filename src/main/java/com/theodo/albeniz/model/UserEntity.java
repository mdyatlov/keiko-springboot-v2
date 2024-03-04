package com.theodo.albeniz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="APP_USER")
@Getter
@Setter
@Builder
public class UserEntity implements UserDetails {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;


    @OneToMany(
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinTable(
            name = "USER_SELECTION",
            joinColumns = @JoinColumn(
                    name = "USER_ID",
                    referencedColumnName = "ID",
                    foreignKey = @ForeignKey(name = "FK_SELECTION_USER_ID")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "TUNE_ID",
                    referencedColumnName = "ID",
                    foreignKey = @ForeignKey(name = "FK_SELECTION_TUNE_ID")
            )
    )
    @JsonIgnore
    private Set<TuneEntity> selection;

    public void addSelection(TuneEntity tuneEntity){
        if(selection == null){
            selection = new HashSet<>();
        }
        selection.add(tuneEntity);
    }
    public void removeSelection(TuneEntity tuneEntity){
        if(selection == null){
            return;
        }
        selection.remove(tuneEntity);
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

}
