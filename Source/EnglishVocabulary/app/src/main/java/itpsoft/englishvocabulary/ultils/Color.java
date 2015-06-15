package itpsoft.englishvocabulary.ultils;


public class Color {
	private String string1;
	private String string2;
	private String string3;
	private String string4;
	private String string5;
	private Color() {
		string1 = "qwerty";
		string2 = "uiopa";
		string3 = "sdfgh";
		string4 = "jklzx";
		string5 = "cvbnm";
	}
	private static Color color;
	public static Color instance() {
		if(color == null){
			color = new Color();
		}
		return color;
	}
	public String getColor(String string) {
		String color = "#c5c5c5";
		String onChar = string.substring(0, 1).toLowerCase();
		
		if(string1.contains(onChar))
			color = "#00ff12";
		else if(string2.contains(onChar))
			color = "#c45d18";
		else if(string3.contains(onChar))
			color = "#c41840";
		else if(string4.contains(onChar))
			color = "#c41818";
		else if(string5.contains(onChar))
			color = "#8518c4";
		
		return color;
	}
}
