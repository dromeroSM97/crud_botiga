/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package src;

import dao.ClientDAO;
import dao.ComandaDAO;
import dao.ConsultesDAO;
import dao.DescompteDAO;
import dao.ProducteDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import model.Client;
import model.Comanda;
import model.Descompte;
import model.LiniaComanda;
import model.Producte;

public class App {

    private static final Scanner sc = new Scanner(System.in);
    private static final ProducteDAO producteDAO = new ProducteDAO();
    private static final ClientDAO clientDAO = new ClientDAO();
    private static final DescompteDAO descompteDAO = new DescompteDAO();
    private static final ComandaDAO comandaDAO = new ComandaDAO();
    private static final ConsultesDAO consultesDAO = new ConsultesDAO();

    public static void main(String[] args) {
        int opcio;
        do {
            System.out.println("\n===== BOTIGA ONLINE =====");
            System.out.println("1. Gestionar Productes (CRUD)");
            System.out.println("2. Gestionar Clients (CRUD)");
            System.out.println("3. Gestionar Descomptes (CRUD)");
            System.out.println("4. Crear Comanda (Transacció + Savepoint)");
            System.out.println("5. Llistar Comandes d'un Client (JOIN)");
            System.out.println("0. Sortir");
            opcio = llegirEnter("Opció: ");

            try {
                switch (opcio) {
                    case 1:
                        menuProductes();
                        break;
                    case 2:
                        menuClients();
                        break;
                    case 3:
                        menuDescomptes();
                        break;
                    case 4:
                        crearComanda();
                        break;
                    case 5:
                        llistarComandesClient();
                        break;
                    case 0:
                        System.out.println("Fins aviat!");
                        break;
                    default:
                        System.out.println("Opció no vàlida.");
                }
            } catch (SQLException e) {
                System.err.println("Error de Base de Dades: " + e.getMessage());
            }
        } while (opcio != 0);

        sc.close();
    }

    private static void menuProductes() throws SQLException {
        int op;
        do {
            System.out.println("\n--- GESTIÓ DE PRODUCTES ---");
            System.out.println("1. Llistar productes");
            System.out.println("2. Afegir producte");
            System.out.println("3. Actualitzar producte");
            System.out.println("4. Eliminar producte");
            System.out.println("0. Tornar");
            op = llegirEnter("Opció: ");

            switch (op) {
                case 1:
                    List<Producte> productes = producteDAO.llistar();
                    System.out.println("\nLlistat de productes:");
                    for (Producte p : productes) {
                        System.out.printf("ID: %d | %s | Preu: %.2f € | Estoc: %d%n",
                                p.getId(), p.getNom(), p.getPreu(), p.getEstoc());
                    }
                    break;
                case 2:
                    System.out.print("Nom del producte: ");
                    String nom = sc.nextLine();
                    double preu = llegirDouble("Preu (€): ");
                    int estoc = llegirEnter("Estoc inicial: ");
                    producteDAO.inserir(new Producte(0, nom, preu, estoc));
                    System.out.println("Producte afegit correctament.");
                    break;
                case 3:
                    int idAct = llegirEnter("ID del producte a actualitzar: ");
                    Producte existent = producteDAO.obtenirPerId(idAct);
                    if (existent == null) {
                        System.out.println("No existeix cap producte amb aquest ID.");
                        break;
                    }
                    System.out.print("Nou nom (" + existent.getNom() + "): ");
                    String nouNom = sc.nextLine();
                    double nouPreu = llegirDouble("Nou preu (" + existent.getPreu() + "): ");
                    int nouEstoc = llegirEnter("Nou estoc (" + existent.getEstoc() + "): ");
                    producteDAO.actualitzar(new Producte(idAct, nouNom, nouPreu, nouEstoc));
                    System.out.println("Producte actualitzat correctament.");
                    break;
                case 4:
                    int idDel = llegirEnter("ID del producte a eliminar: ");
                    producteDAO.eliminar(idDel);
                    System.out.println("Producte eliminat (si existia).");
                    break;
            }
        } while (op != 0);
    }

