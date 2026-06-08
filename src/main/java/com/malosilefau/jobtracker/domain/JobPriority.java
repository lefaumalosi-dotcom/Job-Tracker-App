package com.malosilefau.jobtracker.domain;

public enum JobPriority {
  LOW("Low", "priority-low"),
  MEDIUM("Medium", "priority-medium"),
  HIGH("High", "priority-high");

  private final String label;
  private final String cssClass;

  JobPriority(String label, String cssClass) {
    this.label = label;
    this.cssClass = cssClass;
  }

  public String getLabel() {
    return label;
  }

  public String getCssClass() {
    return cssClass;
  }
}
