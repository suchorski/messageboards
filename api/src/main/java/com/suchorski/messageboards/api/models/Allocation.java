package com.suchorski.messageboards.api.models;

import java.io.Serializable;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(uniqueConstraints = { @UniqueConstraint(name = "uq_allocation", columnNames = { "user_id", "board_id" }) })
public class Allocation implements Serializable {

    private static final long serialVersionUID = ApiApplication.VERSION;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, updatable = false, nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_allocation_user"))
    @NotNull
    private User user;

    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_allocation_board"))
    @NotNull
    private Board board;

    @NonNull
    @Column(nullable = false)
    private Boolean administrator;

    @NonNull
    @Column(nullable = false)
    @NotNull
    private Boolean moderator;

}
