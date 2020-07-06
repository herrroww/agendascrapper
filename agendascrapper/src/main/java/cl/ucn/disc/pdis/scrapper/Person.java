package cl.ucn.disc.pdis.scrapper;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Person")
public class Person {

    //Field of database

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String position;

    @DatabaseField
    private String unit;

    @DatabaseField
    private String email;

    @DatabaseField
    private String phone;

    @DatabaseField
    private String office;

    @DatabaseField
    private String address;


    // Constructor.

    public Person() {

     // Void.
    }

    /**
     * Contructor for field.
     *
     * @param id
     * @param name
     * @param position
     * @param unit
     * @param email
     * @param phone
     * @param office
     * @param address
     */
    public Person(int id, String name, String position, String unit, String email, String phone, String office, String address) {

        this.id = id;

        this.name = name;

        this.position = position;

        this.unit = unit;

        this.email = email;

        this.phone = phone;

        this.office = office;

        this.address = address;
    }

    /**
     * to String
     *
     * @return toString.
     */

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", unit='" + unit + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", office='" + office + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
