package com.room.booking.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Maintenance Time Entity: Room id can be included as well for practical scenarios but this is currently not in scope
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "MAINTENANCE_TIME")
public class MaintenanceTimeEntity extends TimeDurationEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

}
