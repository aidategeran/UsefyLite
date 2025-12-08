package com.aida.usefy_lite.repository;

import com.aida.usefy_lite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /*
     You can define custom query methods if needed, e.g.:
     Spring Data JPA:
     1) Looks at the method name (findByUsername)
     2) Parses it
     */
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);



}
