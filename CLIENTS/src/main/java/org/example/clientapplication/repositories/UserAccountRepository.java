package org.example.clientapplication.repositories;

import org.example.clientapplication.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository  extends JpaRepository<UserAccount, Long>{
    UserAccount findByUsername(String username);
    UserAccount findByEmail(String email);
    UserAccount findByUsernameAndPassword(String username, String password);
    UserAccount findByEmailAndPassword(String email, String password);

}
