package com.shinhan.solstar.domain.fundingAgency.entity;

import com.shinhan.solstar.domain.agency.entity.Agency;
import com.shinhan.solstar.domain.funding.entity.Funding;
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

}
