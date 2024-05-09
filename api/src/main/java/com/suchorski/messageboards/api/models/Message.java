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
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(indexes = {
        @Index(name = "idx_message_finalization_date", columnList = "finalization_date")
})
public class Message implements Serializable {

    private static final long serialVersionUID = ApiApplication.VERSION;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    @NotBlank
    @Lob
    private String text;

    @Column(name = "creation_date", updatable = false, nullable = false)
    @ColumnDefault("current_timestamp")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant creationDate = Instant.now();

    @Column(name = "last_update_date", nullable = false)
    @ColumnDefault("current_timestamp")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant lastUpdateDate = Instant.now();

    @Column(name = "finalization_date")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant finalizationDate;

    @OneToMany(mappedBy = "message", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH })
    @JsonIgnore
    private Set<Comment> comments = new HashSet<>();

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false, nullable = false, foreignKey = @ForeignKey(name = "fk_message_board"))
    @NotNull
    private Board board;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(updatable = false, nullable = false, foreignKey = @ForeignKey(name = "fk_message_author"))
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User author;

}
