package com.shinhan.solstar.domain.funding.entity;

import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.user.entity.User;
import com.shinhan.solstar.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funding extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255)
    private String fundingImage;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 255)
    private String contentImage;

    private int goalAmount;

    private LocalDateTime deadlineDate;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private int totalJoin;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isDelete;

    @Column(length = 255)
    private String account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = false)
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", referencedColumnName = "id", nullable = false)
    private User host;

    @Enumerated(EnumType.STRING)
    private FundingType type;

    @Enumerated(EnumType.STRING)
    private FundingStatus status;

}
