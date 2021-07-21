/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 *
 * @author roberto
 */
public class servidor {

    /**
     * @param args the command line arguments
     */
    
     private static final int puerto = 5555;
    private Socket socket;
    //Directorio donde estan los archivos en el servidor
    private static final File Directorio = new File("/home/roberto/Escritorio/servidor"); 
    private DataInputStream RECIBE;
    private DataOutputStream ENVIA;

    servidor() throws IOException {
        //Creo un ServerSocket
        ServerSocket server = new ServerSocket(puerto);
        System.out.println("Servidor en funcionamiento");
      
        //Ciclo infinito para recibir peticiones siempre
        while (true) {
            //Acepto la conexion
            socket = server.accept();

            //Para recibir datos
            RECIBE = new DataInputStream(socket.getInputStream());
            //Para enviar datos
            ENVIA = new DataOutputStream(socket.getOutputStream());

            System.out.println("conexion desde " + socket);
            try {

                while (true) {

                    //Guardo lo que el servidor recibe
                    String archivo = RECIBE.readUTF();
                    //Junto el directorio raiz con el nombre del archivo solicitado
                    File Server_archivo = new File(Directorio.getPath() + "//" + archivo);

                    //En caso de existir el archivo se lo hago saber al cliente
                    ENVIA.writeBoolean(Server_archivo.exists());

                    //En caso de existir hago lo siguiehte
                    if (Server_archivo.exists()) {
                        //Leer el flujo de datos
                        BufferedInputStream BIS = new BufferedInputStream(new FileInputStream(Server_archivo));

                        BufferedOutputStream BOS = new BufferedOutputStream(socket.getOutputStream());

                        byte[] buffer = new byte[8192];
                        int in;
                        while ((in = BIS.read(buffer)) != -1) {
                            BOS.write(buffer, 0, in);
                        }
                        System.out.println("Se ha enviado: " + Server_archivo.getName());
                        BIS.close();
                        BOS.close();
                    } else {
                        //En caso de no existir imprimo ese mensaje
                        System.out.println("No se encuentra: " + archivo);
                    }

                }
            } catch (Exception e) {
                System.out.println("Cliente desconectado \n");
                //Cierro el socket
                socket.close();
            }
        }
    } 
   
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        new servidor();
    }
    
}
