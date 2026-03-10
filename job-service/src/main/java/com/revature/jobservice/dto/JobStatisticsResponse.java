package com.revature.jobservice.dto;

import java.util.Objects;

public class JobStatisticsResponse {

    private Long totalJobs;
    private Long openJobs;
    private Long closedJobs;
    private Long filledJobs;

    public JobStatisticsResponse() {
    }

    public JobStatisticsResponse(Long totalJobs, Long openJobs, Long closedJobs, Long filledJobs) {
        this.totalJobs = totalJobs;
        this.openJobs = openJobs;
        this.closedJobs = closedJobs;
        this.filledJobs = filledJobs;
    }

    public Long getTotalJobs() {
        return totalJobs;
    }

    public void setTotalJobs(Long totalJobs) {
        this.totalJobs = totalJobs;
    }

    public Long getOpenJobs() {
        return openJobs;
    }

    public void setOpenJobs(Long openJobs) {
        this.openJobs = openJobs;
    }

    public Long getClosedJobs() {
        return closedJobs;
    }

    public void setClosedJobs(Long closedJobs) {
        this.closedJobs = closedJobs;
    }

    public Long getFilledJobs() {
        return filledJobs;
    }

    public void setFilledJobs(Long filledJobs) {
        this.filledJobs = filledJobs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobStatisticsResponse that = (JobStatisticsResponse) o;
        return Objects.equals(totalJobs, that.totalJobs) &&
               Objects.equals(openJobs, that.openJobs) &&
               Objects.equals(closedJobs, that.closedJobs) &&
               Objects.equals(filledJobs, that.filledJobs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalJobs, openJobs, closedJobs, filledJobs);
    }

    @Override
    public String toString() {
        return "JobStatisticsResponse{" +
                "totalJobs=" + totalJobs +
                ", openJobs=" + openJobs +
                ", closedJobs=" + closedJobs +
                ", filledJobs=" + filledJobs +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long totalJobs;
        private Long openJobs;
        private Long closedJobs;
        private Long filledJobs;

        public Builder totalJobs(Long totalJobs) {
            this.totalJobs = totalJobs;
            return this;
        }

        public Builder openJobs(Long openJobs) {
            this.openJobs = openJobs;
            return this;
        }

        public Builder closedJobs(Long closedJobs) {
            this.closedJobs = closedJobs;
            return this;
        }

        public Builder filledJobs(Long filledJobs) {
            this.filledJobs = filledJobs;
            return this;
        }

        public JobStatisticsResponse build() {
            return new JobStatisticsResponse(totalJobs, openJobs, closedJobs, filledJobs);
        }
    }
}
