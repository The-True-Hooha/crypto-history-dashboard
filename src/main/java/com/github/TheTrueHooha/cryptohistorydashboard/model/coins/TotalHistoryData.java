package com.github.TheTrueHooha.cryptohistorydashboard.model.coins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalHistoryData {
    private String timestamp;
    private double value;
}
