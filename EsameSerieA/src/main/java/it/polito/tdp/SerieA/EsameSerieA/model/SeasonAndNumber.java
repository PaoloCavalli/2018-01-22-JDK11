package it.polito.tdp.SerieA.EsameSerieA.model;

public class SeasonAndNumber {
Season s;
Integer punti;
/**
 * @param s
 * @param punti
 */
public SeasonAndNumber(Season s, Integer punti) {
	super();
	this.s = s;
	this.punti = punti;
}
public Season getS() {
	return s;
}
public void setS(Season s) {
	this.s = s;
}
public Integer getPunti() {
	return punti;
}
public void setPunti(Integer punti) {
	this.punti = punti;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((s == null) ? 0 : s.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	SeasonAndNumber other = (SeasonAndNumber) obj;
	if (s == null) {
		if (other.s != null)
			return false;
	} else if (!s.equals(other.s))
		return false;
	return true;
}

}
