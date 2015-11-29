/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicast.cliente;

import java.util.LinkedList;
import multicast.cliente.gui.JFrameMain;
import multicast.lib.Log;

/**
 *
 * @author PROF_VESPERTINO
 */
public class HiloPrincipal extends Thread{
    private Thread hiloCompresor;
    private Thread hiloEnvio;    
    private LinkedList<String> listaSelecionados;
    private LinkedList<String> listaComprimidos;
    private JFrameMain ventana;
    private Log log;

    public HiloPrincipal(LinkedList<String> listaSelecionados, JFrameMain ventana) {
        this.listaSelecionados = listaSelecionados;
        listaComprimidos = new LinkedList<String>();    
        log = new Log(ventana.getJTextAreaLog());
    }
       
    
    @Override
    public void run() {                
        // Arrancamos hilos
        hiloCompresor = new HiloCompresor(listaSelecionados, listaComprimidos, log);        
        hiloEnvio = new HiloEnvio(listaComprimidos, log);        
        hiloCompresor.start();
        hiloEnvio.start();
        
        
    }
    
}
