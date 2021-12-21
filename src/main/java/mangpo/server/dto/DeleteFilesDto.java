package mangpo.server.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteFilesDto {

    List<String> imgLocations;
}
