
package org.hsu.hsuapp.util.taba;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "type",
    "effect",
    "id",
    "color",
    "msec",
    "name",
    "text",
    "next_url",
    "src"
})
public class Script {

    @JsonProperty("type")
    private String type;
    @JsonProperty("effect")
    private String effect;
    @JsonProperty("id")
    private String id;
    @JsonProperty("color")
    private String color;
    @JsonProperty("msec")
    private String msec;
    @JsonProperty("name")
    private String name;
    @JsonProperty("text")
    private String text;
    @JsonProperty("next_url")
    private String nextUrl;
    @JsonProperty("src")
    private String src;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("effect")
    public String getEffect() {
        return effect;
    }

    @JsonProperty("effect")
    public void setEffect(String effect) {
        this.effect = effect;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("color")
    public String getColor() {
        return color;
    }

    @JsonProperty("color")
    public void setColor(String color) {
        this.color = color;
    }

    @JsonProperty("msec")
    public String getMsec() {
        return msec;
    }

    @JsonProperty("msec")
    public void setMsec(String msec) {
        this.msec = msec;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("next_url")
    public String getNextUrl() {
        return nextUrl;
    }

    @JsonProperty("next_url")
    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    @JsonProperty("src")
    public String getSrc() {
        return src;
    }

    @JsonProperty("src")
    public void setSrc(String src) {
        this.src = src;
    }
    
	@Override
	public String toString() {
		return "Script [type=" + type + ", effect=" + effect + ", id=" + id + ", color=" + color + ", msec=" + msec
				+ ", name=" + name + ", text=" + text + ", nextUrl=" + nextUrl + ", src=" + src + "]";
	}
}
