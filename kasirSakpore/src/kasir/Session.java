/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kasir;

/**
 *
 * @author yaniyan
 */

public class Session {
    private static String username;
    private static String role;

    public static void setUsername(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }

    public static void setRole(String r) {
        role = r;
    }

    public static String getRole() {
        return role;
    }

    public static void clear() {
        username = null;
        role = null;
    }
}
