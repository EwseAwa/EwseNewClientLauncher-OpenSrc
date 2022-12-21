
package cn.Ewse.ClientLauncher;

import com.mojang.authlib.AuthenticationCpp;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;

public class Api {

    public static String Version = "ewse";

    public static AuthenticationCpp auth;
    public static int socketPort = 13337;
    public static String token;
    public static String uuid;
    public static String Uname;

    public static int neteaseControlPort;

    public static void fuckNeteaseBox() {
        try {
            String line = System.getProperty("sun.java.command");
            uuid = line.substring(line.lastIndexOf("--uuid ") + 7, line.indexOf(" --accessToken")).replace(" ", "");
            token = line.substring(line.lastIndexOf("--accessToken ") + 14, line.lastIndexOf("--accessToken ") + 47).replace(" ", "");
            Uname = line.substring(line.lastIndexOf("--username ") + 11, line.indexOf(" --version")).replace(" ", "");
            neteaseControlPort = Integer.parseInt(System.getProperty("launcherControlPort"));
            JOptionPane.showMessageDialog(null,"成功的进行了一个初始化.","EwseNewClientLauncher-Dll",1);
            JOptionPane.showMessageDialog(null,
                    "Name:" + Uname + "\n" +
                            "NeteaseControlPort:" + neteaseControlPort + "\n" +
                            "UUID:" + uuid + "\n" +
                            "Token:" + token + "\n" +
                            "请点击确定进行开端,本CL仅Ewse授权使用."
                    , "EwseNewClientLauncher-Dll", 1);
            ServerSocket serverSocket = new ServerSocket(socketPort);
            while (true) {
                Socket client = serverSocket.accept();
                new Listener(client);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
