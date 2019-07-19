package org.n52.testbed.routing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Link
 */
@Validated
public class Link {
    @JsonProperty("href")
    private String href = null;

    @JsonProperty("rel")
    private String rel = null;

    @JsonProperty("type")
    private String type = null;

    @JsonProperty("hreflang")
    private String lang = null;

    @JsonProperty("title")
    private String title = null;

    public Link href(String href) {
        this.href = href;
        return this;
    }

    @NotNull
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Link rel(String rel) {
        this.rel = rel;
        return this;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public Link type(String type) {
        this.type = type;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Link hreflang(String hreflang) {
        this.lang = hreflang;
        return this;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Link title(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(this.href, link.href) &&
                Objects.equals(this.rel, link.rel) &&
                Objects.equals(this.type, link.type) &&
                Objects.equals(this.lang, link.lang) &&
                Objects.equals(this.title, link.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, rel, type, lang, title);
    }
}
