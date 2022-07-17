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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "view_order", nullable = false)
    private int viewOrder;

    protected ChannelSubscription() {
    }

    public ChannelSubscription(final Channel channel, final Member member, final int viewOrder) {
        this.channel = channel;
        this.member = member;
        this.viewOrder = viewOrder;
    }

    public void changeOrder(int order) {
        this.viewOrder = order;
    }

    public Long getChannelId() {
        return this.channel.getId();
    }
}
