package com.company;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.sql.SQLOutput;
import java.util.Arrays;

public class Server {

    ControlMouse mouse;

    Robot robot;

    private double version = 1.2;

    public void run() throws IOException {

        try (DatagramSocket udpServer = new DatagramSocket(3000)) {
            int portLocal = udpServer.getLocalPort();
            byte[] localIp = InetAddress.getLocalHost().getAddress();
            robot = new Robot();
            mouse = new ControlMouse(robot);

            System.out.printf("versão: %f\n", version);
            System.out.printf("Servidor aberto na porta: %d\n", portLocal);
            System.out.println("Ip: " + ipArrayToString(localIp));
            System.out.println("Basta digitar seu Ip e porta no app, e ligar o touchpad!");
            System.out.println("Para sair, pressione Ctrl + C.");
            System.out.println("Para melhor desempenho, conecte seu celular a uma rede criada pelo seu computador, \n"
                    + "ou, mantenha somente seu celular e o seu computador na mesma rede.");

            byte[] buf = new byte[12];
            DatagramPacket packet;
            int initialX = 0, initialY = 0;
            int toX = 0, toY = 0;
            double bias = 1;

            while (true) {
                packet = new DatagramPacket(buf, buf.length);
                udpServer.receive(packet);
                byte[] receive = packet.getData();
                if (receive[8] > 0) {
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    initialX = point.x;
                    initialY = point.y;
                }


                int receivedX = fromByteArray(Arrays.copyOfRange(receive, 0, 4));
                int receivedY = fromByteArray(Arrays.copyOfRange(receive, 4, 8));
                boolean isLeftButtonPressed = receive[9] > 0;
                boolean isRightButtonPressed = receive[10] > 0;
                int mouseWheel = receive[11];

                toX = (int) (receivedX * bias);
                toY = (int) (receivedY * bias);

                mouse.toClick(isLeftButtonPressed, isRightButtonPressed);
                mouse.wheel(mouseWheel);
                mouse.move(initialX + toX, initialY + toY);

            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    String ipArrayToString(byte[] ip){
        int[] convertIp = new int[4];
        for(int i = 0; i < 4; i++){
            convertIp[i] = ip[i] < 0? ip[i] + 256: ip[i];
        }
        return convertIp[0] + "." + convertIp[1] + "." + convertIp[2] + "." + convertIp[3];
    }

    int fromByteArray(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                ((bytes[3] & 0xFF) << 0);
    }
}


