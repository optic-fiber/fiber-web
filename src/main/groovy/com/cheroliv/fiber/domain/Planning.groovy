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
        @Index(name = "`idx_planning_tech_initial`", columnList = "`tech_initial`"),
        @Index(name = "`idx_planning_open`", columnList = "`open`"),
        @Index(name = "`idx_planning_date_creation`", columnList = "`date_creation`"),
        @Index(name = "`idx_planning_last_name_tech`", columnList = "`last_name_tech`"),
        @Index(name = "`idx_planning_first_name_tech`", columnList = "`first_name_tech`")])
class Planning implements Serializable {
    static final long serialVersionUID = 1L
    @Id
    @Column(name = "`id`")
    Long id
    @NotNull
    @Size(min = 4)
    @Column(name="`tech_initial`",nullable = false)
    String techInitial
    @NotNull
    @Column(name="`open`",nullable = false)
    Boolean open
    @NotNull
    @Column(name="`date_creation`")
    ZonedDateTime dateCreation
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

        if (dateCreation != planning.dateCreation) return false
        if (firstNameTech != planning.firstNameTech) return false
        if (lastNameTech != planning.lastNameTech) return false
        if (open != planning.open) return false
        if (techInitial != planning.techInitial) return false

        return true
    }

    int hashCode() {
        int result
        result = (techInitial != null ? techInitial.hashCode() : 0)
        result = 31 * result + (open != null ? open.hashCode() : 0)
        result = 31 * result + (dateCreation != null ? dateCreation.hashCode() : 0)
        result = 31 * result + (lastNameTech != null ? lastNameTech.hashCode() : 0)
        result = 31 * result + (firstNameTech != null ? firstNameTech.hashCode() : 0)
        return result
    }
}
