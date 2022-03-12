package mangpo.server.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import mangpo.server.entity.user.User;
import mangpo.server.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String username) {
      return userRepository.findWithAuthByEmail(username)
         .map(user -> createUser(username, user))
         .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(String username, User user) {
//      if (user.getIsDormant()) {
//         log.info("비활성 상태 유저 : {}", username);
//         throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
//      }
      List<GrantedAuthority> grantedAuthorities = user.getUserAuthorityList().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().getAuthorityName()))
              .collect(Collectors.toList());
      return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()),
              user.getPassword(),
              grantedAuthorities);
   }
}
