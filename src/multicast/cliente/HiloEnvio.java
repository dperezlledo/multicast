/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast.cliente;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PROF_VESPERTINO
 */
public class HiloEnvio extends Thread {

    private LinkedList<String> listaComprimidos;
    private MulticastSocket enviador;
    public static final String INET_ADDR = "224.0.0.3";
    public static final int PORT = 8888;
    private InetAddress addr;
    private DatagramSocket serverSocket;
    
    public HiloEnvio(LinkedList<String> listaComprimidos) {
        try {
            this.listaComprimidos = listaComprimidos; // Ficheros compridos
            // Nos creamos la conexion
            addr = InetAddress.getByName(INET_ADDR);         
            serverSocket = new DatagramSocket();
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(HiloEnvio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        String ficheroAEnviar;

        while (true) {
            try {                
                if (!listaComprimidos.isEmpty()) {
                    ficheroAEnviar = listaComprimidos.removeFirst();
                    enviarFichero(ficheroAEnviar);
                    System.out.println("Enviado " + ficheroAEnviar);
                } else {
                    sleep(500);
                }
                    
            } catch (Exception ex) {
                Logger.getLogger(HiloEnvio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    private void enviarFichero(String ficheroAEnviar) {
        int leidos;
        byte[] datos = new byte[1024];
        DatagramPacket msgPacket;
        byte fin[] = new byte[] {-1};
        
        try {            
            // Se abre el fichero original para lectura
            FileInputStream fileInput = new FileInputStream(ficheroAEnviar);
            BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
                                   
            leidos = bufferedInput.read(datos);
            while (leidos > 0) {
                msgPacket = new DatagramPacket(datos,datos.length, addr, PORT);
                serverSocket.send(msgPacket);                
                leidos=bufferedInput.read(datos);
            }
            // Aviso de fin de fichero
            msgPacket = new DatagramPacket(fin,1, addr, PORT);
            serverSocket.send(msgPacket);  
            // Cierre de los ficheros
            bufferedInput.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(HiloEnvio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(HiloEnvio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloEnvio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
