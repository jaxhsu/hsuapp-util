
package org.hsu.hsuapp.trans.rpgmakermv.pojo.CommonEvents;

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
    "code",
    "indent",
    "parameters"
})
public class CommonEventsList {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("indent")
    private Integer indent;
    @JsonProperty("parameters")
    private java.util.List<Object> parameters = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonProperty("indent")
    public Integer getIndent() {
        return indent;
    }

    @JsonProperty("indent")
    public void setIndent(Integer indent) {
        this.indent = indent;
    }

    @JsonProperty("parameters")
    public java.util.List<Object> getParameters() {
        return parameters;
    }

    @JsonProperty("parameters")
    public void setParameters(java.util.List<Object> parameters) {
        this.parameters = parameters;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	@Override
	public String toString() {
		return "List [code=" + code + ", indent=" + indent + ", parameters=" + parameters + ", additionalProperties="
				+ additionalProperties + "]";
	}
}
