package com.xiaoxuan.news.model.entity;

/**
 * Created by chanplion on 2015/12/18 0018.
 */
public class PaymentRequest {

    String channel;
    int amount;

    public PaymentRequest(String channel, int amount) {
        this.channel = channel;
        this.amount = amount;
    }
}
