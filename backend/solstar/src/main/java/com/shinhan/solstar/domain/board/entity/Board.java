package com.shinhan.solstar.domain.board.entity;

import com.shinhan.solstar.domain.funding.entity.Funding;
import com.shinhan.solstar.global.baseTimeEntity.BaseTimeEntity;
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
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funding_id", nullable = false)
    private Funding funding;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", nullable = false, length = 1500)
    private String content;

//    @Column(name = "content_image", nullable = true, length = 255)
//    private String contentImage;

    @Column(name = "is_delete", nullable = true)
    private Boolean isDelete;

    public static Board createBoard(Funding funding, String title, String content) {
        return Board.builder()
                .funding(funding)
                .title(title)
                .content(content)
                .isDelete(false)
                .build();
    }

}
