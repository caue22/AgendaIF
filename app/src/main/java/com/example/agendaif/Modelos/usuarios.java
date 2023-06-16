package com.example.agendaif.Modelos;

public class usuarios {
    String UserId, UserName, ProfilePic;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public usuarios(String userId, String userName, String profilePic) {
        UserId = userId;
        UserName = userName;
        ProfilePic = profilePic;
    }
public usuarios(){}

}
