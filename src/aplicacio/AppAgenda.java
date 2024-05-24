package aplicacio;

import classes.*;
import eines.EinesJoan;
import menu.Menu;

import java.io.IOException;

import static classes.GuardarRecuperar.guardarAgenda;
import static classes.GuardarRecuperar.recuperarAgenda;
import static eines.EinesJoan.fitxerTrobat;


public class AppAgenda {
    private static final String NOM_FITXER = "agenda";
    private static final String EXTENSIO_FITXER = ".ajp";
    private static Agenda agenda = new Agenda();

    private static void afegirContacte() {
        EinesJoan.pintaComSubTitol("Afegir Dades del Contacte");
        String nom = Teclat.llegirCadena("Nom: ");
        String cognom = Teclat.llegirCadena("Cognom: ");

        Contacte contacte = new Contacte(nom, cognom);

        EinesJoan.pintaComSubTitol("Afegir Telefon del Contacte");
        int numeroTelefon = Teclat.llegirEnter("Número de telèfon: ");
        System.out.print("Etiqueta (Mòbil, Casa, Treball, etc.): ");
        String etiqueta = Teclat.llegirCadena();
        Telefon telefon = new Telefon(numeroTelefon, etiqueta);
        agenda.afegirTelefonContacte(contacte, telefon);
        System.out.println("Telèfon de contacte afegit amb èxit!");
        EinesJoan.pintaComSubTitol("Afegir Adreça del Contacte");
        String etiquetaAdressaContacte = Teclat.llegirCadena("Etiqueta de l'adreça: ");
        String carrerConctacte = Teclat.llegirCadena("Carrer: ");
        int numeroCarrerConctacte = Teclat.llegirEnter("Número del carrer: ");
        String ciutatContacte = Teclat.llegirCadena("Ciutat: ");
        String codiPostalContacte = Teclat.llegirCadena("Codi postal: ");
        String paisContacte = Teclat.llegirCadena("País: ");
        Adressa adresssaAdressaContacte = new Adressa(
                carrerConctacte, numeroCarrerConctacte, ciutatContacte,
                codiPostalContacte, paisContacte,
                etiquetaAdressaContacte);
        contacte.afegirAdressa(adresssaAdressaContacte);

        System.out.println("Adreça contacte afegida amb èxit!!");
        System.out.println("Contacte afegit amb èxit!");
    }

    private static void buscarContacte() {
        EinesJoan.pintaComSubTitol("Buscar Contacte");

        System.out.print("Nom a buscar: ");
        String nom = Teclat.llegirCadena();

        var contactes = agenda.buscaContactePerNom(nom);

        if (contactes == null) {
            System.out.println("No s'han trobat contactes amb aquest nom.");
        } else {
            for (Contacte contacte : contactes) {
                System.out.println(contacte);
            }
        }
    }

    private static void modificarContacte() {
        EinesJoan.pintaComSubTitol("Modificar Contacte");

        System.out.print("Nom o Cognom del contacte a modificar: ");
        String criteri = Teclat.llegirCadena();
        var contactes = agenda.buscaContactePerNomOCognom(criteri);

        if (contactes == null) {
            System.out.println("No s'ha trobat cap contacte amb aquest criteri.");
            return;
        }

        if (contactes.size() == 1) {
            modificarContacte(contactes.get(0));
        } else {
            System.out.println("S'han trobat múltiples contactes:");
            for (int i = 0; i < contactes.size(); i++) {
                System.out.println((i + 1) + ". " + contactes.get(i));
            }

            System.out.print("Escull el número del contacte a modificar: ");
            int index = Teclat.llegirEnter() - 1;

            if (index >= 0 && index < contactes.size()) {
                modificarContacte(contactes.get(index));
            } else {
                System.out.println("Opció no vàlida.");
            }
        }
    }

    private static void modificarContacte(Contacte contacte) {
        System.out.print("Nou nom: ");
        String nouNom = Teclat.llegirCadena();
        System.out.print("Nou cognom: ");
        String nouCognom = Teclat.llegirCadena();

        contacte.setNom(nouNom);
        contacte.setCognom(nouCognom);

        System.out.println("Contacte modificat amb èxit!");
    }
    public static void mostrarContactes() {
        for (Contacte contacte : agenda.getLlistaContactes()) {
            System.out.println("Nom: " + contacte.getNom());
            System.out.println("Cognom: " + contacte.getCognom());
            System.out.println("Telèfons:");
            for (Telefon telefon : contacte.getLlistaTelefons()) {
                System.out.format("%s", telefon.toString());
                if(telefon != null){
                    System.out.println();
                }
            }
            System.out.println("Adreces:");
            for (Adressa adressa : contacte.getLlistaAdreces()) {
                System.out.format("%s", adressa.toString());
                if(adressa != null){
                    System.out.println();
                }
            }
            System.out.println();
        }
    }
    private static void eliminarContacte() {
        EinesJoan.pintaComSubTitol("Eliminar Contacte");

        System.out.print("Nom del contacte a eliminar: ");
        String nom = Teclat.llegirCadena();
        System.out.print("Cognom del contacte a eliminar: ");
        String cognom = Teclat.llegirCadena();

        boolean eliminat = agenda.eliminarContacte(nom, cognom);

        if (eliminat) {
            System.out.println("Contacte eliminat amb èxit!");
        } else {
            System.out.println("No s'ha trobat el contacte a eliminar.");
        }
    }
    public static void main(String[] args) throws IOException {
        String[] opcions = {
                "Afegir Contacte",
                "Buscar Contacte",
                "Modificar Contacte",
                "Eliminar Contacte",
                "Mostrar Contactes",
                "Sortir"
        };

        String nomFitxerAmbAgenda = NOM_FITXER + EXTENSIO_FITXER;
        boolean sortir = false;
        // Mirem si hi ha un arxiu amb el nom del fitxer amb l'Agenda
        if(fitxerTrobat(nomFitxerAmbAgenda) != null){
            // S'ha trobat un arxiu
            agenda = new Agenda(recuperarAgenda(nomFitxerAmbAgenda));
        }
        while (!sortir) {
            int opcio = Menu.obteOpcioMenu(opcions,
                    "Menu Principal",
                    "Escull una opció");
            switch (opcio) {
                case 1:
                    afegirContacte();
                    break;
                case 2:
                    buscarContacte();
                    break;
                case 3:
                    modificarContacte();
                    break;
                case 4:
                    eliminarContacte();
                    break;
                case 5:
                    mostrarContactes();
                    break;
                case 6:
                    guardarAgenda(nomFitxerAmbAgenda);
                    sortir = true;
                    EinesJoan.pintaComiat();
                    break;
                default:
                    System.out.println("Opció no vàlida!");
            }
        }
    }
}