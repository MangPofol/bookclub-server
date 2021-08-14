package mangpo.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import mangpo.server.entity.User;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Builder
public class UserRequestDto {

    private String userPassword;
    private String email;

    public User toEntity(){

        return User.builder()
                .email(this.email)
                .userPassword(this.userPassword)
                .build();
    }
}
