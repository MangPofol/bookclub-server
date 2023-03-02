package mangpo.server.repository.cbu;

import mangpo.server.dto.cbu.ClubBookUserSearchCondition;
import mangpo.server.entity.cbu.ClubBookUser;

import java.util.List;

public interface ClubBookUserRepositoryCustom {
    List<ClubBookUser> findAllBySearchCondition(ClubBookUserSearchCondition cbuSearchCondition);

    Boolean isDuplicate(ClubBookUserSearchCondition cbuSearchCondition);
}
