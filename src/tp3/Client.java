package tp3;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String hote = args.length > 0 ? args[0] : "localhost";
        int port = 1234;

        try (Socket socket = new Socket(hote, port);
             BufferedReader entree = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter sortie = new PrintWriter(socket.getOutputStream(), true);
             Scanner clavier = new Scanner(System.in)) {

            String ligne;
            while ((ligne = entree.readLine()) != null) {
                System.out.println(ligne);
                if (ligne.startsWith("Tapez un message")) {
                    break;
                }
            }

            while (true) {
                System.out.print("> ");
                String message = clavier.nextLine();
                sortie.println(message);

                String reponse = entree.readLine();
                if (reponse == null) break;
                System.out.println(reponse);

                if ("quit".equalsIgnoreCase(message.trim())) break;
            }
        } catch (IOException e) {
            System.err.println("Impossible de contacter le serveur : " + e.getMessage());
        }
    }
}
