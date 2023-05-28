package application.Tabela;

import java.util.Arrays;
import java.util.List;

public class Klasa {
	private Integer Od;
	private Integer Do;

	public Klasa(Integer od2, Integer do2) {
		Od = od2;
		Do = do2;
	}

	public Klasa(String od2, String do2) {
		// TODO Auto-generated constructor stub
	}

	public Integer getOd() {
		return Od;
	}

	public void setOd(Integer od) {
		Od = od;
	}

	public Integer getDo() {
		return Do;
	}

	public void setDo(Integer do1) {
		Do = do1;
	}

	@Override
	public String toString() {
		return Od + ", " + Do;
	}
	
	
	private String[] myArray;

    public Klasa(String[] myArray) {
        this.myArray = myArray;
    }

    public List<String> getAsList() {
        return Arrays.asList(myArray);
    }
    

}
