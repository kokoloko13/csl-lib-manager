package pl.dgutowski.csl_lib_manager.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT u.role FROM User u WHERE u.email=:email")
    UserRole findRoleByEmail(@Param("email") String email);
}
