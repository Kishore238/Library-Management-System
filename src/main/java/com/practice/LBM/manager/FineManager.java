package com.practice.LBM.manager;

public interface FineManager {
    Integer getForUser(String username) throws Exception;
    void payForUser(String username,Integer amount) throws Exception;
}
