package mangpo.server.controller;

import lombok.RequiredArgsConstructor;
import mangpo.server.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUserByEmail(@RequestParam String email){
        adminService.deleteUserByEmail(email);
        return ResponseEntity.noContent().build();
    }
}
