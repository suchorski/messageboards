package com.suchorski.messageboards.api.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_cpf", columnNames = "cpf"),
}, indexes = {
        @Index(name = "idx_user_cpf", columnList = "cpf"),
        @Index(name = "idx_user_name", columnList = "name"),
        @Index(name = "idx_user_om", columnList = "om"),
})
public class User implements Serializable {

    private static final long serialVersionUID = -1_000_000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    @NotBlank
    @JsonIgnore
    private String cpf;

    @NonNull
    @Column(nullable = false)
    @NotBlank
    private String name;

    @NonNull
    @Column(nullable = false)
    @NotBlank
    private String nickname;

    @NonNull
    @Column(nullable = false)
    @NotBlank
    private String rank;

    @NonNull
    @Column(nullable = false)
    @NotBlank
    private String om;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Allocation> allocations = new HashSet<>();

}
