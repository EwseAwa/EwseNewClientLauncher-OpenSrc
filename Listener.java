
package cn.Ewse.ClientLauncher;

import com.netease.mc.mod.network.common.GameState;
import com.netease.mc.mod.network.common.Library;
import net.minecraft.launchwrapper.Launch;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.net.Socket;

public class Listener implements Runnable {
    private Socket socket;

    public Listener(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            try {
                DataInputStream input = new DataInputStream(this.socket.getInputStream());
                String clientInputStr = input.readUTF();
                if (clientInputStr.contains("EwseNewClientLauncher|")) {
                    System.out.println("[消息] 请求:" + this.socket.getRemoteSocketAddress() + "|" + clientInputStr);
                    DataOutputStream out = new DataOutputStream(this.socket.getOutputStream());
                    if (clientInputStr.contains("Connect")) {
                        try {
                            //clientInputStr.split("\\|")[2] is serverid
                            final Class  nativeClass = Launch.classLoader.loadClass("nativeTransformer");
                            final Field field_str = nativeClass.getDeclaredField("str");
                            field_str.setAccessible(true);
                            field_str.set(field_str,clientInputStr.split("\\|")[2]);
                            Library.Test();
                            Api.auth.Authentication(Api.neteaseControlPort, clientInputStr.split("\\|")[2]);
                        }catch (Exception e) {
                            out.writeUTF("错的，呜呜呜|" + Api.Version);
                            e.printStackTrace();
                            out.close();
                            input.close();
                        }
                        out.writeUTF("成功的进服了|" + GameState.gameid + "|" + GameState.launcherport);
                        out.close();
                        input.close();
                    }
                }
                this.socket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
                try {
                    this.socket.close();
                }
                catch (Exception e2) {
                    this.socket = null;
                }
            }
        } finally {
            if (this.socket != null) {
                try {
                    this.socket.close();
                }
                catch (Exception e) {
                    this.socket = null;
                }
            }
        }
    }
}
