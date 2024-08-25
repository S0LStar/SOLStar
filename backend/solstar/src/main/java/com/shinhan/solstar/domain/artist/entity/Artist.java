package com.shinhan.solstar.domain.artist.entity;

import com.shinhan.solstar.domain.agency.entity.Agency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @Enumerated(EnumType.STRING)
    private ArtistType type;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "group", nullable = true, length = 45)
    private String group;

    @Column(name = "profile_image", nullable = true, length = 255)
    private String profileImage;

    @Column(name = "popularity", nullable = false)
    private int popularity;

    @Column(name = "is_delete", nullable = false)
    private boolean isDelete;


}