    private static void menuClients() throws SQLException {
        int op;
        do {
            System.out.println("\n--- GESTIÓ DE CLIENTS ---");
            System.out.println("1. Llistar clients");
            System.out.println("2. Afegir client");
            System.out.println("3. Actualitzar client");
            System.out.println("4. Eliminar client");
            System.out.println("0. Tornar");
            op = llegirEnter("Opció: ");

            switch (op) {
                case 1:
                    List<Client> clients = clientDAO.llistar();
                    System.out.println("\nLlistat de clients:");
                    for (Client c : clients) {
                        System.out.printf("ID: %d | Nom: %s | Correu: %s%n",
                                c.getId(), c.getNom(), c.getCorreu());
                    }
                    break;
                case 2:
                    System.out.print("Nom del client: ");
                    String nom = sc.nextLine();
                    System.out.print("Correu electrònic: ");
                    String correu = sc.nextLine();
                    clientDAO.inserir(new Client(0, nom, correu));
                    System.out.println("Client afegit correctament.");
                    break;
                case 3:
                    int idAct = llegirEnter("ID del client a actualitzar: ");
                    Client existent = clientDAO.obtenirPerId(idAct);
                    if (existent == null) {
                        System.out.println("No existeix cap client amb aquest ID.");
                        break;
                    }
                    System.out.print("Nou nom (" + existent.getNom() + "): ");
                    String nouNom = sc.nextLine();
                    System.out.print("Nou correu (" + existent.getCorreu() + "): ");
                    String nouCorreu = sc.nextLine();
                    clientDAO.actualitzar(new Client(idAct, nouNom, nouCorreu));
                    System.out.println("Client actualitzat correctament.");
                    break;
                case 4:
                    int idDel = llegirEnter("ID del client a eliminar: ");
                    clientDAO.eliminar(idDel);
                    System.out.println("Client eliminat (si existia).");
                    break;
            }
        } while (op != 0);
    }

    private static void menuDescomptes() throws SQLException {
        int op;
        do {
            System.out.println("\n--- GESTIÓ DE DESCOMPTES ---");
            System.out.println("1. Llistar descomptes");
            System.out.println("2. Afegir descompte");
            System.out.println("3. Actualitzar descompte");
            System.out.println("4. Eliminar descompte");
            System.out.println("0. Tornar");
            op = llegirEnter("Opció: ");

            switch (op) {
                case 1:
                    List<Descompte> descomptes = descompteDAO.llistar();
                    System.out.println("\nLlistat de descomptes:");
                    for (Descompte d : descomptes) {
                        System.out.printf("ID: %d | Producte ID: %d | Tipus: %s | Valor: %.2f%n",
                                d.getId(), d.getProducteId(), d.getTipus(), d.getValor());
                    }
                    break;
                case 2:
                    int prodId = llegirEnter("ID del producte: ");
                    System.out.print("Tipus de descompte (% o €): ");
                    String tipus = sc.nextLine().trim();
                    double valor = llegirDouble("Valor del descompte: ");
                    descompteDAO.inserir(new Descompte(prodId, tipus, valor));
                    System.out.println("Descompte afegit correctament.");
                    break;
                case 3:
                    int idAct = llegirEnter("ID del descompte a actualitzar: ");
                    int nouProdId = llegirEnter("Nou ID del producte: ");
                    System.out.print("Nou tipus (% o €): ");
                    String nouTipus = sc.nextLine().trim();
                    double nouValor = llegirDouble("Nou valor: ");
                    descompteDAO.actualitzar(new Descompte(idAct, nouProdId, nouTipus, nouValor));
                    System.out.println("Descompte actualitzat correctament.");
                    break;
                case 4:
                    int idDel = llegirEnter("ID del descompte a eliminar: ");
                    descompteDAO.eliminar(idDel);
                    System.out.println("Descompte eliminat.");
                    break;
            }
        } while (op != 0);
    }

    private static void crearComanda() {
        System.out.println("\n--- NOVA COMANDA ---");
        try {
            for (Client c : clientDAO.llistar()) {
                System.out.printf("Client [%d] %s (%s)%n", c.getId(), c.getNom(), c.getCorreu());
            }
            int clientId = llegirEnter("Introdueix l'ID del client: ");
            Comanda comanda = new Comanda(clientId);

            System.out.println("\nProductes disponibles:");
            for (Producte p : producteDAO.llistar()) {
                System.out.printf("Producte [%d] %s - %.2f € (Estoc: %d)%n",
                        p.getId(), p.getNom(), p.getPreu(), p.getEstoc());
            }

            String continuar;
            do {
                int prodId = llegirEnter("ID del producte a afegir: ");
                int quantitat = llegirEnter("Quantitat: ");
                comanda.afegirLinia(new LiniaComanda(prodId, quantitat, 0.0));

                System.out.print("Vols afegir un altre producte? (s/n): ");
                continuar = sc.nextLine().trim().toLowerCase();
            } while (continuar.equals("s"));

            comandaDAO.crearComanda(comanda);

        } catch (SQLException e) {
            System.out.println("No s'ha pogut completar la comanda: " + e.getMessage());
        }
    }

    private static void llistarComandesClient() throws SQLException {
        System.out.println("\n--- HISTORIAL DE COMANDES PER CLIENT ---");
        int clientId = llegirEnter("Introdueix l'ID del client: ");
        consultesDAO.llistarComandesClient(clientId);
    }

    private static int llegirEnter(String missatge) {
        while (true) {
            System.out.print(missatge);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número enter vàlid.");
            }
        }
    }

    private static double llegirDouble(String missatge) {
        while (true) {
            System.out.print(missatge);
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número decimal vàlid.");
            }
        }
    }
}