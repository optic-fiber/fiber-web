package com.cheroliv.fiber.domain

import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.ZonedDateTime

@CompileStatic
@ToString
@Entity
@Table(name = "`planning`",indexes = [
        @Index(name = "`idx_planning_initial_tech`", columnList = "`initial_tech`"),
        @Index(name = "`idx_planning_open`", columnList = "`open`"),
        @Index(name = "`idx_planning_date_time_creation`", columnList = "`date_time_creation`"),
        @Index(name = "`idx_planning_last_name_tech`", columnList = "`last_name_tech`"),
        @Index(name = "`idx_planning_first_name_tech`", columnList = "`first_name_tech`")])
class Planning implements Serializable {
    static final long serialVersionUID = 1L
    @Id
    @Column(name = "`id`")
    Long id
    @NotNull
    @Size(min = 4)
    @Column(name="`initial_tech`",nullable = false)
    String initialTech
    @NotNull
    @Column(name="`open`",nullable = false)
    Boolean open
    @NotNull
    @Column(name="`date_time_creation`")
    ZonedDateTime dateTimeCreation
    @Size(max = 100)
    @Column(name="`last_name_tech`",length = 100)
    String lastNameTech
    @Size(max = 100)
    @Column(name="`first_name_tech`",length = 100)
    String firstNameTech
    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "`id`", nullable = false)
    @MapsId
    User user

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Planning planning = (Planning) o

        if (dateTimeCreation != planning.dateTimeCreation) return false
        if (firstNameTech != planning.firstNameTech) return false
        if (lastNameTech != planning.lastNameTech) return false
        if (open != planning.open) return false
        if (initialTech != planning.initialTech) return false

        return true
    }

    int hashCode() {
        int result
        result = (initialTech != null ? initialTech.hashCode() : 0)
        result = 31 * result + (open != null ? open.hashCode() : 0)
        result = 31 * result + (dateTimeCreation != null ? dateTimeCreation.hashCode() : 0)
        result = 31 * result + (lastNameTech != null ? lastNameTech.hashCode() : 0)
        result = 31 * result + (firstNameTech != null ? firstNameTech.hashCode() : 0)
        return result
    }
}
