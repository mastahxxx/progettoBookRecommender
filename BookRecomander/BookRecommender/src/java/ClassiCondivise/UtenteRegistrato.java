/**
 * @author Adrian Gabriel Soare 749483
 * @author Abdullah Waheed Malik 756789
 * 
 * 
 * 
 */






package ClassiCondivise;

import java.io.Serializable;

/**
 * Rappresenta un utente registrato con dati anagrafici e credenziali di accesso.
 */
public class UtenteRegistrato implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nomeCognome;
    private String codiceFiscale;
    private String mail;
    private String userId;
    private String password;
    private boolean controllo;

    //Getter

    /** @return nome e cognome dell'utente */
    public String getNomeCognome() { return this.nomeCognome; }

    /** @return codice fiscale dell'utente */
    public String getCodiceFiscale() { return this.codiceFiscale; }

    /** @return indirizzo e-mail dell'utente */
    public String getMail() { return this.mail; }

    /** @return user ID scelto dall'utente */
    public String getUserId() { return this.userId; }

    /** @return password dell'utente */
    public String getPassoword() { return this.password; }

    /** @return flag di controllo */
    public boolean getControllo() { return this.controllo; }

    //Setter

    /** @param nomeCognome nuovo nome e cognome */
    public void setNomeCognome(String nomeCognome) { this.nomeCognome = nomeCognome; }

    /** @param codiceFiscale nuovo codice fiscale */
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

    /** @param mail nuovo indirizzo e-mail */
    public void setMail(String mail) { this.mail = mail; }

    /** @param userId nuovo user ID */
    public void setUserId(String userId) { this.userId = userId; }

    /** @param password nuova password */
    public void setPassoword(String password) { this.password = password; }

    /** @param c nuovo valore del flag di controllo */
    public void setControllo(boolean c) { this.controllo = c; }
}
