package com.pumpprogress.api.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user", indexes = @Index(columnList = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @NotEmpty(message = "Email cannot be empty")
    private String email;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    private String token;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    // By default all user have the role USER
    private Set<Role> roles = new HashSet<>(Collections.singletonList(new Role(2, "USER")));
}
