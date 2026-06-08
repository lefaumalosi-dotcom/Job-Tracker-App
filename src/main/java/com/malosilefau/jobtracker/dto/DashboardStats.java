package com.malosilefau.jobtracker.dto;

public record DashboardStats(
    long total,
    long active,
    long wishlist,
    long applied,
    long interviewing,
    long offer,
    long rejected,
    long archived,
    long followUpsDue) {
}
