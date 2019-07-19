package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValueType {
    @JsonProperty("inlineValue")
    private Object inlineValue;

    @JsonProperty("href")
    private String href;

    public ValueType inlineValue(Object inlineValue) {
        this.inlineValue = inlineValue;
        return this;
    }

    public Object getInlineValue() {
        return inlineValue;
    }

    public void setInlineValue(Object inlineValue) {
        this.inlineValue = inlineValue;
    }

    public ValueType href(String href) {
        this.href = href;
        return this;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValueType valueType = (ValueType) o;
        return Objects.equals(this.inlineValue, valueType.inlineValue) &&
               Objects.equals(this.href, valueType.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inlineValue, href);
    }

}
