package com.common.solstar.domain.fundingAgency.entity;

import com.common.solstar.domain.agency.entity.Agency;
import com.common.solstar.domain.funding.entity.Funding;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingAgency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id", referencedColumnName = "id", nullable = false)
    private Funding funding;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", referencedColumnName = "id", nullable = false)
    private Agency agency;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean status;

    public static FundingAgency createFundingAgency(Funding funding, Agency agency) {
        return FundingAgency.builder()
                .funding(funding)
                .agency(agency)
                .status(false)
                .build();
    }

    public void acceptFunding() {
        this.status = true;
    }

}
