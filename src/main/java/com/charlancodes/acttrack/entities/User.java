package com.charlancodes.acttrack.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity(name = "Users")
@Table(name = "users")
public class User {
    @SequenceGenerator(name = "user_id", sequenceName = "user_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id")
    @Id
    private Long id;
    private String name;
    @Column(name = "email", unique = true)
    private String email;
    private String password;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
    @JsonIgnore
    private List<Task> task = new ArrayList<>();
}
