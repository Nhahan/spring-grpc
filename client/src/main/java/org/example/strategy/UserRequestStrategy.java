package org.example.strategy;

public interface UserRequestStrategy<T> {
    T createUserRequest(int itemCount);
}
