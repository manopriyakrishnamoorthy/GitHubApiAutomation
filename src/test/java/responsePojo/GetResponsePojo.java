package responsePojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
//@RequiredArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GetResponsePojo {
	@JsonProperty(value="id")
	public String id;
	@JsonProperty(value="node_id")
	public String node_id;
	@JsonProperty(value="name")
	public String name;
	@JsonProperty(value="full_name")
	public String full_name;
	@JsonProperty(value="private")
	public boolean Private;

}
