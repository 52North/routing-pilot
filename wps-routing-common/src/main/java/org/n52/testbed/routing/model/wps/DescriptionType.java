package org.n52.testbed.routing.model.wps;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.n52.testbed.routing.model.Link;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * DescriptionType
 */
@Validated
public class DescriptionType {
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("keywords")
    @Valid
    private List<String> keywords;

    @JsonProperty("metadata")
    @Valid
    private List<Link> metadata;

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Valid
    public List<Link> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Link> metadata) {
        this.metadata = metadata;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DescriptionType that = (DescriptionType) o;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.getTitle(), that.getTitle()) &&
                Objects.equals(this.getDescription(), that.getDescription()) &&
                Objects.equals(this.getKeywords(), that.getKeywords()) &&
                Objects.equals(this.getMetadata(), that.getMetadata());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getKeywords(), getMetadata());
    }

}
