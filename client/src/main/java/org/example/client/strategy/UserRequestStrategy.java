package org.example.client.strategy;

public interface UserRequestStrategy<T> {
    T createUserRequest(int itemCount);
}
