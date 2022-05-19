package model;

public class User {
    private String account;
    private String password;

    public User (String account, String password){
        this.account = account;
        this.password = password;
    }

    public boolean checkPassword(String password)
    {
        if (password==null)
        {
            return false;
        }
        else
        {
            return this.password.equals(password);
        }
    }

    public String changePassWord(String oldPassword,String newPassword) {
        if (password.equals(oldPassword))
        {
            password = newPassword;
            return null;
        }
        else
        {
            return "Wrong password.";
        }
    }

    public String getAccount() {
        return account;
    }
}
