package com.techprimers.transactionalitydemo.runner;

import com.techprimers.transactionalitydemo.model.User;
import com.techprimers.transactionalitydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class UserRunner implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... strings) throws Exception {
        //todo: choose here one of the methods to demonstrate Transactional behavior
        //insertInUserTableAndFail();
        //insert2Lists();

        System.out.println(userService.getUsers());
    }

    /**
     * Calls @Transactional userService.insertIn2Batches, which does insert twice. Rolls back both inserts
     * */
    public void insert2Lists() {
        try {
            User user1 = new User("Peter", "Ops", 12000L);
            User user2 = new User("Sam", "Tech", 22000L);
            User user3 = new User("Nick", "Ops", 18000L);
            User user4 = new User("Ryan King", "Tech", 32000L);
            User user5 = new User("user5", "Tech", 32000L);

            userService.insertIn2Batches(Arrays.asList(user1, user2), Arrays.asList(user3, user4, user5));
        }
        catch (RuntimeException exception) {
            System.out.println("Exception in insert2Lists...!" + exception.getMessage());
        }
    }

    /**
     * As invalid User is 4th, all 3 users will get inserted into db
     * @Transactional DOES NOT WORK in this case, and is put in the code to demonstrate a point
     * My experiments showed it only takes effect in file, where JdbcTemplate is directly autowired
     * */
    @Transactional
    public void insertInUserTableAndFail() {
        try {

            User user1 = new User("Peter", "Ops", 12000L);
            User user2 = new User("Sam", "Tech", 22000L);
            userService.insert(Arrays.asList(
                    user1, user2
            ));
            }
        catch (RuntimeException exception) {
            System.out.println("Exception in batch 1...!" + exception.getMessage());
        }


            try {
            User user3 = new User("Nick", "Ops", 18000L);
            User user4 = new User("Mike", "Tech", 32000L);
            User user5 = new User("Ryan King", "Tech", 32000L);
            //ANOTHER VALID USER, that will not be inserted, because method threw exception before execution got to it
            User user6 = new User("Dale", "Tech", 32000L);
            userService.insert(Arrays.asList(
                    user3, user4, user5, user6
            ));


        }
        catch (RuntimeException exception) {
            System.out.println("Exception in batch 2...!" + exception.getMessage());
        }
    }
}
