
package org.hsu.hsuapp.util.taba;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "script"
})
public class Datum {

    @JsonProperty("id")
    private String id;
    @JsonProperty("script")
    private List<Script> script = null;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("script")
    public List<Script> getScript() {
        return script;
    }

    @JsonProperty("script")
    public void setScript(List<Script> script) {
        this.script = script;
    }

	@Override
	public String toString() {
		return "Datum [id=" + id + ", script=" + script + "]";
	}
	
}
