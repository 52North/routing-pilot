package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * Output
 */
@Validated
public class Output {
    @JsonProperty("id")
    private String id = null;
    @JsonProperty("transmissionMode")
    private TransmissionMode transmissionMode = null;
    @JsonProperty("format")
    private Format format = null;

    @Valid
    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Valid
    public TransmissionMode getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(TransmissionMode transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Output that = (Output) o;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getTransmissionMode(), that.getTransmissionMode()) &&
                Objects.equals(this.getFormat(), that.getFormat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTransmissionMode(), getFormat());
    }

}
