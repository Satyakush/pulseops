package com.satyakush.pulseops.auth;

import org.springframework.stereotype.Service;

@Service
public class SessionService {
    private final AuthSessionRepository sessions;
    private final UserRepository users;
    public SessionService(AuthSessionRepository sessions, UserRepository users) { this.sessions = sessions; this.users = users; }
    public void revoke(String token) { sessions.deleteByToken(token); }
    public AuthenticatedUser authenticate(String token) {
        AuthSession session = sessions.findByToken(token).orElseThrow(SessionNotFoundException::new);
        if (session.expired()) throw new SessionNotFoundException();
        User user = users.findById(session.userId()).filter(User::active).orElseThrow(SessionNotFoundException::new);
        return new AuthenticatedUser(user.id(), user.username(), user.role());
    }
}
