package com.pickpick.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "channel_subscription")
@Entity
public class ChannelSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "view_order", nullable = false)
    private int viewOrder;

    protected ChannelSubscription() {
    }
}
