package com.example.projectx;

import com.example.projectx.authentication.LoginActivity;
import com.example.projectx.authentication.SignUpActivity;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class LoginTest {
    @Test
    public void testLogin() {
        String email = "test@test.com";
        String password = "Tester";
        LoginActivity mylog = Mockito.mock(LoginActivity.class);
        Assert.assertFalse(mylog.login(email, password));
    }

    @Test
    public void testSignUp() {
        String name = "Ahmad";
        String email = "ahmadnader98@gmail.com";
        String password = "nadoora";
        int age = 40;
        String gender = "male";
        SignUpActivity act = Mockito.mock(SignUpActivity.class);
        Assert.assertTrue(act.signUp(name,email,age, gender, password));
    }
}
