package mangpo.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtil {

   private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

   private SecurityUtil() {
   }

   // SecurityContext 에 유저 정보가 저장되는 시점
   // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
   public static Long getCurrentUserId() {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || authentication.getName() == null) {
         throw  new RuntimeException("Security Context 에 인증 정보가 없습니다.");
      }

      return Long.parseLong(authentication.getName());
   }

//   //getCurrentUserName
//   public static Optional<Long> getCurrentUserId() {
//      System.out.println("@@@@@@@@@@@@111");
//
//      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//      if (authentication == null) {
//         logger.debug("Security Context에 인증 정보가 없습니다.");
//         return Optional.empty();
//      }
//
//      Long userId = null;
//      if (authentication.getPrincipal() instanceof UserDetails) {
//         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
//
//         System.out.println("@@@@@@@@@@@@1");
//         System.out.println(springSecurityUser.getUsername());
//
//         userId = Long.parseLong(springSecurityUser.getUsername());
//      } else if (authentication.getPrincipal() instanceof Long) {
//         userId = (Long) authentication.getPrincipal();
//      }
//
//      System.out.println("@@@@@@@@@@@@2");
//      System.out.println(userId);
//      return Optional.ofNullable(userId);
//   }

//   //getCurrentUserName
//   public static Optional<String> getCurrentUserId() {
//      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//      if (authentication == null) {
//         logger.debug("Security Context에 인증 정보가 없습니다.");
//         return Optional.empty();
//      }
//
//      String username = null;
//      if (authentication.getPrincipal() instanceof UserDetails) {
//         UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
//         username = springSecurityUser.getUsername();
//      } else if (authentication.getPrincipal() instanceof String) {
//         username = (String) authentication.getPrincipal();
//      }
//
//      return Optional.ofNullable(username);
//   }
}
