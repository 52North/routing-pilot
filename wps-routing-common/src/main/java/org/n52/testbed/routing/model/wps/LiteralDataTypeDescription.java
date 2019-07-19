package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * LiteralDataTypeDescription
 */
@Validated
public class LiteralDataTypeDescription implements DataTypeDescription {
    @JsonProperty("literalDataDomains")
    @Valid
    private List<LiteralDataDomain> literalDataDomains = null;

    @Valid
    public List<LiteralDataDomain> getLiteralDataDomains() {
        return literalDataDomains;
    }

    public void setLiteralDataDomains(List<LiteralDataDomain> literalDataDomains) {
        this.literalDataDomains = literalDataDomains;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LiteralDataTypeDescription that = (LiteralDataTypeDescription) o;
        return Objects.equals(this.getLiteralDataDomains(), that.getLiteralDataDomains());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLiteralDataDomains());
    }
}
