package com.Banking_application.Persistence.service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long boardId;
    private String title;
    private Long ownerId;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
    private  LocalDateTime updatedAt;


    @PreUpdate
    public void preUpdate()
    {
        updatedAt=LocalDateTime.now();
    }

    @OneToMany(mappedBy = "board" ,cascade = CascadeType.ALL, orphanRemoval = true)
    List<boardMember> members;

    @OneToMany(mappedBy = "board" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Element> elements;

//    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Snapshot> snapshots;

}
