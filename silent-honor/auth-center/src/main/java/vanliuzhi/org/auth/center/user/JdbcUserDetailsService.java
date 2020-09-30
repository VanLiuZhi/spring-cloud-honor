// package vanliuzhi.org.auth.center.user;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
//
// /**
//  * @author Lys3415
//  * @date 2020/9/30 15:53
//  */
// @Service
// public class JdbcUserDetailsService implements UserDetailsService {
//
//     @Autowired
//     private UsersRepository usersRepository;
//
//     @Override
//     public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//         Users users = usersRepository.findByUsername(s);
//         return new User(users.getUsername(),users.getPassword(),new
//                 ArrayList<>());
//     }
// }
