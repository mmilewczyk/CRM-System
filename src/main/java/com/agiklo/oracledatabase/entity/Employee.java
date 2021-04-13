package com.agiklo.oracledatabase.entity;

import com.agiklo.oracledatabase.enums.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Table(name = "SYS_USER")
public class Employee implements UserDetails {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1)
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence")
    @Column(name = "USER_ID")
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "USER_ROLE")
    private USER_ROLE userRole;
    private String pesel;
    private String sex;
    private LocalDate birthdate;
    private Double salary;
    @Column(name = "IS_LOCKED")
    private Boolean isLocked = false;
    @Column(name = "IS_ENABLED")
    private Boolean isEnabled = false;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "DEPARTMENT_CODE")
    private Departments department;

    public Employee(String firstName,
                    String lastName,
                    String email,
                    String password,
                    USER_ROLE userRole,
                    String pesel,
                    String sex,
                    LocalDate birthdate,
                    Double salary,
                    Departments department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.pesel = pesel;
        this.sex = sex;
        this.birthdate = birthdate;
        this.salary = salary;
        this.department = department;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    public boolean isAdmin(){
        return this.getUserRole().equals(USER_ROLE.ADMIN);
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
