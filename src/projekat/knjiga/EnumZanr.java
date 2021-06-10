package projekat.knjiga;

import java.util.HashMap;
import java.util.Map;

public enum EnumZanr {
	KOMEDIJA(0),
	ANIMIRANI(1),
	NAUCNA_FANTASTIKA(2),
	EP(3),
	ISTORIJA(4),
	MISTERIJA(5),
	AKCIJA(6),
	AVANTURA(7),
	BAJKA(8),
	FILOZOFIJA(8),
	KULTURA(9),
	REALZIAM(10),
	RELIGIJA(11),
	HOROR(12),
	TRILER(13),
	KRIMI(14),
	DECIJI(15),
	ROMANTIKA(16),
	DOKUMENTARNI(17),
	BIOGRAFIJA(18),
	EDUKATIVNO(19),
	AKADEMSKI_RAD(20),
	PUTOVANJE(21);

	private final int redniBroj;
	private static final Map<Integer, EnumZanr> mapaZanrova = new HashMap<>();

	static {
		for (EnumZanr zanr : EnumZanr.values()) {
			mapaZanrova.put(zanr.redniBroj, zanr);
		}
	}

	EnumZanr(int redniBroj) {
		this.redniBroj = redniBroj;
	}

	public static EnumZanr getZanr(int redniBroj) {
		return mapaZanrova.get(redniBroj);
	}

	public int getRedniBroj() {
		return redniBroj;
	}
}
