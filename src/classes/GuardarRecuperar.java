package classes;

import java.io.*;
import java.util.LinkedList;

// Importem la llista de contactes de l'agenda actual
import static classes.Agenda.getLlistaContactes;

public class GuardarRecuperar {
    private static final String MARCA_FINAL_TELEFONS = "M_F_T";
    private static final String MARCA_FINAL_ADRECES = "M_F_A";
    private static final String SEPARADOR_ETIQUETA_TELEFON = "#";

    public static LinkedList<Contacte> recuperarAgenda(String nomArxiu) throws IOException {
        BufferedReader bur;
        boolean hiHaTelefons = false;
        boolean hiHaAdreces = false;
        bur = new BufferedReader(new FileReader(nomArxiu));
        LinkedList<Contacte> llistaContactes = new LinkedList<>();

        String linia;
        String[] dadesTelefon;

        linia = bur.readLine();
        while (linia != null) {
            // Inicialitzem les llistes
            LinkedList<Telefon> llistaTelefons = new LinkedList<>();
            LinkedList<Adressa> llistaAdreces = new LinkedList<>();
            // Llegir nom i cognom del contacte
            String nom = linia;
            String cognom = bur.readLine();
            // llistaTelefons per afegir TOTS els telèfons llegits del contacte
            // Llegir l'etiqueta del telèfon del contacte
            linia = bur.readLine();
            hiHaTelefons = (linia != null && !linia.equals(MARCA_FINAL_TELEFONS));
//            hiHaContactes = !llistaContactes.isEmpty();
            while (hiHaTelefons) {
                dadesTelefon = null;
                // Hi ha telefons amb el format int telefon#String etiqueta
                dadesTelefon = linia.split(SEPARADOR_ETIQUETA_TELEFON);
                int numeroTelefon = Integer.parseInt(dadesTelefon[0]);
                String etiqueta = dadesTelefon[1];
                llistaTelefons.add(new Telefon(numeroTelefon, etiqueta));
                linia = bur.readLine();
                hiHaTelefons = (linia != null && !linia.equals(MARCA_FINAL_TELEFONS));
            }
            if(linia.equals(MARCA_FINAL_TELEFONS)){
                linia = bur.readLine();
            }
            hiHaAdreces = (linia != null && !linia.equals(MARCA_FINAL_ADRECES));
            // Pot ser MARCA_FINAL_ADRESSES, o etiqueta 1a Adreça
            while (hiHaAdreces) {
                String etiqueta = linia;
                String carrer = bur.readLine();
                int numeroCarrer = Integer.parseInt(bur.readLine());
                String codiPostal = bur.readLine();
                String ciutat = bur.readLine();
                String pais = bur.readLine();
                llistaAdreces.add(new Adressa(carrer, numeroCarrer, ciutat, codiPostal, pais, etiqueta));
                linia = bur.readLine();
                hiHaAdreces = (linia != null && !linia.equals(MARCA_FINAL_ADRECES));
            }
            Contacte contacte = new Contacte(nom, cognom, llistaTelefons, llistaAdreces);
            llistaContactes.add(contacte);
            linia = bur.readLine();
        }
        bur.close();
        return llistaContactes;
    }
    public static boolean guardarAgenda(String nomArxiu)
            throws IOException {
        // desa el contingut de la col·lecció donada en el primer paràmetre
        // a l'arxiu especificat pel segon paràmetre.

        boolean guardatCorrecte = false;
        if (getLlistaContactes().isEmpty()) {
            System.out.println("Agenda buida, no hi ha res a guardar!");
        } else {
            // Tipus de fitxer
            File file = new File(nomArxiu);
            try {

                // Tipus de canal
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                for (Contacte contacte : getLlistaContactes()) {
                    // Escriure el nom del contacte ...
                    writer.write(contacte.getNom() + "\n");

                    // Escriure el cognom del contacte ...
                    writer.write(contacte.getCognom() + "\n");

                    // Obtenir TOTS els telèfons del contacte per poder
                    // iterar sobre TOTS els telèfons del contacte.
                    for (Telefon telefon : contacte.getLlistaTelefons()) {
                        // Escriure el número del telèfon del contacte
                        writer.write(String.valueOf(telefon.getNumeroTelefon()));

                        // seguit del separador de l'etiqueta del telèfon i el número de teléfon.
                        writer.write(SEPARADOR_ETIQUETA_TELEFON);

                        // més escriure l'etiqueta del telèfon del contacte més el salt de línia
                        writer.write(telefon.getEtiqueta() + "\n");

                    }

                    // desar marca de MARCA_FINAL_TELEFONS per indicar
                    // que ja no hi ha més telèfons del contacte
                    writer.write(MARCA_FINAL_TELEFONS + "\n");

                    if (contacte.getLlistaAdreces() == null) {
                        System.out.printf("No hi ha adreces per guardar!");
                    } else {
                        // Obtenir TOTES les adreces del contacte per poder
                        for (Adressa adressa : contacte.getLlistaAdreces()) {

                            // iterar sobre TOTES les adreces del contacte.

                            writer.write(adressa.getEtiqueta() + "\n");

                            // Escriure el nom del carrer de l'adreça del contacte més el salt de línia
                            writer.write(adressa.getCarrer() + "\n");

                            // Escriure el número del carrer de l'adreça del contacte més el salt de línia
                            writer.write(adressa.getNumeroCarrer() + "\n");

                            // Escriure el codi postal del carrer de l'adreça del contacte més el salt de línia
                            writer.write(adressa.getCodiPostal() + "\n");

                            // Escriure el nom de la ciutat de l'adreça del contacte més el salt de línia
                            writer.write(adressa.getCiutat() + "\n");

                            // Escriure el nom del país de l'adreça del contacte més el salt de línia
                            writer.write(adressa.getPais() + "\n");
                        }

                        // desar marca de MARCA_FINAL_ADRESSES de la seqüència de telèfons  més el salt de línia
                        writer.write(MARCA_FINAL_ADRECES + "\n");
                    }
                }
                System.out.println("Agenda guardada al fitxer " +
                        nomArxiu + "!");
                guardatCorrecte = true;
                writer.close(); // Tancat canal
            } catch (Exception e) {
                System.out.println("Problemes a l'hora de llegir el fitxer de contactes!");
                // O millor, mostra un missatge d'error a l'usuari
                e.printStackTrace();            }
        }
        return guardatCorrecte;
    }
}
