package cl.ucn.disc.pdis.scrapper;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

public class Agenda {

    // The Logger.
    public static Logger log = LoggerFactory.getLogger(Agenda.class);

    /**
     * Get PersonInfo.
     *
     * @param id .
     */
    private static Person getPersonInfo(Integer id) {
        final String direct = "http://online.ucn.cl/directoriotelefonicoemail/fichaGenerica/?cod=";
        Person newPerson = null;
        try {
            Document doc = Jsoup.connect(direct + id).get();

            String name = doc.getElementById("lblNombre").text();

            String position = doc.getElementById("lblCargo").text();

            String unit = doc.getElementById("lblUnidad").text();

            String email = doc.getElementById("lblEmail").text();

            String phone = doc.getElementById("lblTelefono").text();

            String office = doc.getElementById("lblOficina").text();

            String address = doc.getElementById("lblDireccion").text();

            if (!name.isEmpty()) {
                newPerson = new Person(id, name, position, unit, email, phone, office, address);
                log.debug("PersonContact: " + newPerson.toString());
            }


        } catch (IOException e) {
            log.error("Error retrieving contact info: " + e.getMessage());
        }

        return newPerson;

    }

    /**
     * The Main.
     *
     * @param args .
     */
    public static void main(String[] args) {
        // DataBase Configuration.
        Dao<Person, Integer> DaoPerson = null;

        ConnectionSource connection = null;

        String DirecUrl = "jdbc:sqlite:directorio.db";


        try {
            // Create connection with the db.
            connection = new JdbcConnectionSource(DirecUrl);
            // Message debug.
            log.debug("Data Base Conection Start");
            // Table.
            TableUtils.createTableIfNotExists(connection, Person.class);
            // The Dao.
            DaoPerson = DaoManager.createDao(connection, Person.class);

        } catch (SQLException e) {
            log.error("Error whit database connection " + e.getMessage());

        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (IOException e) {
                log.error("Error in database close connection " + e.getMessage());
            }


            // Search for last id in db and start scrapping from it.
            int actualId = 0;

            // Search for final Id.
            int finalId = 30000;


            // For every id get contact info.
            for (; actualId <= finalId; actualId++) {
                log.info("Get id Person. " + actualId);
                Person person = getPersonInfo(actualId);
                if (person != null) {
                    try {
                        DaoPerson.create(person);
                    } catch (SQLException e) {
                        log.error("Error info insert. " + e.getMessage());
                    }
                }

                try {
                    int sleep = 1500;
                    Thread.sleep(sleep);
                    log.info("Sleep of:" + sleep);
                } catch (InterruptedException e) {
                    log.debug("Sleep interrumped. " + e.getMessage());
                }

            }
            try {
                assert DaoPerson != null;
                String acId = DaoPerson.queryRaw("The max id from persona.").getFirstResult()[0];
                try {
                    actualId = Integer.parseInt(acId);
                } catch (Exception e) {
                    log.debug("Error empty DataBase?: " + e.getMessage());
                }
                log.info("Start scrap for id: " + actualId);
            } catch (SQLException e) {
                log.error("Error last id inserted: " + e.getMessage());
            }

        }

    }
}
