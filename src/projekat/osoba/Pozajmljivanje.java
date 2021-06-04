package projekat.osoba;

import projekat.Main;
import projekat.knjiga.Knjiga;
import projekat.osoba.AbstractKorisnik;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Pozajmljivanje {
	private static final double MNOZILAC = Main.pravila.getMultiplier();
	private String clanUUID;
	private double dug = 0.0D;
	private Calendar datumPozajmljivanja, datumVracanja;
	private Knjiga pozajmljenaKnjiga;
	private boolean razreseno;



	public Pozajmljivanje(String UUID, double dug, Knjiga pozajmljenaKnjiga, Calendar datumPozajmljivanja, Calendar datumVracanja, boolean razreseno) {
		this.setClanUUID(UUID);
		this.setDug(dug);
		this.setPozajmljenaKnjiga(pozajmljenaKnjiga);
		this.setDatumPozajmljivanja(datumPozajmljivanja);
		this.setDatumVracanja(datumVracanja);
		this.setRazreseno(razreseno);
	}

	public Pozajmljivanje() {
		this.dug = 0;
		this.datumPozajmljivanja = Calendar.getInstance();
		this.datumVracanja = Calendar.getInstance();
		this.pozajmljenaKnjiga = new Knjiga();
	}

	public boolean isRazreseno() { return razreseno; }

	public void setRazreseno(boolean razreseno) { this.razreseno = razreseno; }

	public String getClanUUID() { return clanUUID; }

	public void setClanUUID(String clanUUID) { this.clanUUID = clanUUID; }

	public double getDug() {
		return dug;
	}

	public void setDug(double dug) {
		this.dug = dug;
	}

	public Knjiga getPozajmljenaKnjiga() {
		return pozajmljenaKnjiga;
	}

	public void setPozajmljenaKnjiga(Knjiga pozajmljeneKnjige) {
		this.pozajmljenaKnjiga = pozajmljeneKnjige;
	}

	public Calendar getDatumPozajmljivanja() {
		return datumPozajmljivanja;
	}

	public void setDatumPozajmljivanja(Calendar datumPozajmljivanja) {
		this.datumPozajmljivanja = datumPozajmljivanja;
	}

	public Calendar getDatumVracanja() {
		return datumVracanja;
	}

	public void setDatumVracanja(Calendar datumVracanja) {
		this.datumVracanja = datumVracanja;
	}

	public void produziZajam() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, Main.pravila.getMaxPeriod());
		this.datumVracanja = calendar;
	}

	public void izracunajDug() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		if (calendar.get(Calendar.DATE) > datumVracanja.get(Calendar.DATE)) {
			this.dug = 1;
			for (int i = 0; i < Math.abs(ChronoUnit.DAYS.between(calendar.toInstant(), datumPozajmljivanja.toInstant())); i++) {
				this.dug *= MNOZILAC;
			}
			this.dug = Math.abs(ChronoUnit.DAYS.between(calendar.toInstant(), datumPozajmljivanja.toInstant()) * MNOZILAC);
		}
		System.out.println("Dug: " + dug);
	}

	public String toSerializableString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return String.format("%s~%.2f~(%s)~%s~%s~%s", getClanUUID(), getDug(), getPozajmljenaKnjiga(), dateFormat.format(getDatumPozajmljivanja().toString()), dateFormat.format(getDatumVracanja()), isRazreseno());
	}
}
