
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
    "id",
    "list",
    "name",
    "switchId",
    "trigger"
})
public class CommonEventsRoot {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("list")
    private java.util.List<org.hsu.hsuapp.trans.rpgmakermv.pojo.CommonEvents.CommonEventsList> list = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("switchId")
    private Integer switchId;
    @JsonProperty("trigger")
    private Integer trigger;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("list")
    public java.util.List<org.hsu.hsuapp.trans.rpgmakermv.pojo.CommonEvents.CommonEventsList> getList() {
        return list;
    }

    @JsonProperty("list")
    public void setList(java.util.List<org.hsu.hsuapp.trans.rpgmakermv.pojo.CommonEvents.CommonEventsList> list) {
        this.list = list;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("switchId")
    public Integer getSwitchId() {
        return switchId;
    }

    @JsonProperty("switchId")
    public void setSwitchId(Integer switchId) {
        this.switchId = switchId;
    }

    @JsonProperty("trigger")
    public Integer getTrigger() {
        return trigger;
    }

    @JsonProperty("trigger")
    public void setTrigger(Integer trigger) {
        this.trigger = trigger;
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
		return "CommonEventsRoot [id=" + id + ", list=" + list + ", name=" + name + ", switchId=" + switchId
				+ ", trigger=" + trigger + ", additionalProperties=" + additionalProperties + "]";
	}
}
