package com.github.supercodingfinalprojectbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "abstractAccountId", callSuper = false)
@Table(name = "user_abstract_accounts")
public class UserAbstractAccount extends CommonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abstract_account_id")
    private Long abstractAccountId;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "paymoney")
    private Long paymoney;

    private transient final ReentrantLock chargePaymentLock = new ReentrantLock();

    private UserAbstractAccount(Long abstractAccountId, String accountNumber, Long paymoney) {
        this.abstractAccountId = abstractAccountId;
        this.accountNumber = accountNumber;
        this.paymoney = paymoney;
    }

    public static UserAbstractAccount of() {
        return new UserAbstractAccount(null, createAccountNumber(), 0L);
    }

    private static String createAccountNumber() {
        long seed = Instant.now().toEpochMilli();
        Random random = new Random(seed);
        int min = 0x1000;
        int max = 0xffff;

        String num1 = Integer.toHexString(random.nextInt(max - min + 1) + min);
        long seconds = seed / 1000;    // 초 단위
        long time = seconds % LocalDateTime.of(2070, 1, 1, 0, 0, 0).toEpochSecond(ZoneOffset.UTC);    // 0 ~ 99년 12월 31일 23시 59분 59초
        String num2 = String.format("%08x", time);
        String num3 = Integer.toHexString(random.nextInt(max - min + 1) + min);
        String num4 = Integer.toHexString(random.nextInt(max - min + 1) + min);
        return num1 + "-" + num2 + "-" + num3 + "-" + num4;
    }

    public static UserAbstractAccount dummy() {
        return new UserAbstractAccount(null, createAccountNumber(), randomPaymoney());
    }

    private static Long randomPaymoney() {
        final int min = 1;
        final int max = 1000;
        final double value = min + (max - min) * new Random().nextDouble();
        return Math.round(value) * 1_000L;
    }

    public Long chargePaymoney(Long chargeAmount) {
        try {
            chargePaymentLock.lock();
            this.paymoney += chargeAmount;
        } finally {
            chargePaymentLock.unlock();
        }

        return this.paymoney;
    }

    public void spendPayMoney(Integer price) {
        this.paymoney -= price;
    }

    public Long chargePaymoneyNoLock(long paymoney) {
        return this.paymoney += paymoney;
    }
}
