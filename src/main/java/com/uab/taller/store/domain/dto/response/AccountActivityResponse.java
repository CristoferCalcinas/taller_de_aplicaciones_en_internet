package com.uab.taller.store.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountActivityResponse {

    private Long accountId;
    private String accountNumber;
    private Integer totalActivities;
    private List<ActivityItem> activities;
    private ActivityStats stats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityItem {
        private String activityType;
        private String description;
        private String timestamp;
        private String userAgent;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityStats {
        private String period;
        private Integer totalActivities;
        private Integer accessCount;
        private Integer modificationCount;
        private Integer balanceOperations;
    }
}
