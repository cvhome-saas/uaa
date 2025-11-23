package com.asrevo.cvhome.uaa.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
public class Permission {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    public Permission(String name) { this.name = name; }

    @PrePersist
    public void prePersist() {
        if (id == null) id = UUID.randomUUID();
    }
}
