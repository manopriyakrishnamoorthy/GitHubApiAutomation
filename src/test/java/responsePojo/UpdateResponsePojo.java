package responsePojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateResponsePojo {
	@JsonProperty(value="id")
	private String id;
	@JsonProperty(value="node_id")
	private String node_id;
	@JsonProperty(value="name")
	private String name;
	@JsonProperty(value="full_name")
	private String full_name;
	@JsonProperty(value="private")
	private boolean Private;
	
}
