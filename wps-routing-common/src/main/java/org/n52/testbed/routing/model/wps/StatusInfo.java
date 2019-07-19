package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * StatusInfo
 */
@Validated
public class StatusInfo {
    @JsonProperty("status")
    private Status status = null;

    @JsonProperty("message")
    private String message = null;

    @JsonProperty("progress")
    private Integer progress = null;

    @NotNull
    @Valid
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Min(0)
    @Max(100)
    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusInfo)) return false;
        StatusInfo that = (StatusInfo) o;
        return getStatus() == that.getStatus() &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getProgress(), that.getProgress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getProgress());
    }
}
