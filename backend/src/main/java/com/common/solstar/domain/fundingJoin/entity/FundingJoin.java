package com.common.solstar.domain.fundingJoin.entity;

import com.common.solstar.domain.funding.entity.Funding;
import com.common.solstar.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundingJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id", referencedColumnName = "id", nullable = false)
    private Funding funding;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private LocalDateTime date;

}
