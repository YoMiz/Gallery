package com.example.app.domain;

import java.sql.Date;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReservation {
    @NotNull
    private Integer id;

    @NotNull
    private Integer userId;

    @NotNull
    @Size(min = 1, max = 100) // ユーザー名は1文字以上100文字以下
    private String userName;

    @NotNull
    private Integer eventId;

    @NotNull
    @Size(min = 1, max = 100) // イベント名は1文字以上100文字以下
    private String eventName;

    @NotNull
    @Min(1) // 大人の数は1以上
    private Integer adultNum;
    private Integer childNum;

    @NotNull
    @Min(0) // 価格は0以上
    private Integer price;

    @Future
    private Date reservationDate;

    private Date created;

    @Min(0) // ステータスは0以上
    private Integer status;

    private Integer point;

    private Date selectedDate;
	}

