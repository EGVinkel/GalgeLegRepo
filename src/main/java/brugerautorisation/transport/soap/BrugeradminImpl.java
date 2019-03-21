package brugerautorisation.transport.soap;

import brugerautorisation.data.Bruger;
import brugerautorisation.server.Brugerdatabase;

import javax.jws.WebService;

@WebService(endpointInterface = "brugerautorisation.transport.soap.Brugeradmin")
public class BrugeradminImpl implements Brugeradmin {
	Brugerdatabase db;

	@Override
	public Bruger hentBruger(String brugernavn, String adgangskode) {
		return db.hentBruger(brugernavn, adgangskode);
	}

	@Override
	public Bruger ændrAdgangskode(String brugernavn, String glAdgangskode, String nyAdgangskode) {
    return db.ændrAdgangskode(brugernavn, glAdgangskode, nyAdgangskode);
	}


	@Override
	public Object getEkstraFelt(String brugernavn, String adgangskode, String feltnavn) {
		return db.hentBruger(brugernavn, adgangskode).ekstraFelter.get(feltnavn);
	}

	@Override
	public void setEkstraFelt(String brugernavn, String adgangskode, String feltnavn, Object værdi) {
		db.hentBruger(brugernavn, adgangskode).ekstraFelter.put(feltnavn, værdi);
		db.gemTilFil(false);
	}

  @Override
  public Bruger hentBrugerOffentligt(String brugernavn) {
		return db.hentBrugerOffentligt(brugernavn);
  }
}
