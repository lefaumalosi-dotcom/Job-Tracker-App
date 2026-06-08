package com.malosilefau.jobtracker.domain;

public enum JobStatus {
  WISHLIST("Wishlist", "status-wishlist", false),
  APPLIED("Applied", "status-applied", false),
  INTERVIEW("Interview", "status-interview", false),
  OFFER("Offer", "status-offer", false),
  REJECTED("Rejected", "status-rejected", true),
  ARCHIVED("Archived", "status-archived", true);

  private final String label;
  private final String cssClass;
  private final boolean closed;

  JobStatus(String label, String cssClass, boolean closed) {
    this.label = label;
    this.cssClass = cssClass;
    this.closed = closed;
  }

  public String getLabel() {
    return label;
  }

  public String getCssClass() {
    return cssClass;
  }

  public boolean isClosed() {
    return closed;
  }
}
