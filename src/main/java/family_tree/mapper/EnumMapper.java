package family_tree.mapper;

import family_tree.model.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class EnumMapper {

    // == Role ==
    public String roleToString(Role role) {
        return role == null ? null : role.name();
    }
    public Role stringToRole(String role) {
        return role == null ? null : Role.valueOf(role);
    }
}
