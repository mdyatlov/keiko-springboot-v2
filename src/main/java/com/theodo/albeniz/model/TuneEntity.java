package com.theodo.albeniz.model;

import lombok.*;

import jakarta.persistence.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TUNE")
@Getter @Setter
public class TuneEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "RELEASE_DATE")
    private String releaseDate;

    @Column(name = "ALBUM")
    private String album;
}
