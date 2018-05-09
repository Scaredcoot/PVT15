package StarGaze;



public class Place {
	
	private String name;
	private String temperature;
	private int rainfall;
	private int cloudiness;
	private String url;
	private int grade;
	
	//------------------------------Contructors------------------------------------------
	
	
	
	//------------------------------Methods------------------------------------------
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public int getRainfall() {
		return rainfall;
	}
	public void setRainfall(int rainfall) {
		this.rainfall = rainfall;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getCloudiness() {
		return cloudiness;
	}
	public void setCloudiness(int cloudiness) {
		this.cloudiness = cloudiness;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
}
