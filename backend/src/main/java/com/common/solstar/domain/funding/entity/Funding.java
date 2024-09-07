package com.common.solstar.domain.funding.entity;

import com.common.solstar.domain.artist.entity.Artist;
import com.common.solstar.domain.funding.dto.request.FundingUpdateRequestDto;
import com.common.solstar.domain.user.entity.User;
import com.common.solstar.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

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

    @Column(nullable = false)
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

        if (fundingDto.getContent() != null) {
            this.content = fundingDto.getContent();
        }
    }

    public void updateByJoin(int amount) {
        this.totalAmount += amount;
        this.totalJoin += 1;
    }

    public void setFundingImage(String fundingImage) {
        this.fundingImage = fundingImage;
    }

    // 펀딩 성공 시 계좌번호 업데이트
    public void createAccount(String account) {
        this.account = account;
    }

    public void deleteFunding() {
        this.isDelete = true;
    }

    public void updateStatusProcessing(){
        this.status = FundingStatus.PROCESSING;
    }

}
