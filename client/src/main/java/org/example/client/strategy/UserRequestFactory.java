package org.example.client.strategy;

public class UserRequestFactory<T> {

    private final UserRequestStrategy<T> strategy;

    public UserRequestFactory(UserRequestStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public T createUserRequest(int itemCount) {
        return strategy.createUserRequest(itemCount);
    }
}
