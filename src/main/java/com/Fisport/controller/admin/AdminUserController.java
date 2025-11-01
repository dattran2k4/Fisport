package com.Fisport.controller.admin;


import com.Fisport.repository.UserRepository;
import com.Fisport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/users")
public class    AdminUserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    // Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"", "/", "/list"})
    public String listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) com.Fisport.common.ERole role,
            @RequestParam(required = false) com.Fisport.common.EUserStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // enforce a sensible page size (defensive)
        int safeSize = size <= 0 ? 10 : size;
        var usersPage = userService.getAllUsersPaged(keyword, role, status, page, safeSize);

        // Add statistics
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus(com.Fisport.common.EUserStatus.ACTIVE);
        long newUsersThisMonth = userRepository.countCreatedAfter(LocalDateTime.now().minusMonths(1));

        model.addAttribute("usersPage", usersPage);
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        model.addAttribute("role", role);
        model.addAttribute("status", status);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("newUsers", newUsersThisMonth);

        return "admin/users/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        var user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/users/view";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        var user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", com.Fisport.common.ERole.values());
        model.addAttribute("statuses", com.Fisport.common.EUserStatus.values());
        return "admin/users/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, 
                           @ModelAttribute com.Fisport.dto.request.UpdateProfileRequest request,
                           @RequestParam com.Fisport.common.ERole role,
                           @RequestParam com.Fisport.common.EUserStatus status,
                           java.security.Principal principal) {
        userService.adminUpdateUser(id, request, role, status);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id, java.security.Principal principal) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/change-status")
    public String changeStatus(@RequestParam Long id,
                               @RequestParam com.Fisport.common.EUserStatus status,
                               java.security.Principal principal) {
        userService.changeStatus(id, status);
        return "redirect:/admin/users";
    }

    // API endpoint for AJAX status toggle
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/change-status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> changeStatusAjax(@RequestParam Long id,
                                                               @RequestParam com.Fisport.common.EUserStatus status,
                                                               java.security.Principal principal) {
        try {
            userService.changeStatus(id, status);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("status", status.name());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error changing user status", e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}