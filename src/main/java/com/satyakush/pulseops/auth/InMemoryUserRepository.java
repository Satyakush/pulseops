package com.satyakush.pulseops.auth;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!postgres")
public class InMemoryUserRepository implements UserRepository { private final ConcurrentMap<UUID,User> users=new ConcurrentHashMap<>(); public Optional<User> findById(UUID id){return Optional.ofNullable(users.get(id));} public Optional<User> findByUsernameIgnoreCase(String username){return users.values().stream().filter(u->u.username().equalsIgnoreCase(username)).findFirst();} public User save(User user){users.put(user.id(),user);return user;} public boolean existsByUsernameIgnoreCase(String username){return users.values().stream().anyMatch(u->u.username().equalsIgnoreCase(username));} }
