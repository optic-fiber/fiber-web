package com.cheroliv.fiber.domain

import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@CompileStatic
@Entity
@Table(name = "`authority`")
@ToString
class Authority implements Serializable {
    static final long serialVersionUID = 1L
    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50,name="`name`")
    String name

    @Override
    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Authority authority = (Authority) o

        if (name != authority.name) return false

        return true
    }

    @Override
    int hashCode() {
        return (name != null ? name.hashCode() : 0)
    }
}
