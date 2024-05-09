package com.suchorski.messageboards.api.models;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.suchorski.messageboards.api.ApiApplication;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Comment implements Serializable {

    private static final long serialVersionUID = ApiApplication.VERSION;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NonNull
    @Column(length = 4096, updatable = false, nullable = false)
    @NotBlank
    private String text;

    @Column(name = "creation_date", updatable = false, nullable = false)
    @ColumnDefault("current_timestamp")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant creationDate = Instant.now();

    @Column(nullable = false)
    @ColumnDefault("false")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean deleted = false;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_comment_message"))
    @NotNull
    @JsonIgnore
    private Message message;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_comment_user"))
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User author;

}
