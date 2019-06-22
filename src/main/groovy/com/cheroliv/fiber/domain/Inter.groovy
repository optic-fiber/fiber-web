package com.cheroliv.fiber.domain

import com.cheroliv.fiber.domain.enumeration.ContractEnum
import com.cheroliv.fiber.domain.enumeration.TypeInterEnum
import groovy.transform.CompileStatic
import groovy.transform.ToString

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.ZonedDateTime

@CompileStatic
@ToString
@Entity
@Table(name = "`inter`", indexes = [
        @Index(name = "`uniq_idx_nd_type`", columnList = "`nd`,`type_inter`", unique = true),
        @Index(name = "`idx_inter_type`", columnList = "`type_inter`"),
        @Index(name = "`idx_inter_contract`", columnList = "`contract`"),
        @Index(name = "`idx_inter_date_time_inter`", columnList = "`date_time_inter`"),
        @Index(name = "`idx_inter_first_name_client`", columnList = "`first_name_client`"),
        @Index(name = "`idx_inter_last_name_client`", columnList = "`last_name_client`")])
class Inter implements Serializable, InterConstants {
    static final long serialVersionUID = 1L
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "`id`")
    Long id
    @Column(name = "`nd`")
    @NotNull
    @Size(min = 10, max = 10)
    String nd
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "`type_inter`", nullable = false)
    TypeInterEnum typeInter
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "`contract`", nullable = false)
    ContractEnum contract
    @Column(name = "`date_time_inter`")
    ZonedDateTime dateTimeInter
    @Size(max = 100)
    @Column(name = "`first_name_client`")
    String firstNameClient
    @Size(max = 100)
    @Column(name = "`last_name_client`")
    String lastNameClient
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "`planning_id`")
    Planning planning
}
