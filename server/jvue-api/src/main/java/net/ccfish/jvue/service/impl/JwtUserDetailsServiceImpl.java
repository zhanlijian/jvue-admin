package net.ccfish.jvue.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.ccfish.jvue.model.JvueRole;
import net.ccfish.jvue.model.User;
import net.ccfish.jvue.repository.UserRepository;
import net.ccfish.jvue.security.JwtUserDetails;

/**
 * 用户验证方法
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public JwtUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(value = "JwtUserDetailsService", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(
                    String.format("No user found with username '%s'.", username));
        } else {
            
            Set<Integer> roles = user.getRoles();
            
            return new JwtUserDetails(user.getId(), user.getUsername(), user.getPassword(),
                    user.getSuperUser(), user.getNickname(), user.getEmail(), user.getAuthorities(), roles);
        }
    }

}
