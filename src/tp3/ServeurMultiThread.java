package tp3;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ServeurMultiThread {
    private static final int PORT = 1234;
    private static final AtomicInteger compteurClients = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serveur = new ServerSocket(PORT)) {
            System.out.println("Serveur multi-thread démarré sur le port " + PORT);

            while (true) {
                Socket socketClient = serveur.accept();
                int numeroClient = compteurClients.incrementAndGet();

                System.out.println("Client n°" + numeroClient
                        + " connecté depuis " + socketClient.getRemoteSocketAddress());

                Thread thread = new Thread(
                        new ClientProcess(socketClient, numeroClient),
                        "ClientProcess-" + numeroClient
                );
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur : " + e.getMessage());
        }
    }
}

class ClientProcess implements Runnable {
    private final Socket socket;
    private final int numeroClient;

    ClientProcess(Socket socket, int numeroClient) {
        this.socket = socket;
        this.numeroClient = numeroClient;
    }

    @Override
    public void run() {
        try (Socket client = socket;
             BufferedReader entree = new BufferedReader(
                     new InputStreamReader(client.getInputStream()));
             PrintWriter sortie = new PrintWriter(client.getOutputStream(), true)) {

            sortie.println("Bienvenue ! Vous êtes le client n°" + numeroClient);
            sortie.println("Tapez un message; tapez 'quit' pour quitter.");

            String message;
            while ((message = entree.readLine()) != null) {
                if ("quit".equalsIgnoreCase(message.trim())) {
                    sortie.println("Au revoir, client n°" + numeroClient);
                    break;
                }
                sortie.println("Serveur reçu du client n°" + numeroClient + " : " + message);
            }
        } catch (IOException e) {
            System.err.println("Erreur avec le client n°" + numeroClient + " : " + e.getMessage());
        } finally {
            System.out.println("Client n°" + numeroClient + " déconnecté.");
        }
    }
}
