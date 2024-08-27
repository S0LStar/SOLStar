package com.shinhan.solstar.domain.funding.entity;

import com.shinhan.solstar.domain.artist.entity.Artist;
import com.shinhan.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.shinhan.solstar.domain.user.entity.User;
import com.shinhan.solstar.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

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

    private int goalAmount;

    private LocalDate deadlineDate;

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

    @Setter
    @Enumerated(EnumType.STRING)
    private FundingStatus status;

    public static Funding createFunding(String title, String fundingImage, String content,
                                        int goalAmount, LocalDate deadlineDate, int totalAmount, int totalJoin,
                                        Artist artist, User host, FundingType type, FundingStatus status) {
        return Funding.builder()
                .title(title)
                .fundingImage(fundingImage)
                .content(content)
                .goalAmount(goalAmount)
                .deadlineDate(deadlineDate)
                .totalAmount(totalAmount)
                .totalJoin(totalJoin)
                .account(null)
                .artist(artist)
                .host(host)
                .type(type)
                .status(status)
                .build();
    }

    public void updateFundingDetails(FundingUpdateRequestDto fundingDto) {
        if (fundingDto.getTitle() != null) {
            this.title = fundingDto.getTitle();
        }

        if (fundingDto.getFundingImage() != null) {
            this.fundingImage = fundingDto.getFundingImage();
        }

        if (fundingDto.getContent() != null) {
            this.content = fundingDto.getContent();
        }
    }

    public void deleteFunding() {
        this.isDelete = true;
    }

}
