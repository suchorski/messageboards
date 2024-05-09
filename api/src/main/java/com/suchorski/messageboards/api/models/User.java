package com.suchorski.messageboards.api.models;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.suchorski.messageboards.api.ApiApplication;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_cpf", columnNames = "cpf"),
}, indexes = {
        @Index(name = "idx_user_cpf", columnList = "cpf"),
        @Index(name = "idx_user_lastUpdate", columnList = "last_update"),
})
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = ApiApplication.VERSION;

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
    private String company;

    @Column(name = "last_update", nullable = false)
    @ColumnDefault("current_timestamp")
    @JsonIgnore
    @Builder.Default
    private Instant lastUpdate = Instant.now();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private Set<Allocation> allocations = new HashSet<>();

    @Transient
    public boolean isOneDayOld() {
        return lastUpdate.plusSeconds(60 * 60 * 24).isBefore(Instant.now());
    }

}
