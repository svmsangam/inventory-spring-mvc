package com.inventory.web.session;

/**
 * Created by dhiraj on 8/17/17.
 */
public class SessionInfo {

   /* @Autowired
    private SessionRegistry sessionRegistry;

    public void expireUserSessions(String username) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof User) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getUsername().equals(username)) {
                    for (SessionInformation information : sessionRegistry.getAllSessions(userDetails, true)) {

                        System.out.println("session id : " + information.getSessionId());
                        information.expireNow();
                    }
                }
            }
        }
    }*/
}
