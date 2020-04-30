
package org.hsu.hsuapp.util.baidu;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "src",
    "dst"
})
public class TransResult {

    @JsonProperty("src")
    private String src;
    @JsonProperty("dst")
    private String dst;

    @JsonProperty("src")
    public String getSrc() {
        return src;
    }

    @JsonProperty("src")
    public void setSrc(String src) {
        this.src = src;
    }

    @JsonProperty("dst")
    public String getDst() {
        return dst;
    }

    @JsonProperty("dst")
    public void setDst(String dst) {
        this.dst = dst;
    }

	@Override
	public String toString() {
		return "TransResult [src=" + src + ", dst=" + dst + "]";
	}
}
