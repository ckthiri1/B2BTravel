package Projet.tools;

import Projet.entities.User;

public class UserSession {

        private static UserSession instance;
        private User currentUser;

        private UserSession() {}

        public static UserSession getInstance() {
            if (instance == null) {
                instance = new UserSession();
            }
            return instance;
        }

        public void setUser(User user) {
            this.currentUser = user;
        }

        public User getUser() {
            return currentUser;
        }

        public int getUserId() {
            return currentUser != null ? currentUser.getUser_id(): -1;
        }

        public void clearSession() {
            currentUser = null;
        }
    }

