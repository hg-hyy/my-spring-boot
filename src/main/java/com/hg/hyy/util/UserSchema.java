package com.hg.hyy.util;

import java.io.IOException;

import java.util.*;

import io.protostuff.Input;
import io.protostuff.Output;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


public class UserSchema implements Schema<User> {
    public boolean isInitialized(User user) {
        return user.getEmail() != null;
    }

    private User message = new User();

    public void mergeFrom(Input input, User user) throws IOException {
        while (true) {
            int number = input.readFieldNumber(this);
            switch (number) {
            case 0:
                return;
            case 1:
                user.setEmail(input.readString());
                break;
            case 2:
                user.setFirstName(input.readString());
                break;
            case 3:
                user.setLastName(input.readString());
                break;
            case 4:
                if (message.friends == null)
                    message.friends = new ArrayList<User>();
                message.friends.add(input.mergeObject(null, this));
                break;
            default:
                input.handleUnknownField(number, this);
            }
        }
    }

    public void writeTo(Output output, User user) throws IOException {
        if (user.getEmail() == null)
            throw new UninitializedMessageException(user, this);

        output.writeString(1, user.getEmail(), false);

        if (user.getFirstName() != null)
            output.writeString(2, user.getFirstName(), false);

        if (user.getLastName() != null)
            output.writeString(3, user.getLastName(), false);

        if (message.friends != null) {
            for (User friend : message.friends) {
                if (friend != null)
                    output.writeObject(4, friend, this, true);
            }
        }
    }

    public User newMessage() {
        return new User();
    }

    public Class<User> typeClass() {
        return User.class;
    }

    public String messageName() {
        return User.class.getSimpleName();
    }

    public String messageFullName() {
        return User.class.getName();
    }

    // the mapping between the field names to the field numbers.

    public String getFieldName(int number) {
        switch (number) {
        case 1:
            return "email";
        case 2:
            return "firstName";
        case 3:
            return "lastName";
        case 4:
            return "friends";
        default:
            return null;
        }
    }

    public int getFieldNumber(String name) {
        Integer number = fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    private static final HashMap<String, Integer> fieldMap = new HashMap<String, Integer>();
    static {
        fieldMap.put("email", 1);
        fieldMap.put("firstName", 2);
        fieldMap.put("lastName", 3);
        fieldMap.put("friends", 4);
    }
}
    class User {
        String firstName;
        String lastName;
        String email;
        List<User> friends;

        public User() {
        }

        public User(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<User> getFriends() {
            return friends;
        }

        public void setFriends(List<User> friends) {
            this.friends = friends;
        }
    }
