package com.github.supercodingfinalprojectbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@Table(name = "mentee_social_infos")
public class MenteeSocialInfo extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mentee_social_info_id")
    private Long menteeSocialId;

    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private Mentee mentee;

    @Column(name = "social_id")
    private Long socialId;

    @Column(name = "social_platform")
    private String socialPlatform;
}
