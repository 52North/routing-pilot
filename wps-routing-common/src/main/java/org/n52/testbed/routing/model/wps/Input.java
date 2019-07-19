package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Input
 */
@Validated
public class Input {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("input")
    private Data input = null;

    public Input() {
    }

    public Input(String id) {
        this(id, null);
    }

    public Input(String id, Data input) {
        this.id = id;
        this.input = input;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull
    public Data getInput() {
        return input;
    }

    public void setInput(Data input) {
        this.input = input;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Input that = (Input) o;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getInput(), that.getInput());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInput());
    }

}
