package com.practice.LBM.manager;

import com.practice.LBM.entity.Fine;
import com.practice.LBM.entity.User;
import com.practice.LBM.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FineManagerImpl implements FineManager{
    @Autowired
    UserRepository userRepository;

    @Override
    public Integer getForUser(String username) throws Exception {
        User user = userRepository.findByUsername(username).orElseThrow(
                () ->new Exception("No such user Exists")
        );
        return user.getFine().getAmount();
    }

    @Override
    public void payForUser(String username, Integer amount) throws Exception {
        User user = userRepository.findByUsername(username).orElseThrow(
                () ->new Exception("No such user Exists")
        );
        Fine fine= user.getFine();
        Integer presentAmount = fine.getAmount();
        fine.setAmount(Math.max(0,presentAmount-amount));
        userRepository.save(user);

    }
}
