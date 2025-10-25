package com.Banking_application.Persistence.service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class boardMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long Id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private BoardRole role;

    @ManyToOne
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private Board board;

}
